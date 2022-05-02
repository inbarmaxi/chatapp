package com.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatappinbar.ChatMessage;
import com.example.chatappinbar.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;

public class ChatRv extends RecyclerView.Adapter<ChatRv.ChatViewHolder> {


    private List<ChatMessage> messageList;
    private FirebaseUser user;
    private Calendar c = Calendar.getInstance();

    public ChatRv(List<ChatMessage> messageList, FirebaseUser user) {
        this.messageList = messageList;
        this.user = user;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false));
    }


    private String getDateTimeString(long date) {
        c.setTimeInMillis(date);
        return c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        ChatMessage cur = messageList.get(position);

        holder.senderNameTv.setText(cur.getSenderName());
        holder.contentTv.setText(cur.getContent());
        holder.dateTv.setText(getDateTimeString(cur.getDate()));
        Glide.with(holder.itemView.getContext()).load(cur.getSenderImage()).into(holder.senderImage);

        if (user != null)
            if (cur.getSenderName().equals(user.getUid()))
                holder.itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {


        TextView contentTv, senderNameTv, dateTv;
        ImageView senderImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.contentTv = itemView.findViewById(R.id.messageContent);
            this.senderNameTv = itemView.findViewById(R.id.senderNameTv);
            this.dateTv = itemView.findViewById(R.id.messageDateTime);
            this.senderImage = itemView.findViewById(R.id.senderIv);
        }
    }
}
