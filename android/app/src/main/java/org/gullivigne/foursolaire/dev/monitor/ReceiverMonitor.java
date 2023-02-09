package org.gullivigne.foursolaire.dev.monitor;

import android.os.CountDownTimer;
import android.widget.Chronometer;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReceiverMonitor {

    public static final int MODE_ASK = 0, MODE_WAIT = 1;
    private int mode;
    private String title = "";
    private String command = "";
    private String unit = "";
    private CountDownTimer timer;
    private String value = "-";
    private TextView txtValue;

    public ReceiverMonitor(int mode, String title, String command, String unit) {
        this.mode = mode;
        this.title = title;
        this.command = command;
        this.unit = unit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.txtValue.setText(value);
    }

    public void bindTextView(TextView txtView) { this.txtValue = txtView; }

}
