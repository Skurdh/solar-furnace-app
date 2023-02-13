package org.gullivigne.foursolaire.dev.monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.gullivigne.foursolaire.BluetoothArduino;
import org.gullivigne.foursolaire.R;

import java.util.ArrayList;

public class MonitorRecyclerViewAdapater extends RecyclerView.Adapter<MonitorRecyclerViewAdapater.ViewHolder> {

    private ArrayList<ReceiverMonitor> receives;
    private Context mContext;
    public Handler mHandler;

    public MonitorRecyclerViewAdapater(Context context, Handler handler) {
        receives = new ArrayList<>();
        mContext = context;
        mHandler = handler;
    }

    @NonNull
    @Override
    public MonitorRecyclerViewAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_developer_receive_monitor, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MonitorRecyclerViewAdapater.ViewHolder holder, int position) {
        if (receives.get(position).getMode() == ReceiverMonitor.MODE_WAIT) {
            PassiveReceiverMonitor receiver = (PassiveReceiverMonitor) receives.get(holder.getAdapterPosition());

            receiver.bindTextView(holder.txtValue); // Lier le TextView au receiver pour éviter de refresh les items.
            // Création du chronomètre
            holder.timerUp.setVisibility(View.VISIBLE);
            holder.progressTimer.setVisibility(View.GONE);
            receiver.setTimer(new CountUpTimer() {
                @Override
                public void onTick(long l) {
                    super.onTick(l);
                    holder.timerUp.setText(receiver.getTimer().getTime());
                }
            });
            receiver.getTimer().start();

        } else {
            ActiveReceiverMonitor receiver = (ActiveReceiverMonitor) receives.get(holder.getAdapterPosition());

            receiver.bindTextView(holder.txtValue); // Lier le TextView au receiver pour éviter de refresh les items.
            // Création de la fréquence de refresh
            holder.timerUp.setVisibility(View.GONE);
            holder.progressTimer.setVisibility(View.VISIBLE);
            int frequency = Integer.parseInt(receiver.getRequestFrequency());
            holder.progressTimer.setMax(frequency);
            receiver.setTimer(new CountDownTimer(frequency * 1000L, 1000) {
                @Override
                public void onTick(long l) {
                    holder.progressTimer.setProgress(frequency - (int) l / 1000);
                }

                @Override
                public void onFinish() {
                    mHandler.obtainMessage(BluetoothArduino.MESSAGE_REQUEST, receiver.getRequestCommand()).sendToTarget();
                    this.cancel();
                }
            });
            receiver.getTimer().start();
        }

        holder.txtValue.setText("null " + receives.get(holder.getAdapterPosition()).getUnit());
        holder.txtTitle.setText(receives.get(holder.getAdapterPosition()).getTitle());
        holder.mainCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mContext).setTitle("Supprimer")
                        .setMessage("Voulez vous supprimer le récepteur " + receives.get(holder.getAdapterPosition()).getTitle() + " ?")
                        .setNegativeButton("Annuler", null)
                        .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteReceiver(holder.getAdapterPosition());
                            }
                        })
                        .create().show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return receives.size();
    }

    public ArrayList<ReceiverMonitor> getReceives() {
        return receives;
    }

    public void addReceiver(ReceiverMonitor receiver) {
        this.receives.add(receiver);
        notifyItemInserted(this.receives.size());
    }

    public void deleteReceiver(int position) {
//        SaveManager.getInstance(mContext).removeReceiver(this.receives.get(position));
        this.receives.get(position).getTimer().cancel();
        this.receives.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mainCard;
        private TextView txtTitle, txtValue, timerUp;
        private ProgressBar progressTimer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainCard = itemView.findViewById(R.id.cardReceiveMonitor);
            txtTitle = itemView.findViewById(R.id.txtMonitorTitle);
            txtValue = itemView.findViewById(R.id.txtMonitorValue);
            timerUp = itemView.findViewById(R.id.txtCountUpTimer);
            progressTimer = itemView.findViewById(R.id.progressMonitorTimer);
        }
    }
}
