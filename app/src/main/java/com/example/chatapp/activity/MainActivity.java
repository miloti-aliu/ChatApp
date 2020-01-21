package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chatapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager =findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setTitle("Chat App");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            firebaseAuth.signOut();
            startActivity(new Intent(this, com.example.chatapp.StartActivity.class));
            finish();
            return true;
        }
        return false;



    }
    private void setupViewPager(ViewPager viewPager){
        com.example.chatapp.ViewPagerAdapter adapter = new com.example.chatapp.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(com.example.chatapp.UserFragment.getInstance(),"Contacts");
        adapter.addFragment(com.example.chatapp.ChatFragment.getInstance(),"Chats");
        viewPager.setAdapter(adapter);
    }
}
