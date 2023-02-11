package org.gullivigne.foursolaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;

public class DevelopperBluetoothControlActivity extends AppCompatActivity {

    private Button btnWrite, btnDisconnect;
    private TextView txtInput;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developper_bluetooth_control);

        btnDisconnect = findViewById(R.id.dev_master_disconnect);
        btnWrite = findViewById(R.id.dev_master_write);
        txtInput = findViewById(R.id.txtDevMasterInput);


        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothArduino.getInstance().getBtConnectedThread().cancel();
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothArduino.getInstance().getBtConnectedThread().write("bonjour");
            }
        });

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == BluetoothArduino.MESSAGE_READ) {
                    String readMsg = null;
                    readMsg = new String((byte[]) msg.obj, 0, msg.arg1);
                    Log.d("DEV_TAG", "Buffer : "+ msg.obj + " - Bytes : " + msg.arg1);
                    txtInput.setText(readMsg);
                }
            }
        };

        BluetoothArduino.getInstance().bindHandler(handler);

    }
}