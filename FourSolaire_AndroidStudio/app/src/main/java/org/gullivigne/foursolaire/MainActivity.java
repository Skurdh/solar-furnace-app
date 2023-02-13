package org.gullivigne.foursolaire;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.gullivigne.foursolaire.bluetooth.BluetoothService;
import org.gullivigne.foursolaire.bluetooth.paired_device.PairedDevice;
import org.gullivigne.foursolaire.bluetooth.paired_device.PairedDevicesAdapter;
import org.gullivigne.foursolaire.database.DatabaseManager;
import org.gullivigne.foursolaire.database.DeviceInfoData;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    public static final int MESSAGE_TRY_CONNECT = 0, MESSAGE_CONNECTING = 1;
    private static final int REQUEST_BLUETOOTH_CODE = 1;

    private Group grpBluetoothConfiguration, grpPairedDevices;
    private TextView txtBluetoothPermissions, txtPairedDevicesVoid;
    private Button btnConfiguration;
    private ImageView imgAndroid;

    private RecyclerView recyclerPairedDevices;
    private PairedDevicesAdapter adapterPairedDevices;
    private Handler handler;
    private AlertDialog connectingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(getColor(R.color.main_color));

        // Create Handler
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("MissingPermission")
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == BluetoothService.MESSAGE_CONNECTION_SUCCESS) {
                    Log.d("MainActivity", "SUCCESS");
                } else if (msg.what == BluetoothService.MESSAGE_CONNECTION_FAIL) {
                    Log.d("MainActivity", "FAIL");
                } else if (msg.what == BluetoothService.MESSAGE_CONNECTION_LOST) {

                }


//                switch (msg.what) {
////                    case MESSAGE_TRY_CONNECT:
////                        BluetoothArduino bluetooth = BluetoothArduino.getInstance(MainActivity.this);
////                        BluetoothDevice device = (BluetoothDevice) msg.obj;
////                        bluetooth.setArduinoDevice(device);
////                        bluetooth.setArduinoUUID(device.getUuids()[0].getUuid());
////
////                        bluetooth.startConnection(handler);
////                        break;
////                    case MESSAGE_CONNECTING:
////                        if ((boolean) msg.obj) {
////                            Log.d("MainActivity", "CONNECTED");
////                        } else {
////                            Toast.makeText(MainActivity.this, R.string.config_bt_connection_failure, Toast.LENGTH_SHORT).show();
////                            connectingDialog.dismiss();
////                        }
////                        break;
//                    case BluetoothService.MESSAGE_READ:
//                }
            }
        };

        // Init Singletons
        DatabaseManager.getInstance(this);
        BluetoothService.getInstance(handler);

        // Init views
        initViewGlobalButton();
        initConnectingPopup();
        initViewBluetoothPermissions();
        initViewPairedDevices();

        checkBluetoothHealth();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkBluetoothHealth();
            } else {
                showPermissionsMsg();
            }
        }
    }

    // Interface

    /**
     * Init global interface view
     */
    private void initViewGlobalButton() {
        btnConfiguration = findViewById(R.id.btnConfiguration);
        btnConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = btnConfiguration.getText().toString();

                // Button Not Supported
                if (btnText.equals(getResources().getString(R.string.main_bt_not_supported_button))) {
                    finish();
                }

                // Button Permission Not Allowed
                else if (btnText.equals(getString(R.string.main_bt_permissions_button_p1))) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    btnConfiguration.setText(getString(R.string.main_bt_permissions_button_p2));
                }

                // Button Check Permission Allowed
                else if (btnText.equals(getString(R.string.main_bt_permissions_button_p2))) {
                    if (Build.VERSION.SDK_INT > 30 &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        checkBluetoothHealth();
                    } else {
                        Toast toast = Toast.makeText(MainActivity.this, R.string.main_bt_permissions_toast, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                        toast.show();
                        btnConfiguration.setText(getString(R.string.main_bt_permissions_button_p1));
                    }
                }

                // Button Not Enabled
                else if (btnText.equals(getString(R.string.main_bt_not_enabled_button))) {
                    if (checkBluetoothEnabled()) {
                        checkBluetoothHealth();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.main_bt_not_enabled_toast, Toast.LENGTH_SHORT).show();
                    }
                }

                // Button Refresh Paired Devices
                else if (btnText.equals(getString(R.string.main_devices_button))) {

                }
            }
        });
    }

    /**
     * Init Bluetooth configuration view
     */
    private void initViewBluetoothPermissions() {
        grpBluetoothConfiguration = findViewById(R.id.grpBluetoothConfiguration);
        imgAndroid = findViewById(R.id.imgAndroidBluetooth);
        txtBluetoothPermissions = findViewById(R.id.txtInfoBluetoothPermissions);
    }

    /**
     * Init paired devices list view
     */
    private void initViewPairedDevices() {
        grpPairedDevices = findViewById(R.id.grpPairedDevices);
        txtPairedDevicesVoid = findViewById(R.id.txtPairedDevicesVoid);
        recyclerPairedDevices = findViewById(R.id.recycleViewPairedDevices);
        adapterPairedDevices = new PairedDevicesAdapter(handler, connectingDialog);
        recyclerPairedDevices.setAdapter(adapterPairedDevices);
        recyclerPairedDevices.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Show Bluetooth configuration interface and hide all others
     */
    private void showBluetoothGroup () {
        grpPairedDevices.setVisibility(View.GONE);
        imgAndroid.setVisibility(View.VISIBLE);
        grpBluetoothConfiguration.setVisibility(View.VISIBLE);
    }

    /**
     *
     */
    private void showBluetoothPermissionsGroup() {
        grpBluetoothConfiguration.setVisibility(View.GONE);
        grpPairedDevices.setVisibility(View.GONE);
        imgAndroid.setVisibility(View.VISIBLE);
    }

    /**
     * Show help interface for Bluetooth not supported.
     */
    private void showBtNotSupportedMsg() {
        setTitle(R.string.main_bt_not_supported_title);
        showBluetoothGroup();
        txtBluetoothPermissions.setText(R.string.main_bt_not_supported_help);
        btnConfiguration.setText(R.string.config_bt_not_supported_btn);
    }

    /**
     *
     */
    private void showPermissionRequest() {
        setTitle(R.string.main_bt_permissions_title);
        showBluetoothPermissionsGroup();
        btnConfiguration.setVisibility(View.INVISIBLE);
    }

    /**
     *
     */
    private void showPermissionsMsg() {
        setTitle(R.string.main_bt_permissions_title);
        showBluetoothGroup();
        txtBluetoothPermissions.setText(R.string.main_bt_permissions_help);
        btnConfiguration.setVisibility(View.VISIBLE);
        btnConfiguration.setText(R.string.main_bt_permissions_button_p1);
    }

    /**
     * Show Paired devices list interface and hide all others
     */
    private void showDevicePaired() {
        grpBluetoothConfiguration.setVisibility(View.GONE);
        imgAndroid.setVisibility(View.GONE);
        grpPairedDevices.setVisibility(View.VISIBLE);

        setTitle(R.string.main_devices_title);
        btnConfiguration.setText(R.string.main_devices_button);
    }


    /**
     * Open help popup for Bluetooth Connection permission
     */
    private void openPopupPermissionExplanations() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.main_bt_permissions_popup_title))
                .setMessage(getString(R.string.main_bt_permissions_popup_help))
                .setPositiveButton(getString(R.string.main_bt_permissions_positive_button), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.S)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CODE);
                    }
                })
                .setCancelable(false);

        builder.create().show();
    }

    /**
     * Show help interface for Bluetooth not enabled.
     */
    private void showBtNotEnabledMsg() {
        setTitle(R.string.main_bt_not_enabled_title);
        showBluetoothGroup();
        txtBluetoothPermissions.setText(R.string.main_bt_not_enabled_help);
        btnConfiguration.setVisibility(View.VISIBLE);
        btnConfiguration.setText(R.string.main_bt_not_enabled_button);
    }

    /**
     * Todo
     */
    private void initConnectingPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View connectingView = getLayoutInflater().inflate(R.layout.popup_device_connecting_progress, null);
        builder.setView(connectingView);
        connectingDialog = builder.create();
        connectingDialog.setCancelable(false);
        connectingDialog.setCanceledOnTouchOutside(false);
    }

    // Utils

    /**
     * TODO : Complete definition
     */
    private void openPopupDeviceConnection() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        final View deviceConnectionView = getLayoutInflater().inflate(R.layout.popup_device_connection, null);

        // Init group view
        Group grpSetName = deviceConnectionView.findViewById(R.id.grpSetName),
                grpChooseFile = deviceConnectionView.findViewById(R.id.grpChooseFile),
                grpConnecting = deviceConnectionView.findViewById(R.id.grpConnection);

        // Init set name group
        EditText editTxtDeviceNickname = deviceConnectionView.findViewById(R.id.editTxtDeviceNickame);
        Button btnSetName = deviceConnectionView.findViewById(R.id.btnSetName);

        // Config set name group
        editTxtDeviceNickname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (editTxtDeviceNickname.getText().toString().equals("")) {
                    btnSetName.setText("Ignorer");
                } else {
                    btnSetName.setText("Suivant");
                }
                return true;
            }
        });
        editTxtDeviceNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTxtDeviceNickname.getText().toString().equals("")) {
                    btnSetName.setText("Ignorer");
                } else {
                    btnSetName.setText("Suivant");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        btnSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnSetName.getText().toString().equals("Ignorer")) {

                } else {

                }
            }
        });


        // Show Dialog
        builder.setView(deviceConnectionView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     *
     * @return
     */
    private boolean checkBluetoothEnabled() {
        if (!BluetoothArduino.getInstance(this).getBluetoothAdapter().isEnabled()) {
            return false;
        }
        return true;
    }

    /**
     *
     */
    private void checkBluetoothHealth() {
        // Check Bluetooth Supported
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) || BluetoothArduino.getInstance(this).getBluetoothAdapter() == null) {
            showBtNotSupportedMsg();
        }
        // Check Bluetooth Permissions and request them
        else if (Build.VERSION.SDK_INT > 30 &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            showPermissionRequest();
            openPopupPermissionExplanations();
        }
        // Check Bluetooth is activated
        else if (!checkBluetoothEnabled()) {
            showBtNotEnabledMsg();
        }
        // Bluetooth OK
        else {
            showDevicePaired();
            populatePairedDevicesRecycler();
        }
    }

    /**
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    private ArrayList<PairedDevice> getPairedDevices() {
        ArrayList<PairedDevice> devices = new ArrayList<>();
        int i = 0;
        Set<BluetoothDevice> pairedDevices = BluetoothArduino.getInstance(this).getBluetoothAdapter().getBondedDevices();
        for (BluetoothDevice pairedDevice : pairedDevices) {
            devices.add(new PairedDevice(i, pairedDevice));
            i++;
        }

        return devices;
    }

    /**
     *
     */
    private void populatePairedDevicesRecycler() {
        ArrayList<PairedDevice> pairedDevices = getPairedDevices();
        ArrayList<DeviceInfoData> devicesInfo = DatabaseManager.getInstance(this).getDevicesInfo();

        if (pairedDevices.isEmpty()) {
            txtPairedDevicesVoid.setVisibility(View.VISIBLE);
            return;
        }

        // Populate paired device with local data
        for (PairedDevice device : pairedDevices) {
            if (devicesInfo != null) {
                for (DeviceInfoData info : devicesInfo) {
                    if (device.getAddress().equals(info.getAddress())) {
                        device.setAlias(info.getAlias());
                        device.setAssociated_files(info.getFiles_name());
                    }
                }
            }
            adapterPairedDevices.addPairedDevice(device);
        }
    }
}
