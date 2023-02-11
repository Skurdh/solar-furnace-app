package org.gullivigne.foursolaire.dev.terminal;

import android.widget.TextView;

public class TerminalMessage {

    public static final int ARDUINO = 0, ANDROID = 1;
    public static final int TYPE_NORMAL = 0, TYPE_REQUEST = 1, TYPE_CONSUMED = 2;

    private String time, content;
    private int source;
    private int type = TYPE_NORMAL;
    private TextView txtBinded;

    public TerminalMessage(String time, String content, int source, int type) {
        this.time = time;
        this.content = content;
        this.source = source;
        this.type = type;
    }

    public TerminalMessage(String time, String content, int source) {
        this.time = time;
        this.content = content;
        this.source = source;
        this.type = TYPE_NORMAL;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public int getSource() {
        return source;
    }

    public int getType() {
        return type;
    }

    public TextView getTxtBinded() {
        return txtBinded;
    }

    public void setTxtBinded(TextView txtBinded) {
        this.txtBinded = txtBinded;
    }
}
