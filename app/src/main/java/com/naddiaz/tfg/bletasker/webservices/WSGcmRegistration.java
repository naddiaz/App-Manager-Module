package com.naddiaz.tfg.bletasker.webservices;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nad on 12/02/15.
 */
public class WSGcmRegistration {

    private View view;
    private Context ctx;
    private int id_airport;
    private String id_person;
    private String id_push;

    private static String URL = "http://tfg.naddiaz.com/gcm/registration";


    public WSGcmRegistration(Context ctx, int id_airport, String id_person, String id_push) {
        this.ctx = ctx;
        this.id_airport = id_airport;
        this.id_person = id_person;
        this.id_push = id_push;
    }

    public void saveRegistrationId(){
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
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
