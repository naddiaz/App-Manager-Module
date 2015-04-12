package com.naddiaz.tfg.bletasker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.naddiaz.tfg.bletasker.widget.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nad on 9/04/15.
 */
public class UserPrefecences {

    private static final String TAG = "UserPreferences";
    public static final String PROPERTY_ID_AIRPORT = "id_airport";
    public static final String PROPERTY_ID_PERSON = "id_person";
    public static final String PROPERTY_WORKER_NAME = "worker_name";
    public static final String PROPERTY_HASH = "hash";

    Context ctx;
    private int id_airport;
    private String id_person;
    private String worker_name;
    private String hash;

    public UserPrefecences(Context ctx){
        this.ctx = ctx;
    }

    public UserPrefecences(Context ctx, JSONObject data){
        this.ctx = ctx;
        Log.i(TAG,data.toString());
        try {
            this.id_airport = data.getInt("id_airport");
            this.id_person = data.getString("id_person");
            this.worker_name = data.getString("worker_name");
            this.hash = data.getString("hash");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public UserPrefecences registerGcm(){
        new GcmRegistration(ctx,id_airport,id_person,worker_name).RegisterGcm();
        return this;
    }

    public void savePreferences(){
        final SharedPreferences prefs = getGCMPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PROPERTY_ID_AIRPORT, id_airport);
        editor.putString(PROPERTY_ID_PERSON, id_person);
        editor.putString(PROPERTY_WORKER_NAME, worker_name);
        editor.putString(PROPERTY_HASH, hash);
        editor.commit();
        Intent intent = new Intent(this.ctx,MainActivity.class);
        this.ctx.startActivity(intent);
    }

    public UserPrefecences readPreferences(){
        final SharedPreferences prefs = getGCMPreferences(ctx);
        hash = prefs.getString(PROPERTY_HASH, "");
        if (hash.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return null;
        }

        id_airport = prefs.getInt(PROPERTY_ID_AIRPORT, 0);
        id_person = prefs.getString(PROPERTY_ID_PERSON, "");
        worker_name = prefs.getString(PROPERTY_WORKER_NAME, "");

        return this;
    }

    public void clearPreferences(){
        final SharedPreferences prefs = getGCMPreferences(ctx);
        prefs.edit().clear().commit();
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    public int getId_airport(){
        return this.id_airport;
    }

    public String getId_person(){
        return this.id_person;
    }

    public String getWorker_name(){
        return this.worker_name;
    }

    public String getHash(){
        return this.hash;
    }
}
