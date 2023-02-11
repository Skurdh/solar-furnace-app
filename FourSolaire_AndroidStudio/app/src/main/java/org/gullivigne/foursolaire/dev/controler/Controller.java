package org.gullivigne.foursolaire.dev.controler;

import android.widget.Button;

public class Controller {

    private String name, command;
    private int id;
    private Button btnBinded;

    public Controller(String name, String command, int id) {
        this.name = name;
        this.command = command;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public int getId() {
        return id;
    }

    public void setBtnBinded(Button btnBinded) {
        this.btnBinded = btnBinded;
    }

    public Button getBtnBinded() {
        return btnBinded;
    }
}
