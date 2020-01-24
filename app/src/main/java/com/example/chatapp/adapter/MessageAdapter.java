package com.example.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MESSAGE_LEFT = 0;
    private static final int MESSAGE_RIGHT = 1;

    private List<Chat> chats = new ArrayList<>();

    public MessageAdapter(){    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view, viewType);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view, viewType);
        }
    }

    public void addChat(Chat chat){
        chats.add(chat);
        notifyItemInserted(chats.size()-1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(holder.value == 0){
            holder.fillReceiverView(chats.get(position));
        }
        else {
            holder.fillSenderView(chats.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(user.getUid())){
            return MESSAGE_RIGHT;
        }
        else {
            return MESSAGE_LEFT;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView show_message;
        private TextView user;
        private int value;

        ViewHolder(View view, int value){
            super(view);
            this.value = value;

            if(this.value == 0){
                show_message = view.findViewById(R.id.show_message);
                user = view.findViewById(R.id.user);
            }
            else {
                show_message = view.findViewById(R.id.show_message);
            }
        }

        void fillSenderView(Chat chat) {
            show_message.setText(chat.getMessage());
        }

        void fillReceiverView(Chat chat) {
            show_message.setText(chat.getMessage());
            user.setText(chat.getSender());
        }
    }
}
