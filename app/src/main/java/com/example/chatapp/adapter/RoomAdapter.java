package com.example.chatapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.activity.ChatDetailActivity;
import com.example.chatapp.activity.MainActivity;
import com.example.chatapp.model.Room;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private List<Room> rooms = new ArrayList<>();
    private String lastMessage;

    public RoomAdapter(){

    }

    @NonNull
    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_room,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.MyViewHolder holder, int position) {
        holder.fillView(rooms.get(position));
    }

    public void addRoom(Room room){
        rooms.add(room);
        notifyItemInserted(getItemCount());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView roomname;
        TextView lastMsg;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomname = itemView.findViewById(R.id.room_name);
            lastMsg = itemView.findViewById(R.id.last_msg);
        }

        void fillView(Room room) {
            roomname.setText(room.getRoomname());
            lastMsg.setText(room.getLastMsg());
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ChatDetailActivity.class);
                intent.putExtra("roomId",room.getId());
                intent.putExtra("roomName",room.getRoomname());
                v.getContext().startActivity(intent);
            });
        }
    }
}
