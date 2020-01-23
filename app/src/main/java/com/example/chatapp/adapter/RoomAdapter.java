package com.example.chatapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.Room;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    ArrayList<Room> rooms = new ArrayList<>();

    @NonNull
    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_room,parent,false);
        return new MyViewHolder(itemView);
    }

    public void addRoom(Room room){
        rooms.add(room);
        notifyItemInserted(rooms.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.MyViewHolder holder, int position) {
        holder.roomname.setText(rooms.get(position).getRoomname());
        holder.lastMsg.setText(rooms.get(position).getLastMsg());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView roomname;
        TextView lastMsg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomname = itemView.findViewById(R.id.room_name);
            lastMsg = itemView.findViewById(R.id.last_msg);
        }
    }
}
