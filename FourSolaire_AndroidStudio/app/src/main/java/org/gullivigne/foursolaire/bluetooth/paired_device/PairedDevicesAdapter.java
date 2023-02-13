package org.gullivigne.foursolaire.bluetooth.paired_device;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.gullivigne.foursolaire.MainActivity;
import org.gullivigne.foursolaire.R;
import org.gullivigne.foursolaire.bluetooth.BluetoothService;

import java.util.ArrayList;

public class PairedDevicesAdapter extends RecyclerView.Adapter<PairedDevicesAdapter.ViewHolder> {



    private final ArrayList<PairedDevice> pairedDevices;
    private final Handler mHandler;
    private final AlertDialog alertDialog;

    public PairedDevicesAdapter(Handler handler, AlertDialog connectingDialog) {
        mHandler = handler;
        alertDialog = connectingDialog;
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
        PairedDevice device = pairedDevices.get(position);

        holder.txtPairedDeviceName.setText(device.getName());
        holder.txtPairedDeviceAddress.setText(device.getAddress());
         if (!device.getAlias().isEmpty()) {
             holder.getTxtPairedDeviceAlias.setVisibility(View.VISIBLE);
             holder.getTxtPairedDeviceAlias.setText(device.getAlias());
         }

        holder.cardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                assert BluetoothService.getInstance() != null;
                BluetoothService.getInstance().connect(device.getDevice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.pairedDevices.size();
    }

    public void addPairedDevice(PairedDevice device) {
        this.pairedDevices.add(device);
        notifyItemInserted(this.pairedDevices.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtPairedDeviceName, txtPairedDeviceAddress, getTxtPairedDeviceAlias;
        private final CardView cardParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardParent = itemView.findViewById(R.id.cardScanPairedDevices);
            txtPairedDeviceName = itemView.findViewById(R.id.txtScanPairedDevicesItemName);
            txtPairedDeviceAddress = itemView.findViewById(R.id.txtMonitorValue);
            getTxtPairedDeviceAlias = itemView.findViewById(R.id.txtScanPairedDevicesItemAlias);
        }
    }
}
