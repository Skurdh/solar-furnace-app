package org.gullivigne.foursolaire.dev;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.gullivigne.foursolaire.dev.monitor.ReceiverMonitor;

import java.util.ArrayList;

public class SaveManager {

    private static final String DB_NAME = "db_dev", KEY_RECEIVERS = "db_receivers";

    private static SaveManager instance = null;
    private SharedPreferences sharedPreferences;

    public SaveManager(Context context) {
        sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
    }

    public void loadData() {
        ArrayList<ReceiverMonitor> receivers = new ArrayList<>();

        Gson gson = new Gson();

    }

    public ArrayList<ReceiverMonitor> getReceivers() {
        Gson gson = new Gson();
        if (sharedPreferences.getString(KEY_RECEIVERS, null) == null) {
            return null;
        } else {
            return gson.fromJson(sharedPreferences.getString(KEY_RECEIVERS, null), new TypeToken<ArrayList<ReceiverMonitor>>() {
            }.getType());
        }
    }

    public void addReceiver(ReceiverMonitor receiver) {
        ArrayList<ReceiverMonitor> receivers = getReceivers();
        if (receivers == null) { receivers = new ArrayList<>();}

        if (receivers.add(receiver)) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (receivers.size() > 0) {editor.remove(KEY_RECEIVERS);}
            editor.putString(KEY_RECEIVERS, gson.toJson(receivers));
            editor.apply();
        }
    }

    public void removeReceiver(ReceiverMonitor receiver) {
        ArrayList<ReceiverMonitor> receivers = getReceivers();
        if(receivers!= null && receivers.remove(receiver)) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_RECEIVERS, gson.toJson(receivers));
            editor.apply();
        }
    }


    public static SaveManager getInstance(Context context) {
        if (instance == null) {
            instance = new SaveManager(context);
        }
        return instance;
    }
}
