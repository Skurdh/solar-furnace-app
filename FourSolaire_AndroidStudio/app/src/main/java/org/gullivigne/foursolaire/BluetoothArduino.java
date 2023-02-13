package org.gullivigne.foursolaire;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.UUID;

public class BluetoothArduino {


    private static final int ERROR_READ = 0;
    private static final String DEBUG_TAG = "BTArduino";
    public static final int MESSAGE_READ = 1, MESSAGE_REQUEST = 2;


    private static BluetoothArduino instance = null;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private UUID arduinoUUID;
    private BluetoothDevice arduinoDevice;
    private BTConnectedThread btConnectedThread = null;
    private Context mContext;

    private BluetoothArduino(Context context) {
        mContext = context;
        bluetoothManager = context.getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        arduinoUUID = UUID.randomUUID();
    }

    public static BluetoothArduino getInstance(Context context) {
        if (instance == null) {
            instance = new BluetoothArduino(context);
        }
        if (context != instance.mContext) {
            instance.mContext = context;
        }
        return instance;
    }

    public static BluetoothArduino getInstance() {
        if (instance == null) {
            Log.e(DEBUG_TAG, "There is no instance of BluetoothArduino");
            return null;
        }
        return instance;
    }

    public void startConnection(Handler handler) {
        BTClientConnectThread connectThread = new BTClientConnectThread(arduinoDevice, arduinoUUID, mContext);
        Looper.prepare();
        Handler clientHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0) {

                }
            }
        };
        connectThread.start();



        if (connectThread.getBtSocket().isConnected()) {
            btConnectedThread = new BTConnectedThread(connectThread.getBtSocket(), null);
            btConnectedThread.start();
            handler.obtainMessage(MainActivity.MESSAGE_CONNECTING, true).sendToTarget();
        } else {
            connectThread.cancel();
            connectThread.interrupt();
            handler.obtainMessage(MainActivity.MESSAGE_CONNECTING, false).sendToTarget();
        }
    }

    public void bindHandler(Handler handler) {
        btConnectedThread.setmHandler(handler);
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public UUID getArduinoUUID() {
        return arduinoUUID;
    }

    public void setArduinoUUID(UUID arduinoUUID) {
        this.arduinoUUID = arduinoUUID;
    }

    public BluetoothDevice getArduinoDevice() {
        return arduinoDevice;
    }

    public void setArduinoDevice(BluetoothDevice arduinoDevice) {
        this.arduinoDevice = arduinoDevice;
    }

    public BTConnectedThread getBtConnectedThread() {
        return btConnectedThread;
    }
}
