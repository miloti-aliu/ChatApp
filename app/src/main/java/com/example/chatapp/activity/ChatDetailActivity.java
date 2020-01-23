package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.adapter.MessageAdapter;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatDetailActivity extends AppCompatActivity {

    @BindView(R.id.username)TextView username;
    @BindView(R.id.sendBtn)ImageButton sendBtn;
    @BindView(R.id.text_send)EditText text;
    @BindView(R.id.rvMessages)RecyclerView recyclerView;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private MessageAdapter messageAdapter;
    private List<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");
        String roomName = intent.getStringExtra("roomName");

        getSupportActionBar().setTitle(roomName);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);

        sendBtn.setOnClickListener(v -> {
            String msg = text.getText().toString();
            if(!msg.trim().equals("")){
                sendMessage(firebaseUser.getUid(), roomId, msg);
            }
            else {
                Toast.makeText(this,"Write a message!", Toast.LENGTH_SHORT).show();
            }
            text.setText("");
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                readMessages(roomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String room, String msg){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("room", room);
        hashMap.put("message", msg);

        databaseReference.child("rooms").child(room).child("chats").push().setValue(hashMap);
    }

    private void readMessages(String roomId){
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId).child("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    chats.add(chat);
                }
                messageAdapter = new MessageAdapter(ChatDetailActivity.this, chats);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
