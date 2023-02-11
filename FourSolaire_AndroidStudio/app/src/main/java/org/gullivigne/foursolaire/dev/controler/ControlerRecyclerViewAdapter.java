package org.gullivigne.foursolaire.dev.controler;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Handler;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.gullivigne.foursolaire.BluetoothArduino;
import org.gullivigne.foursolaire.R;

import java.util.ArrayList;

public class ControlerRecyclerViewAdapter extends RecyclerView.Adapter<ControlerRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Controller> controlers;
    private Context mContext;
    private Handler mHandler;

    public ControlerRecyclerViewAdapter(Context context, Handler handler) {
        controlers = new ArrayList<>();
        this.mContext = context;
        this.mHandler = handler;
    }

    @NonNull
    @Override
    public ControlerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_developer_controler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ControlerRecyclerViewAdapter.ViewHolder holder, int position) {
        controlers.get(holder.getAdapterPosition()).setBtnBinded(holder.btnControler);
        holder.btnControler.setText(controlers.get(holder.getAdapterPosition()).getName());
        holder.btnControler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!controlers.get(holder.getAdapterPosition()).getCommand().isEmpty()) {
                    mHandler.obtainMessage(BluetoothArduino.MESSAGE_REQUEST, controlers.get(holder.getAdapterPosition()).getCommand()).sendToTarget();
                }
            }
        });

        // Todo
        holder.btnControler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final View modifyControllerPopupView = LayoutInflater.from(mContext).inflate(R.layout.popup_modify_controler, null);

                EditText editTxtName = modifyControllerPopupView.findViewById(R.id.editTxtControlerName), editTxtCommand = modifyControllerPopupView.findViewById(R.id.editTxtControlerCommand);

                builder.setView(modifyControllerPopupView).setTitle("Modifier le bouton").setNegativeButton("Annuler", null)
                        .setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Controller controler = controlers.get(holder.getAdapterPosition());
                                controler.setName(!editTxtName.getText().toString().isEmpty() ? editTxtName.getText().toString() : "BTN " + String.valueOf(controler.getId()) );
                                controler.setCommand(editTxtCommand.getText().toString());
                                controler.getBtnBinded().setText(controler.getName());
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return controlers.size();
    }

    public void addControler(Controller controler) {
        this.controlers.add(controler);
        notifyItemInserted(getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btnControler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnControler = itemView.findViewById(R.id.btnControler);
        }
    }
}
