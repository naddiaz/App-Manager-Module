package com.naddiaz.tfg.bletasker.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.naddiaz.tfg.bletasker.utilsUriBeacon.ScanRecord;
import com.naddiaz.tfg.bletasker.utilsUriBeacon.ScanResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nad on 1/03/15.
 */
public class BeaconService extends Service {


    private static final String TAG = "UriBeaconScan";

    private static final Handler mHandler = new Handler();
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new LeScanCallback();
    private BluetoothAdapter mBluetoothAdapter;
    private long mScanTime = 10000;
    private HashMap<BluetoothDevice, ScanResult> scanResult;
    private CountDownTimer countDown;

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        scanResult = new HashMap<>();
        scanLeDevice(true,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"on Destroy");
        scanLeDevice(false,true);
        countDown.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable mScan = new Runnable() {
        @Override
        public void run() {
            scanLeDevice(false,false);
        }
    };

    public void sender(){
        if(scanResult != null) {
            for(final Map.Entry<BluetoothDevice, ScanResult> beacon : scanResult.entrySet()){
                new Runnable() {
                    @Override
                    public void run() {
                        //WSBeacons sendBeacons = new WSBeacons(getApplicationContext(), beacon.getKey(), beacon.getValue().getRssi(), beacon.getValue().getTimestampNanos());
                        //sendBeacons.postLocation();
                    }
                }.run();
            }
        }
        scanLeDevice(true,false);
    }

    @SuppressWarnings("deprecation")
    private void scanLeDevice(final boolean enable, boolean kill) {
        if (enable) {
            mHandler.postDelayed(mScan, mScanTime);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else if(!kill){
            // Cancel the scan timeout callback if still active or else it may fire later.
            mHandler.removeCallbacks(mScan);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            countDown = new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Log.i(TAG, "WAIT: " + millisUntilFinished);
                }

                public void onFinish() {
                    sender();
                }
            };
            countDown.start();
        }
        else{
            // Cancel the scan timeout callback if still active or else it may fire later.
            mHandler.removeCallbacks(mScan);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


    /**
     * Callback for LE scan results.
     */
    private class LeScanCallback implements BluetoothAdapter.LeScanCallback {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanBytes) {

            ScanRecord record = ScanRecord.parseFromBytes(scanBytes);
            ScanResult result = new ScanResult(device, record, rssi, SystemClock.elapsedRealtimeNanos());
            if(scanResult.containsKey(device)){
                scanResult.remove(device);
                scanResult.put(device,result);
            }
            else{
                scanResult.put(device,result);
            }

        }
    }
}
