package org.gullivigne.foursolaire.dev.monitor;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.Locale;

public class CountUpTimer extends CountDownTimer {

    private int hours, minutes, seconds;

    public CountUpTimer() {
        super(3600*1000, 1000);
        hours = 0;
        minutes = 0;
        seconds = 0;
    }

    @Override
    public void onTick(long l) {
        seconds++;
        if (seconds > 59) {
            minutes++;
            seconds = 0;
        }
        if (minutes > 59) {
            hours++;
            minutes = 0;
        }
        if (hours > 23) {
            resetValues();
        }
    }

    @Override
    public void onFinish() {
        resetValues();
    }

    public void reset() {
        this.cancel();
        resetValues();
    }

    private void resetValues() {
        hours = 0;
        minutes = 0;
        seconds = 0;
    }

    public String getTime() {
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

}
