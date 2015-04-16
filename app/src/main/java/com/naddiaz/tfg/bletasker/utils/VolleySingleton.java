package com.naddiaz.tfg.bletasker.utils;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by nad on 16/04/15.
 */

public class VolleySingleton {

    private static VolleySingleton mVolleySingleton = null; // Singleton object
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context) {
        if(mVolleySingleton == null) {
            mVolleySingleton = new VolleySingleton(context);
        }
        return mVolleySingleton;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}