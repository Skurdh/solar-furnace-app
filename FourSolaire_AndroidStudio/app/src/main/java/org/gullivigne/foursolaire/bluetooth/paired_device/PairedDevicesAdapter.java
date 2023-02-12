package org.gullivigne.foursolaire.bluetooth.paired_device;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.gullivigne.foursolaire.BluetoothArduino;
import org.gullivigne.foursolaire.R;
import org.gullivigne.foursolaire.dev.DeveloperBluetoothControlActivity;

import java.util.ArrayList;

public class PairedDevicesAdapter extends RecyclerView.Adapter<PairedDevicesAdapter.ViewHolder> {

    private ArrayList<BluetoothDevice> pairedDevices;
    private Context mContext;

    public PairedDevicesAdapter(Context context) {
        mContext = context;
        pairedDevices = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_paired_device, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtPairedDeviceName.setText(pairedDevices.get(position).getName());
        holder.txtPairedDeviceAdress.setText(pairedDevices.get(position).getAddress());

        holder.cardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothArduino.getInstance(mContext).setArduinoDevice(pairedDevices.get(holder.getAdapterPosition()));
                BluetoothArduino.getInstance(mContext).setArduinoUUID(pairedDevices.get(holder.getAdapterPosition()).getUuids()[0].getUuid());
                if (BluetoothArduino.getInstance(mContext).startConnection()) {
                    Intent intent = new Intent(mContext, DeveloperBluetoothControlActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, R.string.config_bt_connection_failure, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.pairedDevices.size();
    }

    public void addPairedDevice(BluetoothDevice device) {
        this.pairedDevices.add(device);
        notifyItemInserted(this.pairedDevices.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPairedDeviceName, txtPairedDeviceAdress;
        private CardView cardParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardParent = itemView.findViewById(R.id.cardScanPairedDevices);
            txtPairedDeviceName = itemView.findViewById(R.id.txtScanPairedDevicesItemName);
            txtPairedDeviceAdress = itemView.findViewById(R.id.txtMonitorValue);
        }
    }
}
