package org.gullivigne.foursolaire.database;

import java.util.ArrayList;

public class DeviceInfoData {

    private final String address;
    private final String alias;
    private final ArrayList<String> files_name;

    public DeviceInfoData(String address, String alias, ArrayList<String> files_name) {
        this.address = address;
        this.alias = alias;
        this.files_name = files_name;
    }

    public String getAddress() {
        return address;
    }

    public String getAlias() {
        return alias;
    }

    public ArrayList<String> getFiles_name() {
        return files_name;
    }
}
