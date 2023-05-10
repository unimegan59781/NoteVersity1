package com.example.noteversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class Profile extends AppCompatActivity {

    private DbHandler dbHandler; // imports db handler //// here
 // here // links db handler to class and with variable dbHandler to call later

//Gets the currently logged in user's info using dbHandler, and changes the TextViews on the profile screen
    public void setUsernameEmail(){
        dbHandler = new DbHandler(Profile.this);
        List<String> userInfo = dbHandler.getUser(1); //getting data

        TextView username = findViewById(R.id.usernameTV);
        username.setText(userInfo.get(2));

        TextView email = findViewById(R.id.emailTV);
        email.setText(userInfo.get(1));


    }

    //Adds functionality to buttons on the bottom navigation bar by switching to the correct intent on item selected
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

    //uses dbHandler.changeUserName with the new username parameter passed to it by showDialog
    public void changeUsername(String newUsername){
        dbHandler = new DbHandler(Profile.this);
        dbHandler.changeUserName(newUsername,1);
        setUsernameEmail();

    }

    //Creates a new dialog using the change_username_dialog layout resource
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_username_dialog, null);
        builder.setView(dialogView); //adding the layout to the dialog

        EditText newUsernameText = dialogView.findViewById(R.id.new_username);
        builder.setTitle("Change Username");

        //setting save button
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = newUsernameText.getText().toString();
                changeUsername(newUsername);
                //put change here
            }
        });
        //setting cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //void
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show(); //showing the dialog



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        navBarController();
        AppCompatButton changeButton = findViewById(R.id.changeUsername);
        changeButton.setOnClickListener(view -> showDialog());

        setUsernameEmail();

    }
}