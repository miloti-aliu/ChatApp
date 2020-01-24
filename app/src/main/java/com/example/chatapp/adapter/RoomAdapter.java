package com.example.chatapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.activity.ChatDetailActivity;
import com.example.chatapp.model.Room;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private List<Room> rooms;


    String theLastMessage;

    public RoomAdapter(List<Room> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.MyViewHolder holder, int position) {

        final Room room = rooms.get(position);

        holder.roomname.setText(rooms.get(position).getRoomname());

        lastMessage(room.getId(), holder.last_Msg);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChatDetailActivity.class);
            intent.putExtra("roomId", rooms.get(position).getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView roomname;
        TextView last_Msg;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomname = itemView.findViewById(R.id.room_name);
            last_Msg = itemView.findViewById(R.id.last_msg);
        }
    }

    private void lastMessage(String roomid, TextView last_msg) {

        theLastMessage = "default";
        Query query = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomid).child("chats").orderByKey().limitToLast(1);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                theLastMessage = dataSnapshot.child("message").getValue().toString();
                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
