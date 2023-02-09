package org.gullivigne.foursolaire.dev.monitor;

public class PassiveReceiverMonitor extends ReceiverMonitor {

    private CountUpTimer timer;

    public PassiveReceiverMonitor(String title, String command, String unit) {
        super(MODE_WAIT, title, command, unit);
    }

    public CountUpTimer getTimer() {
        return timer;
    }

    public void setTimer(CountUpTimer timer) {
        this.timer = timer;
    }
}
