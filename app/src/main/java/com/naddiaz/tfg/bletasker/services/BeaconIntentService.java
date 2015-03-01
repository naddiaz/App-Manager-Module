package com.naddiaz.tfg.bletasker.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by nad on 1/03/15.
 */
public class BeaconIntentService extends IntentService{

    public BeaconIntentService() {
        super("BeaconService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
