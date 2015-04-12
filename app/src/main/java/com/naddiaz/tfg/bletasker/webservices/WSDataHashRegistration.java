package com.naddiaz.tfg.bletasker.webservices;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.utils.CustomRequest;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.widget.MainActivity;
import com.naddiaz.tfg.bletasker.widget.RestoreUserActivity;
import com.naddiaz.tfg.bletasker.widget.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nad on 12/02/15.
 */
public class WSDataHashRegistration {


    private static String URLcheckRegistration;
    private static String URLverify;
    Context ctx;
    String hash;

    public WSDataHashRegistration(Context ctx, String hash){
        this.ctx = ctx;
        this.hash = hash;
        this.URLcheckRegistration = ctx.getString(R.string.ws_url_data_hash_registration);
        this.URLverify = ctx.getString(R.string.ws_url_data_hash_verify);
    }

    public void checkRegisterHash(){
        RequestQueue queue = Volley.newRequestQueue(ctx);

        Map<String, String>  params = new HashMap<String, String>();
        params.put("hash", hash);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URLcheckRegistration, params,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("status")){
                    Toast.makeText(ctx,ctx.getString(R.string.ws_error_hash_false),Toast.LENGTH_SHORT).show();
                }
                else{
                    UserPrefecences userPrefecences = new UserPrefecences(ctx, response);
                    userPrefecences.registerGcm().savePreferences();
                    new WSLoadWorks(ctx,userPrefecences.getHash()).getWorks();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx,ctx.getString(R.string.ws_error_timeout_hash),Toast.LENGTH_LONG).show();
                Log.d("Error.Response", String.valueOf(error));
            }
        });
        queue.add(jsObjRequest);
    }

    public void verifySessionHash(){
        RequestQueue queue = Volley.newRequestQueue(ctx);

        Map<String, String>  params = new HashMap<String, String>();
        params.put("hash", hash);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URLverify, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("status")){
                            try {
                                if(response.getString("status") == "false") {
                                    Toast.makeText(ctx, ctx.getString(R.string.ws_error_verify_hash), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ctx,RestoreUserActivity.class);
                                    ctx.startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(ctx,MainActivity.class);
                                    ctx.startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx,ctx.getString(R.string.ws_error_timeout_hash),Toast.LENGTH_LONG).show();
                Log.d("Error.Response", String.valueOf(error));
            }
        });
        queue.add(jsObjRequest);
    }
}
