package org.gullivigne.foursolaire;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BTClientConnectThread extends Thread {
    private static final String DEBUG_TAG = "BTClientConnectThread";
    private final BluetoothSocket btSocket;
    private final Context mContext;

    @SuppressLint("MissingPermission")
    public BTClientConnectThread(BluetoothDevice device, UUID APP_UUID, Context context) {
        mContext = context;
        BluetoothSocket tmpBtSocket = null;

        try {
            tmpBtSocket = device.createRfcommSocketToServiceRecord(APP_UUID);
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Socket's create() method failed", e);
        }
        btSocket = tmpBtSocket;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        try {
            btSocket.connect();
        } catch (IOException connectException) {
            try {
                btSocket.close();
            } catch (IOException closeException) {
                Log.e(DEBUG_TAG, "Could not close the client socket", closeException);
            }
            return;
        }

    }

    public void cancel() {
        try {
            btSocket.close();
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Could not close the client socket", e);
        }
    }

    public BluetoothSocket getBtSocket() {
        return btSocket;
    }
}
