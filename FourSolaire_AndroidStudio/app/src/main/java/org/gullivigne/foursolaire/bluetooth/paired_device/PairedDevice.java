package org.gullivigne.foursolaire.bluetooth.paired_device;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class PairedDevice {
    final private int id;
    final private BluetoothDevice device;
    private String nickname;
    private ArrayList<String> associated_files;

    public PairedDevice(int id, BluetoothDevice device) {
        this.id = id;
        this.device = device;
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


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<String> getAssociated_files() {
        return associated_files;
    }

    public void setAssociated_files(ArrayList<String> associated_files) {
        this.associated_files = associated_files;
    }
}
