package com.naddiaz.tfg.bletasker.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.utilsUriBeacon.ScanRecord;
import com.naddiaz.tfg.bletasker.utilsUriBeacon.ScanResult;
import com.naddiaz.tfg.bletasker.webservices.WSBeacons;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by nad on 19/05/15.
 */
public class BeaconManager extends Service{

    private static final String TAG = BeaconManager.class.getSimpleName();

    private static PendingIntent pendingIntent;
    private static AlarmManager manager;
    private static boolean alarmCanceled = false;

    private static long scanPeriod = 5000l;
    private static long scanBetween = 25000l;
    private static long sendTime = 30000l;

    private HashMap<BluetoothDevice, ScanResult> scanResult;
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new LeScanCallback();
    private BluetoothAdapter mBluetoothAdapter;

    private static final Handler mHandler = new Handler();

    private UserPrefecences userPrefecences;

    Runnable mScanPeriod = new Runnable() {
        public void run() {
            Log.i(TAG,"mScanPeriod Runnable");
            stopLeScan();
            new CountDownTimer(scanBetween,5000){

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i(TAG,"TICK : " + millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    if(!new UserPrefecences(getApplication()).readPreferences().getBeaconManagerState()) {
                        startLeScan();
                    }
                    else{
                        mHandler.removeCallbacks(mScanPeriod);
                        mHandler.removeCallbacks(mSendTime);
                        stopSelf();
                    }
                }
            }.start();
        }
    };
    Runnable mSendTime = new Runnable() {
        @Override
        public void run() {
            runTask();
        }
    };

    @SuppressWarnings("deprecation")
    private void startLeScan(){
        Log.wtf(TAG,"startLeScan");
        mHandler.postDelayed(mScanPeriod, scanPeriod);
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    @SuppressWarnings("deprecation")
    private void stopLeScan(){
        Log.wtf(TAG,"stopLeScan");
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        scanResult = new HashMap<>();
        startLeScan();
        mHandler.postDelayed(mSendTime, sendTime);
        userPrefecences = new UserPrefecences(getApplication()).readPreferences();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Callback for LE scan results.
     */
    private class LeScanCallback implements BluetoothAdapter.LeScanCallback {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanBytes) {

            ScanRecord record = ScanRecord.parseFromBytes(scanBytes);
            ScanResult result = new ScanResult(device, record, rssi, SystemClock.elapsedRealtimeNanos());
            scanResult.put(device,result);
        }
    }

    private void runTask() {
        Log.i(TAG,"runTask");
        mHandler.postDelayed(mSendTime, sendTime);
        if(scanResult != null) {
            for(final Map.Entry<BluetoothDevice, ScanResult> beacon : scanResult.entrySet()){
                new Runnable() {
                    @Override
                    public void run() {
                        new WSBeacons(getApplicationContext(),
                                userPrefecences.getHash(),
                                beacon.getKey().getAddress(),
                                beacon.getValue().getRssi()
                        ).saveLocation();
                    }
                }.run();
            }
            scanResult.clear();
        }
    }
}
