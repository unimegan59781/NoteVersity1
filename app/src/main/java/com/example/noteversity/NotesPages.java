package com.example.noteversity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesPages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_page);
        FloatingActionButton createNoteButton = findViewById(R.id.newNoteBtn);
        ImageButton backButton = findViewById(R.id.backButton);

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesPages.this, NoteCreation.class));
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesPages.this, Folders.class));
            }
        });

    };


    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.homeButton) {
//            System.out.println("home");
//            return true;
//        }
//
////        else if (id==R.id.login)
////        {
////            // add your action here that you want
////        }
//
//
//        return super.onOptionsItemSelected(item);
//    }

