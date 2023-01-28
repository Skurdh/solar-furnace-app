package org.gullivigne.foursolaire;

import android.bluetooth.BluetoothSocket;
import android.icu.util.Output;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BTConnectedThread extends Thread {

    private static final String DEBUG_TAG = "BTConnectedThread";
    private final BluetoothSocket btSocket;
    private final InputStream btInStream;
    private final OutputStream btOutStream;
    private Handler mHandler;

    public BTConnectedThread(BluetoothSocket socket, Handler handler) {
        mHandler = handler;
        btSocket = socket;
        InputStream tmpInStream = null;
        OutputStream tmpOutStream = null;

        try {
            tmpInStream = socket.getInputStream();
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOutStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Error occurred when creating output stream", e);
        }

        btInStream = tmpInStream;
        btOutStream = tmpOutStream;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                if (btInStream.available() > 0) {
                    byte tmpByte = (byte) btInStream.read();
                    if (tmpByte == '\n') {
                        mHandler.obtainMessage(BluetoothArduino.MESSAGE_READ, new String(buffer, 0, bytes)).sendToTarget();
                        bytes = 0;
                        buffer = new byte[1024];
                    } else {
                        buffer[bytes] = tmpByte;
                        bytes++;
                    }
                }
                
                

                bytes = btInStream.read(buffer);
                if (bytes > 0 && mHandler != null) {
                    SystemClock.sleep(100);
                    mHandler.obtainMessage(BluetoothArduino.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                }// Send the obtained bytes to the UI activity
            } catch (IOException e) {
                Log.d(DEBUG_TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    public void write(String input) {
        byte[] bytes = input.getBytes(); //converts entered String into bytes
        try {
            btOutStream.write(bytes);
        } catch (IOException e) {
            Log.e("Send Error","Unable to send message",e);
        }
    }

    public void cancel() {
        try {
            btSocket.close();
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Could not close the connect socket", e);
        }
    }

    public InputStream getBtInStream() {
        return btInStream;
    }

    public OutputStream getBtOutStream() {
        return btOutStream;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }
}
