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
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class Profile extends AppCompatActivity {

    private DbHandler dbHandler; // imports db handler

    private int userID;

    public boolean checkName(String title) {
        String nameLookUp = dbHandler.searchUser(title);
        if (title.length() == 0) {
            Toast.makeText(getApplicationContext(),"Nothing typed, Please enter a title", Toast.LENGTH_LONG).show();
            return false;
        } else if (title.length() > 32) {
            Toast.makeText(getApplicationContext(),"Please keep your title to less then 32 characters", Toast.LENGTH_LONG).show();
            return false;
        } else if (nameLookUp != null){
            Toast.makeText(getApplicationContext(),"Sorry that Folder already exists, Please try another name", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    public void setUsernameEmail(int userID){
        dbHandler = new DbHandler(Profile.this);
        List<String> userInfo = dbHandler.getUser(userID);

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
    public void changeUsername(String newUsername, int userID){
        dbHandler = new DbHandler(Profile.this);
        if (checkName(newUsername)){
            dbHandler.changeUserName(newUsername,userID);
            setUsernameEmail(userID);
        }
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
                changeUsername(newUsername, userID);
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

        TextView infoText = findViewById(R.id.infoText);
        infoText.setText("Thank you for using NoteVersity - to view items click, to delete notes + folders swipe left, to change a name double tap, B button changes the background. Enjoy!");

        AppCompatButton changeButton = findViewById(R.id.changeUsername);
        changeButton.setOnClickListener(view -> showDialog());

        Intent intent = getIntent();
        int userID = intent.getIntExtra("userID", 0);
        setUsernameEmail(userID);

    }
}