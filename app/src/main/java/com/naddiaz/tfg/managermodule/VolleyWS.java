package com.naddiaz.tfg.managermodule;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nad on 5/02/15.
 */
public class VolleyWS implements View.OnClickListener {

    View rootView;
    private String[] params = {"year","month","day","hour","minute","second"};

    public VolleyWS(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void onClick(View v) {

        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        String url ="http://tfg.naddiaz.com:3000/ws";

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for (int i = 0; i < params.length; i++) {
                            int id = rootView.getResources().getIdentifier("txt_"+params[i],"id",rootView.getContext().getPackageName());
                            TextView txt = (TextView) rootView.findViewById(id);
                            try {
                                txt.setText(response.getString(params[i]));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

// Add the request to the RequestQueue.
        queue.add(req);
    }
}
