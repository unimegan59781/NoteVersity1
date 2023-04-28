package com.example.noteversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class Profile extends AppCompatActivity {

    private DbHandler dbHandler; // imports db handler //// here
 // here // links db handler to class and with variable dbHandler to call later


    public void setUsernameEmail(){
        dbHandler = new DbHandler(Profile.this);
        List<String> userInfo = dbHandler.getUser(1);

        TextView username = findViewById(R.id.usernameTV);
        username.setText(userInfo.get(2));

        TextView email = findViewById(R.id.emailTV);
        email.setText(userInfo.get(1));


    }

    public void navBarController(){
        BottomNavigationView bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.profileButton);
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.notifButton) {
                    Intent intent = new Intent(Profile.this, Notifications.class);
                    startActivity(intent);
                    return true;
                }

                if (id == R.id.homeButton) {
                    Intent intent = new Intent(Profile.this, Folders.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        navBarController();
        dbHandler = new DbHandler(Profile.this);
        setUsernameEmail();

    }
}