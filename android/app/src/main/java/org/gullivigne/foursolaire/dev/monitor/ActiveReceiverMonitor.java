package org.gullivigne.foursolaire.dev.monitor;

public class ActiveReceiverMonitor extends ReceiverMonitor {

    private String requestCommand, requestFrequency;

    public ActiveReceiverMonitor(String title, String requestCommand, String requestFrequency, String listenCommand, String unit) {
        super(MODE_ASK, title, listenCommand, unit);
        this.requestCommand = requestCommand;
        this.requestFrequency = requestFrequency;
    }

    public String getRequestCommand() {
        return requestCommand;
    }

    public String getRequestFrequency() {
        return requestFrequency;
    }

}
