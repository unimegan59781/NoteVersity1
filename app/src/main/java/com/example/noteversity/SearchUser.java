package com.example.noteversity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SearchUser extends AppCompatActivity {

    private DbHandler dbHandler; // links dbHandler

    // displays all users on page and links buttons when intally uploaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton searchButton = findViewById(R.id.searchUser);

        dbHandler = new DbHandler(SearchUser.this);

        int folderID = getIntent().getIntExtra("folderID", 0);
        int userID = getIntent().getIntExtra("userID", 0);

        // adds all userss into display view
        List<String> usersIDs = dbHandler.getUsersInFolder(folderID);
        String username = "";
        if (usersIDs != null) {
            for (String id : usersIDs) {
                int fID = Integer.parseInt(id);
                username = username + "\n" + dbHandler.getUserName(fID);
            }
        }

        TextView displayView = findViewById(R.id.displayUsers);
        displayView.setText(username);

        // lets user go back to notes page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchUser.this, NotesPages.class));
            }
        });

        // saerch button links database and allows user to add new user from username typed in edit text
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText titleName = findViewById(R.id.noteTitle);
                String userName = titleName.getText().toString();

                int searchID = Integer.parseInt(dbHandler.searchUser(userName));

                dbHandler.insertUFlink(searchID, folderID);

                dbHandler.insertNotification(userID,searchID,folderID);

                Intent intent = new Intent(SearchUser.this, NotesPages.class);
                intent.putExtra("folderID", folderID);
                intent.putExtra("userID", userID);
                startActivity(intent);

                // success message to say user added
                Toast.makeText(getApplicationContext(),"User folder request sent", Toast.LENGTH_LONG).show();

            }});
    };

}
