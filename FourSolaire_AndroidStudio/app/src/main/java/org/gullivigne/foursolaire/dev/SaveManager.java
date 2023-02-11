package org.gullivigne.foursolaire.dev;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.gullivigne.foursolaire.dev.controler.Controller;
import org.gullivigne.foursolaire.dev.monitor.ActiveReceiverMonitor;
import org.gullivigne.foursolaire.dev.monitor.PassiveReceiverMonitor;

import java.io.File;
import java.util.ArrayList;

public class SaveManager {

    private static final String KEY_FILES_LIST = "db_files_name",
            KEY_PASSIVE_RECEIVERS = "db_passive_receivers",
            KEY_ACTIVE_RECEIVERS = "db_active_receivers",
            KEY_CONTROLLERS = "db_controllers";

    private static SaveManager instance = null;
    private SharedPreferences sharedPreferences;


    public SaveManager(Context context) {
        selectFilesList(context);
    }

    /* ----- DEV FILES ----- */

    /**
     * Retrieves the files name data associated with the bluetooth device address
     * @param address Bluetooth device address
     * @return ArrayList of String or null
     */
    public ArrayList<String> getDeviceAssociatedFiles(String address) {
        String files_name = sharedPreferences.getString(address, null);
        if ( files_name == null) {
            return null;
        } else {
            ArrayList<String> files = new ArrayList<>();
            StringBuilder name = new StringBuilder();

            for (int i = 0; i < files_name.length(); i++) {
                if (files_name.charAt(i) == ',') {
                    files.add(name.toString());
                    name = new StringBuilder();
                } else {
                    name.append(files_name.charAt(i));
                }
            }
            return files;
        }
    }

    /**
     * Verifies if the file name exist and adds a new file in the SharedPreference
     * @param address Bluetooth device address
     * @param file_name Name of the new file
     * @return Boolean
     */
    public boolean addDeviceFiles(String address, String file_name) {
        ArrayList<String> files = getDeviceAssociatedFiles(address);
        if (files == null) { files = new ArrayList<>(); }

        for (String f : files ) {
            if (f.equals(file_name)) { return false; }
        }

        if (files.add(file_name)) {
            StringBuilder files_name = new StringBuilder();
            for (String f : files) {
                files_name.append(f);
                files_name.append(',');
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (files.size() - 1 > 0) { editor.remove(address); }
            editor.putString(address, files_name.toString());
            editor.apply();
        }
        return true;
    }

    /**
     * Select the SharedPreference database by the file name
     * @param file_name Name of the file
     */
    public void selectDeviceFile(String file_name, Context context) {
        sharedPreferences = context.getSharedPreferences(file_name, Context.MODE_PRIVATE);
    }

    /**
     * Select the files list SharedPreference database
     */
    public void selectFilesList(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_FILES_LIST, Context.MODE_PRIVATE);
    }


    /* ----- PASSIVE RECEIVER ----- */

    /**
     Retrieves the data saved from the Passive Receiver in the SharedPreference in JSON format and returns it.
     @return ArrayList of PassiveReceiverMonitor or null
     */
    public ArrayList<PassiveReceiverMonitor> getPassiveReceivers() {
        Gson gson = new Gson();
        if (sharedPreferences.getString(KEY_PASSIVE_RECEIVERS, null) == null) {
            return null;
        } else {
            return gson.fromJson(sharedPreferences.getString(KEY_PASSIVE_RECEIVERS, null), new TypeToken<ArrayList<PassiveReceiverMonitor>>() {}.getType());
        }
    }

    /**
     Adds a passive receiver in the SharedPreference in JSON format.
     @param receiver Object of Passive Receiver Monitor
     */
    public void addPassiveReceiver(PassiveReceiverMonitor receiver) {
        ArrayList<PassiveReceiverMonitor> receivers = getPassiveReceivers();
        if (receivers == null) { receivers = new ArrayList<>(); }
        if (receivers.add(receiver)) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (receivers.size() - 1 > 0) { editor.remove(KEY_PASSIVE_RECEIVERS); }
            editor.putString(KEY_PASSIVE_RECEIVERS, gson.toJson(receivers));
            editor.apply();
        }
    }

    /**
     Remove a passive receiver in the SharedPreference in JSON format.
     @param receiver Object of Passive Receiver Monitor
     */
    public void removePassiveReceiver(PassiveReceiverMonitor receiver) {
        ArrayList<PassiveReceiverMonitor> receivers = getPassiveReceivers();
        if (receivers == null) { return; }
        if (receivers.remove(receiver)) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_PASSIVE_RECEIVERS);
            editor.putString(KEY_PASSIVE_RECEIVERS, gson.toJson(receivers));
            editor.apply();
        }
    }

    /* ----- ACTIVE RECEIVER ----- */

    /**
     Retrieves the data saved from the Active Receiver in the SharedPreference in JSON format and returns it.
     @return ArrayList of ActiveReceiverMonitor or null
     */
    public ArrayList<ActiveReceiverMonitor> getActiveReceivers() {
        Gson gson = new Gson();
        if (sharedPreferences.getString(KEY_ACTIVE_RECEIVERS, null) == null) {
            return null;
        } else {
            return gson.fromJson(sharedPreferences.getString(KEY_ACTIVE_RECEIVERS, null), new TypeToken<ArrayList<ActiveReceiverMonitor>>() {}.getType());
        }
    }

    /**
     Adds a active receiver in the SharedPreference in JSON format.
     @param receiver Object of Active Receiver Monitor
     */
    public void addActiveReceiver(ActiveReceiverMonitor receiver) {
        ArrayList<ActiveReceiverMonitor> receivers = getActiveReceivers();
        if (receivers == null) { receivers = new ArrayList<>(); }
        if (receivers.add((ActiveReceiverMonitor) receiver)) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (receivers.size() - 1 > 0) { editor.remove(KEY_ACTIVE_RECEIVERS); }
            editor.putString(KEY_ACTIVE_RECEIVERS, gson.toJson(receivers));
            editor.apply();
        }
    }

    /**
     Remove a active receiver in the SharedPreference in JSON format.
     @param receiver Object of Active Receiver Monitor
     */
    public void removeActiveReceiver(ActiveReceiverMonitor receiver) {
        ArrayList<ActiveReceiverMonitor> receivers = getActiveReceivers();
        if (receivers == null) { return; }
        if (receivers.remove(receiver)) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_ACTIVE_RECEIVERS);
            editor.putString(KEY_ACTIVE_RECEIVERS, gson.toJson(receivers));
            editor.apply();
        }
    }

    /**
     * Singleton pattern, create an unique instance of SaveManager class and return it.
     * @return instance
     */
    public static SaveManager getInstance(Context context) {
        if (instance == null) {
            instance = new SaveManager(context);
        }
        return instance;
    }

    /* ----- CONTROL BUTTON ----- */

    /**
     * Retrieves the data saved from the Active Receiver in the SharedPreference in JSON format and returns it.
     * @return ArrayList of Controller or null
     */
    public ArrayList<Controller> getControllers() {
        Gson gson = new Gson();
        if (sharedPreferences.getString(KEY_CONTROLLERS, null) == null) {
            return null;
        } else {
            return gson.fromJson(sharedPreferences.getString(KEY_CONTROLLERS, null), new TypeToken<ArrayList<ActiveReceiverMonitor>>() {}.getType());
        }
    }

    /**
     * Modifies the data of a Controller or create them if the data does not exist
     * @param controller Object of Controller
     */
    public void modifyController (Controller controller) {
        boolean controllerExist = false;
        ArrayList<Controller> controllers = getControllers();
        if (controllers == null) { controllers = new ArrayList<>(); }

        for (Controller c : controllers) {
            if (c.getId() == controller.getId()) {
                c.setName(controller.getName());
                c.setCommand(controller.getCommand());
                controllerExist = true;
            }
        }

        if (!controllerExist) {
            controllers.add(controller);
        }

        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (controllers.size() - 1 > 0) { editor.remove(KEY_CONTROLLERS); }
        editor.putString(KEY_CONTROLLERS, gson.toJson(controllers));
        editor.apply();
    }
}


