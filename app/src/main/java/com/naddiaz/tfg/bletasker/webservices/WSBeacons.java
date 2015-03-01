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
public class WSBeacons {

    private View view;
    private Context ctx;
    private String id_beacon = "-";
    private String mac = "1";
    private String timestamp = "1";
    private String rssi = "1";
    private String avg_rssi = "-";
    private String distance = "-";

    private static String URL = "http://tfg.naddiaz.com/send/beacons";

    public WSBeacons(View view, String displayName, String deviceAddress, long tsMillis, int rssi, int smoothedRssi, String distance){
        this.view = view;
        this.ctx = this.view.getContext();
        this.id_beacon = displayName;
        this.mac = deviceAddress;
        this.timestamp = String.valueOf(tsMillis);
        this.rssi = String.valueOf(this.rssi);
        this.avg_rssi = String.valueOf(smoothedRssi);
        this.distance = distance;
    }

    public WSBeacons(Context ctx, BluetoothDevice key, int rssi, long timestampNanos) {
        this.ctx = ctx;
        this.mac = key.toString();
        this.timestamp = String.valueOf(timestampNanos);
        this.rssi = String.valueOf(rssi);
    }

    public void postLocation(){
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
                params.put("id_beacon", id_beacon);
                params.put("mac", mac);
                params.put("timestamp", timestamp);
                params.put("rssi", rssi);
                params.put("avg_rssi", avg_rssi);
                params.put("distance", distance);

                return params;
            }
        };
        queue.add(postRequest);
    }
}
