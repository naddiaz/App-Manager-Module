package com.naddiaz.tfg.bletasker.services;

import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.naddiaz.tfg.bletasker.utils.ScanRecord;
import com.naddiaz.tfg.bletasker.utils.ScanResult;
import com.naddiaz.tfg.bletasker.webservices.WSBeacons;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        scanResult = new HashMap<>();
        scanLeDevice(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable mScan = new Runnable() {
        @Override
        public void run() {
            scanLeDevice(false);
        }
    };

    public void sender(){
        if(scanResult != null) {
            for(final Map.Entry<BluetoothDevice, ScanResult> beacon : scanResult.entrySet()){
                new Runnable() {
                    @Override
                    public void run() {
                        WSBeacons sendBeacons = new WSBeacons(getApplicationContext(), beacon.getKey(), beacon.getValue().getRssi(), beacon.getValue().getTimestampNanos());
                        sendBeacons.postLocation();
                    }
                }.run();
            }
        }
        scanLeDevice(true);
    }

    @SuppressWarnings("deprecation")
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(mScan, mScanTime);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            // Cancel the scan timeout callback if still active or else it may fire later.
            mHandler.removeCallbacks(mScan);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            new CountDownTimer(20000, 10000) {

                public void onTick(long millisUntilFinished) {
                    Log.i(TAG, "WAIT: " + millisUntilFinished);
                }

                public void onFinish() {
                    sender();
                }
            }.start();
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
