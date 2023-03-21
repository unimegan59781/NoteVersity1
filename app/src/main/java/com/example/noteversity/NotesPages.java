package com.example.noteversity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesPages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_page);
        FloatingActionButton createNoteButton = findViewById(R.id.newNoteBtn);

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(getApplicationContext(), NoteCreation.class);
                startActivity(change);

            }
    });


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
}

