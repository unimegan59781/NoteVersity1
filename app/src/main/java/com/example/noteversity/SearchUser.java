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
import java.util.List;

public class SearchUser extends AppCompatActivity {

    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton searchButton = findViewById(R.id.searchUser);

        dbHandler = new DbHandler(SearchUser.this);

        int folderID = getIntent().getIntExtra("folderID", 0);
        int userID = getIntent().getIntExtra("userID", 0);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchUser.this, NotesPages.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText titleName = findViewById(R.id.noteTitle);
                String userName = titleName.getText().toString();

                int searchID = Integer.parseInt(dbHandler.searchUser(userName));

                dbHandler.insertUFlink(searchID, folderID);

                // link notifications HENRY

                Intent intent = new Intent(SearchUser.this, NotesPages.class);
                intent.putExtra("folderID", folderID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }});

    };

}
