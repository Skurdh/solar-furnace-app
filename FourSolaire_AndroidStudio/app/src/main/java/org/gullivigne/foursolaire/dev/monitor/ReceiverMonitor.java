package org.gullivigne.foursolaire.dev.monitor;

import android.os.CountDownTimer;
import android.widget.Chronometer;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.UUID;

public class ReceiverMonitor {

    public static final int MODE_ASK = 0, MODE_WAIT = 1;
    private int mode;
    private String title = "";
    private String command = "";
    private String unit = "";
    private transient CountDownTimer timer;
    private transient String value = "null";
    private transient TextView txtValue;

    public ReceiverMonitor(int mode, String title, String command, String unit) {
        this.mode = mode;
        this.title = title;
        this.command = command;
        this.unit = unit;
    }

    public String getTitle() {
        return title;
    }


    public String getCommand() {
        return command;
    }


    public int getMode() {
        return mode;
    }


    public String getUnit() {
        return unit;
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
        this.txtValue.setText(value + " " + unit);
    }

    public void bindTextView(TextView txtView) { this.txtValue = txtView; }

    public String toJSON() {
        return "\"" + UUID.randomUUID().toString().substring(0,5) + "\":{" +
                "\"command\":\"" + command + "\"," +
                "\"mode\":\"" + String.valueOf(mode) + "\"," +
                "\"title\":\"" + title + "\"," +
                "\"unit\":\"" + unit + "\"" +
                "}";
    }
}
