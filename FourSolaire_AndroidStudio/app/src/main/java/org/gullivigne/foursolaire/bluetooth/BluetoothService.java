package org.gullivigne.foursolaire.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@SuppressLint("MissingPermission")
public final class BluetoothService {

    // Debugging
    private static final String TAG = "BluetoothService";

    // UUID
    private static final UUID MY_UUID = UUID.randomUUID();

    // STATES
    private static final int STATE_NONE = 0,
            STATE_CONNECTING = 1,
            STATE_CONNECTED = 2;

    // MESSAGES
    public static final int MESSAGE_READ = 0,
            MESSAGE_WRITE = 1,
            MESSAGE_CONNECTION_SUCCESS = 2,
            MESSAGE_CONNECTION_STOP = 3,
            MESSAGE_CONNECTION_LOST = 4,
            MESSAGE_CONNECTION_FAIL = 5;


//    private BluetoothAdapter bluetoothAdapter;
    private ClientConnectThread clientConnectThread;
    private ConnectedThread connectedThread;
    private static Handler mHandler;

    private static BluetoothService instance = null;
    private int curState;

//    public BluetoothService(Context context, Handler handler) {
    private BluetoothService(Handler handler) {
//        bluetoothAdapter = context.getSystemService(BluetoothManager.class).getAdapter();
        mHandler = handler;
    }

    public static BluetoothService getInstance(Handler handler) {
        if ( instance == null ) { instance = new BluetoothService(handler); }
        return instance;
    }

    public static BluetoothService getInstance() {
        if (mHandler == null) { return null; }
        return instance;
    }

    /**
     * Bind a new Handler
     */
    public synchronized void setHandler(Handler mHandler) {
        Log.d(TAG, "set new Handler");
        this.mHandler = mHandler;
    }

    /**
     * Start the ClientConnectThread to initiate a connection with the device
     */
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect");
        // Cancel thread attempting to create connection
        if (curState == STATE_CONNECTING) {
            if (clientConnectThread != null) {
                clientConnectThread.cancel();
                clientConnectThread = null;
            }
        }

        // Cancel thread running connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Connect the given device
        clientConnectThread = new ClientConnectThread(device);
        clientConnectThread.start();
    }

    /**
     * Start the ConnectedThread to manage the connection with the device
     */
    private synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        Log.d(TAG, "connected");

        // Cancel the thread that completed connection
        if (clientConnectThread != null) {
            clientConnectThread.cancel();
            clientConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

        mHandler.obtainMessage(MESSAGE_CONNECTION_SUCCESS, device).sendToTarget();
    }

    /**
     * Stop all thread
     */
    public synchronized void stop(){
        Log.d(TAG, "stop");

        if (curState == STATE_CONNECTING) {
            if (clientConnectThread != null) {
                clientConnectThread.cancel();
                clientConnectThread = null;
            }
        }

        // Cancel thread running connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        curState = STATE_NONE;
        mHandler.obtainMessage(MESSAGE_CONNECTION_STOP).sendToTarget();
    }

    /**
     * Warn that the connection is failed
     */
    private void connectionFailed() {
        Log.d(TAG, "fail");
        curState = STATE_NONE;
        mHandler.obtainMessage(MESSAGE_CONNECTION_FAIL).sendToTarget();
    }

    /**
     * Warn that the connection is lost
     */
    private void connectionLost() {
        Log.d(TAG, "lost");
        curState = STATE_NONE;
        mHandler.obtainMessage(MESSAGE_CONNECTION_LOST).sendToTarget();
    }

    /**
     * Write to the ConnectedThread
     * @param input Buffer string to send
     */
    public void write(String input) {
        ConnectedThread tmp;

        synchronized (this) {
            if (curState != STATE_CONNECTED) { return ;}
            tmp = connectedThread;
        }

        tmp.write(input);
    }

    // THREADS

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ClientConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ClientConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmpSocket = null;

            try {
                tmpSocket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            } catch (IOException e) {
                Log.e(TAG, "Socket create() failed", e);
            }

            mmSocket = tmpSocket;
            curState = STATE_CONNECTING;
        }

        public void run() {
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    Log.e(TAG, "unable to close() socket during connection failure", e1);
                }
                connectionFailed();
                return;
            }

            synchronized (BluetoothService.this) {
                clientConnectThread = null;
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket) {
            this.socket = socket;
            InputStream tmpInputStream = null;
            OutputStream tmpOutputStream = null;

            try {
                tmpInputStream = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }

            try {
                tmpOutputStream = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            inputStream = tmpInputStream;
            outputStream = tmpOutputStream;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes = 0;

            while (curState == STATE_CONNECTED) {
                try {
                    if (inputStream.available() > 0) {
//                    Log.d(DEBUG_TAG, "Buffer size [" + String.valueOf(btInStream.available()) + "] ?" + String.valueOf(btInStream.available() == 0));
                        byte tmpByte = (byte) inputStream.read();
//                    Log.d(DEBUG_TAG, "Buffer size [" + String.valueOf(btInStream.available()) + "] char = " + (char) tmpByte + String.valueOf(btInStream.available() == 0));
                        if (tmpByte == '\n') {
//                        Log.d(DEBUG_TAG, "Buffer full received" + String.valueOf(cnt));
                            mHandler.obtainMessage(MESSAGE_READ, new String(buffer, 0, bytes)).sendToTarget();
                            bytes = 0;
                            buffer = new byte[1024];
                        } else {
                            buffer[bytes] = tmpByte;
                            bytes++;
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        public void write(String input) {
            byte[] buffer = input.getBytes();
            try {
                outputStream.write(buffer);
                mHandler.obtainMessage(MESSAGE_WRITE, buffer).sendToTarget(); // Todo : Terminal MSG ID ?
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
