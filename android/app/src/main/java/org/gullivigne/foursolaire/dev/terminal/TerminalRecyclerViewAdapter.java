package org.gullivigne.foursolaire.dev.terminal;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.gullivigne.foursolaire.R;

import java.util.ArrayList;

public class TerminalRecyclerViewAdapter extends RecyclerView.Adapter<TerminalRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TerminalMessage> messages;
    private Context mContext;
//    private boolean showConsumed = true;

    public TerminalRecyclerViewAdapter(Context context) {
        messages = new ArrayList<>();
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_developer_terminal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TerminalMessage msg = messages.get(position);
        int type = msg.getType();
        if (type == TerminalMessage.ARDUINO) {
            holder.txtDevTerminalMessage.setTextColor(mContext.getColor(R.color.arduino));
            holder.txtDevTerminalMessage.setText("[" + msg.getTime() + "] [ARDUINO] : " + msg.getContent());
        } else {
            holder.txtDevTerminalMessage.setTextColor(mContext.getColor(R.color.android));
            holder.txtDevTerminalMessage.setText("[" + msg.getTime() + "] [VOUS] : " + msg.getContent());
        }

        if (msg.getType() != TerminalMessage.TYPE_NORMAL) {
            msg.setTxtBinded(holder.txtDevTerminalMessage);
            holder.txtDevTerminalMessage.setPaintFlags(holder.txtDevTerminalMessage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDevTerminalMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDevTerminalMessage = itemView.findViewById(R.id.txtDevTerminalMessage);
        }
    }

    public void addMessage(TerminalMessage message) {
        this.messages.add(message);
        notifyItemChanged(this.messages.size());
    }

    public void clearMessages() {
        int size = this.messages.size();
        if (size > 0) {
            this.messages.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public void hideConsumedMessages(boolean hide) {
        for (TerminalMessage message : messages) {
            if (message.getType() == TerminalMessage.TYPE_CONSUMED) {
                if (hide) {
                    message.getTxtBinded().setVisibility(View.GONE);
                } else {
                    message.getTxtBinded().setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void hideRequestMessages(boolean hide) {
        for (TerminalMessage message : messages) {
            if (message.getType() == TerminalMessage.TYPE_REQUEST) {
                if (hide) {
                    message.getTxtBinded().setVisibility(View.GONE);
                } else {
                    message.getTxtBinded().setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
