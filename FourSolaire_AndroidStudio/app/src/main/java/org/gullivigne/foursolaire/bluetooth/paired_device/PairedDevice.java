package org.gullivigne.foursolaire.bluetooth.paired_device;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class PairedDevice {
    final private int id;
    final private BluetoothDevice device;
    private String alias;
    private ArrayList<String> associated_files;

    public PairedDevice(int id, BluetoothDevice device) {
        this.id = id;
        this.device = device;
        alias = "";
        associated_files = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    @SuppressLint("MissingPermission")
    public String getName() {
        return device.getName();
    }

    @SuppressLint("MissingPermission")
    public String getAddress() {
        return device.getAddress();
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<String> getAssociated_files() {
        return associated_files;
    }

    public void setAssociated_files(ArrayList<String> associated_files) {
        this.associated_files = associated_files;
    }
}
