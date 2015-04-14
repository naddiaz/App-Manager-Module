package com.naddiaz.tfg.bletasker.webservices;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.utils.CustomRequest;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nad on 12/02/15.
 */
public class WSGcmRegistration {

    private Context ctx;
    private int id_airport;
    private String id_person;
    private String id_push;

    private static String URL;
    private static String URLunlink;


    public WSGcmRegistration(Context ctx, int id_airport, String id_person, String worker_name, String id_push) {
        this.ctx = ctx;
        this.id_airport = id_airport;
        this.id_person = id_person;
        this.id_push = id_push;
        this.URL = ctx.getResources().getString(R.string.ws_url_gcm_registration);
        this.URLunlink = ctx.getResources().getString(R.string.ws_url_gcm_registration_unlink);
    }

    public void saveRegistrationId(){
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        Map<String, String>  params = new HashMap<String, String>();
        params.put("id_airport", String.valueOf(id_airport));
        params.put("id_person", id_person);
        params.put("id_push", id_push);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response",response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", String.valueOf(error));
            }
        });
        queue.add(jsObjRequest);
    }

    public void unlinkRegistrationId(){
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        StringRequest postRequest = new StringRequest(Request.Method.POST, URLunlink,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_airport", String.valueOf(id_airport));
                params.put("id_person", id_person);
                params.put("id_push", id_push);

                return params;
            }
        };
        queue.add(postRequest);
    }
}
