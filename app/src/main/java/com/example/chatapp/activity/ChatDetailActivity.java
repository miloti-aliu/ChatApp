package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatDetailActivity extends AppCompatActivity {

    @BindView(R.id.sendBtn)ImageButton sendBtn;
    @BindView(R.id.text_send)EditText text;
    @BindView(R.id.rvMessages)RecyclerView recyclerView;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private DatabaseReference ref;
    private String senderName;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");
        String roomName = intent.getStringExtra("roomName");

        getSupportActionBar().setTitle(roomName);

        bindAdapter();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("users");

        reference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);

        sendBtn.setOnClickListener(v -> {
            String msg = text.getText().toString();
            if(!msg.trim().equals("")){
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        senderName = dataSnapshot.child(firebaseUser.getUid()).child("displayName").getValue().toString();
                        sendMessage(firebaseUser.getUid(), roomId, Encrypt(msg,"TEST"), senderName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            else {
                Toast.makeText(this,"Write a message!", Toast.LENGTH_SHORT).show();
            }
            text.setText("");
        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessages(roomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {     }
        });
    }

    private void bindAdapter() {
        messageAdapter = new MessageAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
    }

    public String Encrypt (String text, String key)
    {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c < 0 || c > '`')
                continue;
            res +=  (char) ((c + key.charAt(j) - 2 * 0) % '`' + 0);
            j = ++j % key.length();
        }
        return res.toLowerCase();
    }

    private void sendMessage(String sender, String room, String msg, String senderName){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();



        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("room", room);
        hashMap.put("message", msg);
        hashMap.put("senderName", senderName);

        databaseReference.child("rooms").child(room).child("chats").push().setValue(hashMap);
    }

    private void readMessages(String roomId){
        reference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId).child("chats");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                messageAdapter.addChat(chat);
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
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
