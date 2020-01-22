package com.example.chatapp.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapp.activity.ChatDetailActivity;
import com.example.chatapp.R;
import com.example.chatapp.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    static UserFragment instance;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    public static UserFragment getInstance() {
        if (instance == null) {
            instance = new UserFragment();
        }
        return instance;
    }


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = v.findViewById(R.id.user_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inflate the layout for this fragment
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseRecyclerOptions<User> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<User>().setQuery(databaseReference.orderByKey(), User.class).build();

        adapter = new FirebaseRecyclerAdapter<User, ViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
                return new ViewHolder(view1);
            }

            @NonNull
            @Override
            public User getItem(int position) {
                return super.getItem(position);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final User user) {
                holder.displayName.setText(user.getUsername());

                holder.itemView.setOnClickListener(v1 -> {
                    Intent intent = new Intent(getContext(), ChatDetailActivity.class);
                    intent.putExtra("USER_OBJECT", user);
                    startActivity(intent);
                });
            }
        };

        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView displayName;

        ViewHolder(View itemView) {
            super(itemView);

            displayName = itemView.findViewById(R.id.txt_username);
        }
    }

}
