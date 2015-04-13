package com.naddiaz.tfg.bletasker.webservices;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.database.Work;
import com.naddiaz.tfg.bletasker.database.WorksDbHelper;
import com.naddiaz.tfg.bletasker.utils.CustomRequest;
import com.naddiaz.tfg.bletasker.widget.MainActivity;
import com.naddiaz.tfg.bletasker.widget.RestoreUserActivity;

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


    public WSLoadWorks(Context ctx, String hash) {
        this.ctx = ctx;
        this.hash = hash;
        this.URL = ctx.getResources().getString(R.string.ws_url_load_works);
        worksDB = new WorksDbHelper(ctx);
    }

    public WSLoadWorks getWorks(){
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        Map<String, String>  params = new HashMap<String, String>();
        params.put("hash", hash);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,response.toString());
                        if(response.has("status")){
                            Toast.makeText(ctx, ctx.getString(R.string.ws_error_load_works), Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                worksDB.clearWorks();
                                getJSONWorks(response,Work.GROUP_COMPLETE);
                                getJSONWorks(response,Work.GROUP_ACTIVE);
                                getJSONWorks(response,Work.GROUP_PAUSE);
                                getJSONWorks(response,Work.GROUP_PENDING);
                                getJSONWorks(response,Work.GROUP_CANCEL);
                            } catch (JSONException e) {
                                e.printStackTrace();
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
        queue.add(jsObjRequest);
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

    public WSLoadWorks syncWorks(){
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        Map<String, String>  params = new HashMap<String, String>();
        params.put("hash", hash);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,response.toString());
                        if(response.has("status")){
                            Toast.makeText(ctx, ctx.getString(R.string.ws_error_load_works), Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                syncJSONWorks(response, Work.GROUP_COMPLETE);
                                syncJSONWorks(response, Work.GROUP_ACTIVE);
                                syncJSONWorks(response, Work.GROUP_PAUSE);
                                syncJSONWorks(response, Work.GROUP_PENDING);
                                syncJSONWorks(response, Work.GROUP_CANCEL);
                            } catch (JSONException e) {
                                e.printStackTrace();
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
        queue.add(jsObjRequest);
        return this;
    }

    private void syncJSONWorks(JSONObject response, String state) throws JSONException {
        JSONArray works = response.getJSONArray(state);
        if(works.length() > 0) {
            for (int i = 0; i < works.length(); i++) {
                JSONObject JSONwork = works.getJSONObject(i);
                Work actualWork = worksDB.getWork(JSONwork.getString(Work.FIELD_ID_TASK));
                if(actualWork == null){
                    createWork(worksDB, JSONwork, state);
                }
            }
        }
        else{
            worksDB.clearWorks();
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

    private void updateWork(WorksDbHelper worksDB, JSONObject JSONwork, String state) throws JSONException {
        worksDB.clearWork(JSONwork.getString(Work.FIELD_ID_TASK));
        createWork(worksDB,JSONwork,state);
    }
}
