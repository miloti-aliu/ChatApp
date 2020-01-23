package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatapp.R;

import com.example.chatapp.adapter.RoomAdapter;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.add)
    Button add;
    @BindView(R.id.add_room_name)
    EditText roomName;
    @BindView(R.id.room_recycler)
    RecyclerView recyclerView;

    private List<Room> roomList;
    private List<String> rooms;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Chat App");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        rooms = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("rooms");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    rooms.add(room.getRoomname());
                }
                readRooms();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add.setOnClickListener(v -> {

            UUID uuid = UUID.randomUUID();
            String roomId = String.valueOf(uuid);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);

            HashMap<String, String> roomMap = new HashMap<>();
            roomMap.put("id", roomId);
            roomMap.put("roomname", roomName.getText().toString());

            reference.setValue(roomMap);
        });
    }

    private void readRooms() {
        roomList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("rooms");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    roomList.add(room);
                }
                roomAdapter = new RoomAdapter(roomList);
                recyclerView.setAdapter(roomAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, StartActivity.class));
            finish();
            return true;
        }
        return false;
    }
}
