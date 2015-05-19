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
import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.utils.CustomRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nad on 12/02/15.
 */
public class WSBeacons {

    private View view;
    private Context ctx;
    private String hash;
    private String mac = "";
    private String rssi = "";
    private String URL;


    public WSBeacons(Context ctx, String hash, String mac, int rssi) {
        this.ctx = ctx;
        this.hash = hash;
        this.mac = mac;
        this.rssi = String.valueOf(rssi);
        this.URL = ctx.getString(R.string.host) +  ctx.getString(R.string.ws_url_save_location);
    }

    public void saveLocation(){
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        Map<String, String>  params = new HashMap<String, String>();
        params.put("hash", this.hash);
        params.put("id_beacon", this.mac);
        params.put("rssi", this.rssi);

        Log.i("WSBeacon","hash: " + this.hash + ", id_beacon: " + this.mac + ", rssi: " + this.rssi);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, this.URL, params,
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
}
