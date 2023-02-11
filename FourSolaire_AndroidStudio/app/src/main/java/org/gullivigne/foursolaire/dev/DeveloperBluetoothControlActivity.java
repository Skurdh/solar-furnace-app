package org.gullivigne.foursolaire.dev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.gullivigne.foursolaire.BluetoothArduino;
import org.gullivigne.foursolaire.R;
import org.gullivigne.foursolaire.dev.controler.Controller;
import org.gullivigne.foursolaire.dev.controler.ControlerRecyclerViewAdapter;
import org.gullivigne.foursolaire.dev.monitor.ActiveReceiverMonitor;
import org.gullivigne.foursolaire.dev.monitor.MonitorRecyclerViewAdapater;
import org.gullivigne.foursolaire.dev.monitor.ReceiverMonitor;
import org.gullivigne.foursolaire.dev.monitor.PassiveReceiverMonitor;
import org.gullivigne.foursolaire.dev.terminal.TerminalMessage;
import org.gullivigne.foursolaire.dev.terminal.TerminalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DeveloperBluetoothControlActivity extends AppCompatActivity {

    // Mode
    private Switch switchNL, switchCR;

    // Terminal
    private RecyclerView terminalRecyclerView;
    private TerminalRecyclerViewAdapter adapterTerminal;
    private Button btnTerminalClear;
    private ImageView btnTerminalSend, btnTerminalOptions;
    private EditText editTerminalMessage;
    private Handler handler;



    // Monitor
    private RecyclerView monitorRecyclerView;
    private MonitorRecyclerViewAdapater adapterMonitor;

    // Controler
    private RecyclerView controlerRecyclerView;
    private ControlerRecyclerViewAdapter adapterControler;

    // Settings
    private boolean setting_autoscroll = true, setting_hide_consumed = false, setting_hide_request = false;
//    private boolean setting_nl = true;
//    private boolean setting_cr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developper_bluetooth_control);

        SaveManager.getInstance(this);

        // Handler
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == BluetoothArduino.MESSAGE_READ) {
                    String inMessage = null;
                    inMessage = (String) msg.obj;

                    boolean isConsumed = false;
                    if (adapterMonitor.getItemCount() > 0) {
                        int i = 0;
                        for (ReceiverMonitor receiver : adapterMonitor.getReceives()) {
                            if (inMessage.startsWith(receiver.getCommand())) {
                                receiver.setValue(inMessage.substring(receiver.getCommand().length()));
                                if (receiver.getMode() == ReceiverMonitor.MODE_WAIT) {
                                    receiver.getTimer().onFinish();
                                } else {
                                    receiver.getTimer().start();
                                }
                                isConsumed = true;
                                break;
                            }
                            i++;
                        }
                    }

                    Log.d("DEV_PART", getCurrentTime() + " : " + inMessage + " - " + String.valueOf(isConsumed));
                    TerminalMessage newTerminalMsg = new TerminalMessage(getCurrentTime(), inMessage, TerminalMessage.ARDUINO, isConsumed ? TerminalMessage.TYPE_CONSUMED : TerminalMessage.TYPE_NORMAL);
                    adapterTerminal.addMessage(newTerminalMsg);
                    if (setting_autoscroll) {
                        terminalRecyclerView.scrollToPosition(adapterTerminal.getItemCount() - 1);
                    }
                } else if (msg.what == BluetoothArduino.MESSAGE_REQUEST) {
                    writeMessage((String) msg.obj, TerminalMessage.TYPE_REQUEST);
                }
            }
        };
        BluetoothArduino.getInstance().bindHandler(handler);
        initView();

        btnTerminalClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterTerminal.clearMessages();
            }
        });

        btnTerminalSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String outMessage = editTerminalMessage.getText().toString();
                writeMessage(outMessage, TerminalMessage.TYPE_NORMAL);
                editTerminalMessage.setText("");
            }
        });

        btnTerminalOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewSettingsDialog();
            }
        });
    }

    private void initView() {
        // MODE
        switchNL = findViewById(R.id.switchModeNewLine);
        switchCR = findViewById(R.id.switchModeReturnCarriage);
        ImageView btnInfo = findViewById(R.id.btnModeInfo);

        switchNL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        switchCR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DeveloperBluetoothControlActivity.this).setTitle("Explications").setMessage(Html.fromHtml(getResources().getString(R.string.dev_mode_explication))).setPositiveButton("Compris !", null).create().show();
            }
        });

        // TERMINAL
        terminalRecyclerView = findViewById(R.id.recyclerViewTerminal);
        adapterTerminal = new TerminalRecyclerViewAdapter(this);
        terminalRecyclerView.setAdapter(adapterTerminal);
        terminalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnTerminalClear = findViewById(R.id.btnTerminalClear);
        btnTerminalSend = findViewById(R.id.btnTerminalSend);
        editTerminalMessage = findViewById(R.id.editTxtTerminalMsg);
        btnTerminalOptions = findViewById(R.id.btnTerminalOptions);


        // MONITOR
        monitorRecyclerView = findViewById(R.id.recyclerViewMonitor);
        adapterMonitor = new MonitorRecyclerViewAdapater(this, handler);
        monitorRecyclerView.setAdapter(adapterMonitor);
        LinearLayoutManager lLM = new LinearLayoutManager(this);
        lLM.setOrientation(LinearLayoutManager.HORIZONTAL);
        monitorRecyclerView.setLayoutManager(lLM);
        ImageView btnCreateReceiver = findViewById(R.id.btnCreateReceiver);
        btnCreateReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddReceiverDialog();
            }
        });

        ArrayList<PassiveReceiverMonitor> receivers = SaveManager.getInstance(this).getPassiveReceivers();
        if (receivers != null) {
            for (PassiveReceiverMonitor receiver : receivers) {
                Log.d("DevActivity", receiver.getClass().toString());
                adapterMonitor.addReceiver(receiver);
            }
        }

        //CONTROLER
        controlerRecyclerView = findViewById(R.id.recyclerViewControl);
        adapterControler = new ControlerRecyclerViewAdapter(this, handler);
        controlerRecyclerView.setAdapter(adapterControler);
        controlerRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        for (int i = 0; i < 9; i++) {
            Controller newControl = new Controller("Btn " + String.valueOf(i), "", i);
            adapterControler.addControler(newControl);
        }



    }

    @NonNull
    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.SECOND));
    }

    private void createNewSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View terminalSettingsPopupView = getLayoutInflater().inflate(R.layout.popup_terminal_settings, null);
        CheckBox checkBoxTerminalAutoscroll = terminalSettingsPopupView.findViewById(R.id.checkAutoScroll),
                checkBoxTerminalHideConsumed = terminalSettingsPopupView.findViewById(R.id.checkConsumedCmd),
                checkBoxTerminalHideRequest = terminalSettingsPopupView.findViewById(R.id.checkMonitorMessage);
        checkBoxTerminalAutoscroll.setChecked(setting_autoscroll);
        checkBoxTerminalHideConsumed.setChecked(setting_hide_consumed);
        checkBoxTerminalHideRequest.setChecked(setting_hide_request);

        checkBoxTerminalAutoscroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setting_autoscroll = checkBoxTerminalAutoscroll.isChecked();
                if (setting_autoscroll) {
                    terminalRecyclerView.scrollToPosition(adapterTerminal.getItemCount() - 1);
                }
            }
        });

        checkBoxTerminalHideConsumed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setting_hide_consumed = checkBoxTerminalHideConsumed.isChecked();
                adapterTerminal.hideConsumedMessages(setting_hide_consumed);
            }
        });

        checkBoxTerminalHideRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setting_hide_request = checkBoxTerminalHideRequest.isChecked();
                adapterTerminal.hideRequestMessages(setting_hide_request);
            }
        });

        builder.setView(terminalSettingsPopupView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createAddReceiverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View createReceiverMonitorPopupView = getLayoutInflater().inflate(R.layout.popup_add_receiver_monitor, null);
        final EditText editTxtName = createReceiverMonitorPopupView.findViewById(R.id.editTxtReceiverName),
                editTxtUnit = createReceiverMonitorPopupView.findViewById(R.id.editTxtReceiverUnit),
                editTxtCommand = createReceiverMonitorPopupView.findViewById(R.id.editTxtReceiverCommand),
                editTxtRequest = createReceiverMonitorPopupView.findViewById(R.id.editTxtReceiverRequestCommand),
                editTxtFrequency = createReceiverMonitorPopupView.findViewById(R.id.editTxtReceiverFrequency);
        final RadioGroup radioMode = createReceiverMonitorPopupView.findViewById(R.id.radioGroupMode);
        final Group grpModeActive = createReceiverMonitorPopupView.findViewById(R.id.groupReceiverActive);
        final TextView txtExplanation = createReceiverMonitorPopupView.findViewById(R.id.txtCreateReceiverExplanation);

        grpModeActive.setVisibility(View.GONE);

        builder.setView(createReceiverMonitorPopupView).setTitle(this.getResources().getString(R.string.dev_monitor_add_receiver_title))
                .setNegativeButton("Annuler", null).setPositiveButton("Créer", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverName = editTxtName.getText().toString(), receiverCmd = editTxtCommand.getText().toString(), receiverUnit = editTxtUnit.getText().toString();

                if (radioMode.getCheckedRadioButtonId() == R.id.radioReceiverModeWait) {
                    if (receiverName.isEmpty() || receiverCmd.isEmpty()) {
                        Toast.makeText(DeveloperBluetoothControlActivity.this, R.string.dev_receiver_creation_error, Toast.LENGTH_LONG).show();
                    } else {
                        PassiveReceiverMonitor newReceiver = new PassiveReceiverMonitor(receiverName, receiverCmd, receiverUnit.isEmpty() ? "" : receiverUnit);
                        SaveManager.getInstance(DeveloperBluetoothControlActivity.this).addPassiveReceiver(newReceiver);
                        adapterMonitor.addReceiver(newReceiver);
                        dialog.dismiss();
                    }
                } else {
                    String receiverRequest = editTxtRequest.getText().toString(), receiverFrequency = editTxtFrequency.getText().toString();

                    if (receiverName.isEmpty() || receiverCmd.isEmpty() || receiverRequest.isEmpty() || receiverFrequency.isEmpty()) {
                        Toast.makeText(DeveloperBluetoothControlActivity.this, R.string.dev_receiver_creation_error, Toast.LENGTH_LONG).show();
                    } else {
                        ActiveReceiverMonitor newReceiver = new ActiveReceiverMonitor(receiverName, receiverRequest, receiverFrequency, receiverCmd, receiverUnit.isEmpty() ? "" : receiverUnit);
                        SaveManager.getInstance(DeveloperBluetoothControlActivity.this).addActiveReceiver(newReceiver);
                        adapterMonitor.addReceiver(newReceiver);
                        dialog.dismiss();
                    }
                }
            }
        });

        radioMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioReceiverModeWait) {
                    grpModeActive.setVisibility(View.GONE);
                    txtExplanation.setText(getResources().getString(R.string.dev_receiver_creation_explanation_passive));

                } else {
                    grpModeActive.setVisibility(View.VISIBLE);
                    txtExplanation.setText(getResources().getString(R.string.dev_receiver_creation_explanation_active));
                }
            }
        });
    }

    public void writeMessage(String msg, int type) {
        if (!msg.isEmpty()) {
            TerminalMessage newTerminalMsg = new TerminalMessage(getCurrentTime(), msg, type, TerminalMessage.TYPE_REQUEST);
            adapterTerminal.addMessage(newTerminalMsg);
            if (setting_autoscroll) {
                terminalRecyclerView.scrollToPosition(adapterTerminal.getItemCount() - 1);
            }
            BluetoothArduino.getInstance().getBtConnectedThread().write(newTerminalMsg.getContent()+'\n');
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        // TODO: Completer cette fonction
//        for (ReceiverMonitor receiver : adapterMonitor.getReceives()) {
//            receiver.getTimer().cancel();
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (ReceiverMonitor receiver : adapterMonitor.getReceives()) {
            receiver.getTimer().cancel();
        }
        BluetoothArduino.getInstance(this).getBtConnectedThread().cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dev_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnActionLogOut:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DevActivity", "CLOSE EIXST");
        BluetoothArduino.getInstance(this).getBtConnectedThread().cancel();
    }
}