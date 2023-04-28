package com.example.noteversity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NoteCreation extends AppCompatActivity {

    private DbHandler dbHandler; // imports db handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_creation);

        Intent intent = getIntent();
        String dbIMG = intent.getStringExtra("noteIMG");
        Boolean oldNote = intent.getBooleanExtra("previousNote", false);
        int folderID = getIntent().getIntExtra("folderID", 0);
        Log.d("Folder get creation page", String.valueOf(folderID));


        Draw note = new Draw(this); // creates new isntace of draw class so user can create note

        dbHandler = new DbHandler(NoteCreation.this); // links db handler to class and with variable dbHandler to call later

        EditText titleName = findViewById(R.id.noteTitle);
        String title = titleName.getText().toString();
        titleName.setText("New Note");

        ImageButton saveBut = (ImageButton) findViewById(R.id.newNoteBtn); // save button link

        RelativeLayout noteView = (RelativeLayout) findViewById(R.id.content); // create view that draw can be implemented on
         // links draw class for user to write on screen and see buttons
        if (oldNote) {
            byte[] byteIMG = Base64.decode(dbIMG, Base64.DEFAULT);

            Bitmap bitmapIMG = getScreen(byteIMG);
            noteView.setBackground(new BitmapDrawable(getResources(), bitmapIMG));

            noteView.addView(note);
        } else {
            noteView.addView(note);
        }

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"SAVED BUTTON CLICKED", Toast.LENGTH_LONG).show(); // test message

                EditText titleName = findViewById(R.id.noteTitle);
                String title = titleName.getText().toString();
                //String name = checkNoteTitle(title);

                // TO DO CHECK ERROR

                byte[] byteIMG = saveScreen(noteView); // get byte array of view
                String byteStingIMG = Base64.encodeToString(byteIMG, Base64.DEFAULT); // set to string to save in db

                note.clean();
                dbHandler.insertNotes(1, folderID, title, byteStingIMG); //inserts into db

                Intent intent = new Intent(NoteCreation.this, NotesPages.class);
                intent.putExtra("folderID", folderID);// key is used to get value in Second Activiy
                Log.d("Folder send cteation", String.valueOf(folderID));
                startActivity(intent);
            }

        });//

    }

    public String checkNoteTitle(String title) {
        String nameLookUp = dbHandler.searchNote(title);
        if (title.length() == 0) {
            return "Please enter a title";
        } else if (title.length() > 16) {
            return "Please keep your title to less then 32 characters";
        } else if (nameLookUp != null) {
            return "Please keep your title to less then 32 characters";
        } else {
            return title;
        }
    }

    public byte[] saveScreen(View noteView) { // saves state of screen to bitmap to save as byte for db
        Bitmap bitmapIMG = Bitmap.createBitmap(noteView.getWidth(), noteView.getHeight(), Bitmap.Config.ARGB_8888); // creates bitmap from view
        Canvas canvas = new Canvas(bitmapIMG); // sets as canvas
        noteView.draw(canvas); // adds to view draw

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // sets output stream for future use
        bitmapIMG.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // compress to save
        byte[] byteIMG = outputStream.toByteArray(); // turn to saveable byte[]

        return byteIMG;
    }

    public Bitmap getScreen(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length); // turns byte[] to bitmap
        return bitmap;
    }
    
    //  Functions that validates the note title fits within 0 - 16 characters
}