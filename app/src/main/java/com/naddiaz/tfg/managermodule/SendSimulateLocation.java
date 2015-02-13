package com.naddiaz.tfg.managermodule;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nad on 12/02/15.
 */
public class SendSimulateLocation {

    private View view;
    private String id_person = "1";
    private String id_beacon = "1";

    public SendSimulateLocation(View rootView){
        this.view = rootView;
        EditText ed_person = (EditText) view.findViewById(R.id.idPerson);
        id_person = ed_person.getText().toString();
        EditText ed_beacon = (EditText) view.findViewById(R.id.idBeacon);
        id_beacon = ed_beacon.getText().toString();
    }

    public void postLocation(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        String url ="http://tfg.naddiaz.com/receiveLocation";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
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
                params.put("id_person", id_person);
                params.put("id_beacon", id_beacon);

                return params;
            }
        };
        queue.add(postRequest);
    }
}
