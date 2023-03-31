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

//
//newFolder.setLayoutParams(params);
//        newFolder.setBackgroundResource(R.drawable.folder_view);
//        newFolder.setText(folderName); // use will name function
//        newFolder.setGravity(Gravity.START);
//        newFolder.setGravity(Gravity.CENTER_VERTICAL);
//        newFolder.setPadding(DPtoPixels(getApplicationContext(),15),0,0,0);
//        return newFolder;
//
//        }
//
//
//@SuppressLint("ClickableViewAccessibility")
//public void folderAdd(android.view.View view){
//        GridLayout folder = (GridLayout) findViewById(R.id.grid);
//        String folderName = nameFolder();
//        AppCompatButton newFolder = createFolder(folderName);
//        folder.addView(newFolder);
//        folderInteractions(newFolder, folder);
//        dbHandler.insertFolder(1, newFolder.getText().toString()); // inserts new folder into db folders table
//
//        }
//
//public View getFolder(String folderName, GridLayout folder) { // function that loops the views in folder grid layout to find location of view with desired name
//        for(int i = 0; i < folder.getChildCount(); i++) {
//        View folderLocation = folder.getChildAt(i);
//        if(folderLocation instanceof Button) { // itterates through folders in grid
//        Button gridFolder = (Button) folderLocation; // casts to appcompatbutton (button) to see if folder exisits
//        if(gridFolder.getText().toString().equals(folderName)) { // use name as unquie identifyer of view
//        return folderLocation; // returns view of folderName
//        }
//        }
//        }
//        return null; // folder doesn't exist (error checker - shouldn't occcur)
//        }
//
//public void deleteFolder(View folderName, GridLayout folder, String name) { // delete folder from view
//        folder.removeView(folderName); // removes view in grid layout folder with view thats be named
//        dbHandler.deleteFolder(name);
//        // TO ADD DB DELETE CODE
//        };





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

