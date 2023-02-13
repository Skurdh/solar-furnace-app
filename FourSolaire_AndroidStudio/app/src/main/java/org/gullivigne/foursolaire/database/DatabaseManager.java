package org.gullivigne.foursolaire.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.gullivigne.foursolaire.dev.controler.Controller;
import org.gullivigne.foursolaire.dev.monitor.ActiveReceiverMonitor;
import org.gullivigne.foursolaire.dev.monitor.PassiveReceiverMonitor;

import java.util.ArrayList;

public class DatabaseManager {

    private static final String KEY_APP = "db_app",
            KEY_DEVICES_INFO = "db_devices_info",
            KEY_PASSIVE_RECEIVERS = "db_passive_receivers",
            KEY_ACTIVE_RECEIVERS = "db_active_receivers",
            KEY_CONTROLLERS = "db_controllers";

    private static DatabaseManager instance = null;
    private SharedPreferences sharedPreferences;
    private Context mContext;

    private String currentKey = "";

    private DatabaseManager(Context context) {
        mContext = context;
    }

    /**
     * Singleton pattern, create an unique instance of SaveManager class and return it.
     * @return instance
     */
    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        if (context != instance.mContext) {
            instance.mContext = context;
        }

        return instance;
    }

    /**
     *
     * @param databaseKey
     */
    private void checkDatabaseCursor(String databaseKey) {
        if (currentKey.equals(databaseKey)) { return; }

        sharedPreferences = mContext.getSharedPreferences(databaseKey, Context.MODE_PRIVATE);
        currentKey = databaseKey;
    }

    /* ----- DEVICE INFORMATION ----- */

    /**
     *
     * @return
     */
    public ArrayList<DeviceInfoData> getDevicesInfo() {
        checkDatabaseCursor(KEY_APP);

        Gson gson = new Gson();
        if (sharedPreferences.getString(KEY_APP, null) == null) {
            return null;
        } else {
            return gson.fromJson(sharedPreferences.getString(KEY_APP, null), new TypeToken<ArrayList<DeviceInfoData>>() {}.getType());
        }
    }

    /**
     *
     * @param deviceInfo
     * @return
     */
    public boolean addDeviceInfo(DeviceInfoData deviceInfo) {
        checkDatabaseCursor(KEY_APP);

        ArrayList<DeviceInfoData> devicesInfo = getDevicesInfo();
        if (devicesInfo == null) { devicesInfo = new ArrayList<>(); }
        if (devicesInfo.add(deviceInfo)) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (devicesInfo.size() - 1 > 0) { editor.remove(KEY_DEVICES_INFO); }
            editor.putString(KEY_DEVICES_INFO, gson.toJson(devicesInfo));
            editor.apply();
            return true;
        }

        return false;
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


