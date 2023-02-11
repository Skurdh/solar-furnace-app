package org.gullivigne.foursolaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.gullivigne.foursolaire.dev.DeveloperBluetoothControlActivity;
import org.gullivigne.foursolaire.dev.SaveManager;

import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final int NOT_SUPPORTED = 0, NOT_ENABLED = 1, PERMISSION_NOT_ALLOWED = 2, ENABLED = 3, AUTO_CONNECTION = 4;
    private static final int REQUEST_BLUETOOTH_CODE = 1;

    private TextView txtConfigBtError;
    private Button btnConfigBtError;
    private RelativeLayout layoutBtError;
    private ConstraintLayout layoutBtPairedDevices;
    private RecyclerView pairedDevicesRecyclerView;
    private PairedDeviceRecyclerViewAdapter adapter;

    private BluetoothArduino bluetooth;


    private int current_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        SaveManager.getInstance(this);

        bluetooth = BluetoothArduino.getInstance(this);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) || bluetooth.getBluetoothAdapter() == null) {
            layoutBtError.setVisibility(View.VISIBLE);
            current_state = NOT_SUPPORTED;
            txtConfigBtError.setText(R.string.config_bt_not_supported_info);
            btnConfigBtError.setText(R.string.config_bt_not_supported_btn);
        } else if (!checkBluetoothEnabled()) {
            layoutBtError.setVisibility(View.VISIBLE);
            current_state = NOT_ENABLED;
            txtConfigBtError.setText(R.string.config_bt_not_enabled_info);
            btnConfigBtError.setText(R.string.config_bt_not_enabled_btn);
        } else {
            startConfiguration();
        }
    }

    private void initView() {
        setTitle("Configuration");
        layoutBtError = findViewById(R.id.layoutConfigBTError);
        layoutBtPairedDevices = findViewById(R.id.layoutConfigBTPairedDevice);

        txtConfigBtError = findViewById(R.id.txtConfigBTError);

        btnConfigBtError = findViewById(R.id.btnConfigBTError);
        btnConfigBtError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (current_state) {
                    case NOT_SUPPORTED:
                        finish();
                        break;
                    case NOT_ENABLED:
                        if (checkBluetoothEnabled()) {
                            layoutBtError.setVisibility(View.GONE);
                            startConfiguration();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.config_bt_not_supported_toast1, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case PERMISSION_NOT_ALLOWED:
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            requestPermission(Manifest.permission.BLUETOOTH_CONNECT, REQUEST_BLUETOOTH_CODE);
                        } else {
                            Toast.makeText(MainActivity.this, "Permission accordée !", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        adapter = new PairedDeviceRecyclerViewAdapter(this);
        pairedDevicesRecyclerView = findViewById(R.id.recycleViewPairedDevices);
        pairedDevicesRecyclerView.setAdapter(adapter);
        pairedDevicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private boolean checkBluetoothEnabled() {
        if (!bluetooth.getBluetoothAdapter().isEnabled()) {
            return false;
        }
        return true;
    }

    private void startConfiguration() {
        // TODO: SCAN / AUTO CONNECT
        if (Build.VERSION.SDK_INT > 30 && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            showExplanation(MainActivity.this.getResources().getString(R.string.config_bt_permission_bt_connect_title), MainActivity.this.getResources().getString(R.string.config_bt_permission_bt_connect_text), Manifest.permission.READ_PHONE_STATE, REQUEST_BLUETOOTH_CODE);
        } else {
            current_state = ENABLED;
            layoutBtPairedDevices.setVisibility(View.VISIBLE);
//            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            Set<BluetoothDevice> pairedDevices = bluetooth.getBluetoothAdapter().getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    adapter.addPairedDevice(device);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_BLUETOOTH_CODE:
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startConfiguration();
                } else {
                    layoutBtError.setVisibility(View.VISIBLE);
                    current_state = PERMISSION_NOT_ALLOWED;
                    txtConfigBtError.setText(R.string.config_bt_not_allowed_info);
                    btnConfigBtError.setText(R.string.config_bt_not_allowed_btn);
                }
        }
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission(android.Manifest.permission.BLUETOOTH_CONNECT, REQUEST_BLUETOOTH_CODE);
                    }
                }).setCancelable(false);
                builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
}
