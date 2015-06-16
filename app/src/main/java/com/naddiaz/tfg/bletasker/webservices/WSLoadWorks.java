package com.naddiaz.tfg.bletasker.webservices;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.*;
import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.database.Work;
import com.naddiaz.tfg.bletasker.database.WorksDbHelper;
import com.naddiaz.tfg.bletasker.utils.CustomRequest;
import com.naddiaz.tfg.bletasker.utils.RSACrypt;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nad on 12/02/15.
 */
public class WSLoadWorks {

    private static final String TAG = "WSLoadWorks";
    private Context ctx;
    private String hash;
    WorksDbHelper worksDB;
    private static String URL;
    private final VolleySingleton volley;
    private RequestQueue requestQueue;
    private UserPrefecences userPrefecences;
    private static final String DELIMITER = "==";

    public WSLoadWorks(Context ctx, String hash) {
        this.ctx = ctx;
        this.hash = hash;
        this.URL = ctx.getString(R.string.host) + ctx.getResources().getString(R.string.ws_url_load_works);
        this.userPrefecences = new UserPrefecences(ctx).readPreferences();
        worksDB = new WorksDbHelper(ctx);
        volley = VolleySingleton.getInstance(this.ctx);
        requestQueue = volley.getRequestQueue();
    }

    private void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (requestQueue == null) {
                requestQueue = volley.getRequestQueue();
            }
            requestQueue.getCache().clear();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            requestQueue.add(request);
        }
    }

    public WSLoadWorks getWorks(){

        String token= userPrefecences.getToken();
        Log.i("TOKEN: ",token);
        Map<String, String>  params = new HashMap<String, String>();
        params.put("data", RSACrypt.crypt(this.hash));
        params.put("token", RSACrypt.crypt(token));

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,response.toString());
                        if(response.has("status")){
                            try {
                                if(!response.getBoolean("status")) {
                                    Toast.makeText(ctx, ctx.getString(R.string.ws_error_load_works), Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Log.i(TAG,"La lista de trabajos esta actualizada");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            if(response.has("token")) {
                                try {
                                    String tokenB64 = response.getString("token");
                                    Log.i(TAG,tokenB64);
                                    String tokenDecrypt = RSACrypt.decrypt(tokenB64);
                                    Log.i(TAG,tokenDecrypt);
                                    userPrefecences.saveToken(tokenDecrypt);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(response.has("response")){
                                String responseDecrypt = "";
                                try {
                                    String responseB64 = response.getString("response");
                                    Log.i(TAG,responseB64);
                                    String[] splitResponse = responseB64.split(DELIMITER);
                                    for(int i=0; i<splitResponse.length; i++){
                                         responseDecrypt += RSACrypt.decrypt(splitResponse[i]+DELIMITER);
                                    }
                                    Log.i(TAG, responseDecrypt);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    worksDB.clearWorks();
                                    JSONObject responseD = new JSONObject(responseDecrypt);
                                    getJSONWorks(responseD,Work.GROUP_COMPLETE);
                                    getJSONWorks(responseD,Work.GROUP_ACTIVE);
                                    getJSONWorks(responseD, Work.GROUP_PAUSE);
                                    getJSONWorks(responseD,Work.GROUP_PENDING);
                                    getJSONWorks(responseD, Work.GROUP_CANCEL);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx,ctx.getString(R.string.ws_error_timeout_works),Toast.LENGTH_LONG).show();
                Log.d("Error.Response", String.valueOf(error));
            }
        });
        addToQueue(jsObjRequest);
        return this;
    }

    private void getJSONWorks(JSONObject response, String state) throws JSONException {
        JSONArray works = response.getJSONArray(state);
        if(works.length() > 0) {
            for (int i = 0; i < works.length(); i++) {
                JSONObject JSONwork = works.getJSONObject(i);
                createWork(worksDB, JSONwork, state);
            }
        }
    }

    private void createWork(WorksDbHelper worksDB, JSONObject JSONwork, String state) throws JSONException {
        Work work = new Work();
        work.setId_task(JSONwork.getString(Work.FIELD_ID_TASK));
        work.setDescription(JSONwork.getString(Work.FIELD_DESCRIPTION));
        work.setPriority(JSONwork.getInt(Work.FIELD_PRIORITY));
        work.setN_employees(JSONwork.getInt(Work.FIELD_N_EMPLOYEES));
        switch (state) {
            case Work.GROUP_COMPLETE:
                work.setState(Work.STATE_COMPLETE);
                break;
            case Work.GROUP_ACTIVE:
                work.setState(Work.STATE_ACTIVE);
                break;
            case Work.GROUP_PAUSE:
                work.setState(Work.STATE_PAUSE);
                break;
            case Work.GROUP_PENDING:
                work.setState(Work.STATE_PENDING);
                break;
            case Work.GROUP_CANCEL:
                work.setState(Work.STATE_CANCEL);
                break;
        }
        work.setCreated_at(JSONwork.getString(Work.FIELD_CREATED_AT));
        worksDB.createWork(work);
    }
}
