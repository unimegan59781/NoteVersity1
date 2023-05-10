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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteCreation extends AppCompatActivity {

    private DbHandler dbHandler; // imports db handler //// here
    private int click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_creation);

        Intent intent = getIntent();
        String dbIMG = intent.getStringExtra("noteIMG");
        Boolean oldNote = intent.getBooleanExtra("previousNote", false);
        int folderID = intent.getIntExtra("folderID", 0);
        int userID = intent.getIntExtra("userID", 0);


        Draw note = new Draw(this); // creates new isntace of draw class so user can create note

        dbHandler = new DbHandler(NoteCreation.this); // here // links db handler to class and with variable dbHandler to call later

        EditText titleName = findViewById(R.id.noteTitle);
        String title = titleName.getText().toString();

        ImageButton saveBut = (ImageButton) findViewById(R.id.newNoteBtn); // save button link
        Button changeButton = findViewById(R.id.backgroundButton);

        RelativeLayout noteView = (RelativeLayout) findViewById(R.id.content); // create view that draw can be implemented on
         // links draw class for user to write on screen and see buttons
        if (oldNote) {
            byte[] byteIMG = Base64.decode(dbIMG, Base64.DEFAULT);

            Bitmap bitmapIMG = getScreen(byteIMG);
            noteView.setBackground(new BitmapDrawable(getResources(), bitmapIMG));
            noteView.addView(note);

            changeButton.setVisibility(View.INVISIBLE);

        } else {
            noteView.addView(note);
            noteView.setBackground(getDrawable(R.drawable.cream_background));

            changeButton.setVisibility(View.VISIBLE);
        }

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> backgrounds = Arrays.asList(R.drawable.blue_background, R.drawable.lined_background, R.drawable.square_background, R.drawable.cream_background);

                if (click < 3) {
                    click++;
                } else {
                    click = 0;
                }
                noteView.setBackground(getDrawable(backgrounds.get(click)));

            }
        });

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText titleName = findViewById(R.id.noteTitle);
                String title = titleName.getText().toString();
                if (checkNoteTitle(title)){
                    titleName.setText("");
                } else {
                    return;
                }

                // TO DO CHECK ERROR

                byte[] byteIMG = saveScreen(noteView); // get byte array of view
                String byteStingIMG = Base64.encodeToString(byteIMG, Base64.DEFAULT); // set to string to save in db

                note.clean();
                dbHandler.insertNotes(userID, folderID, title, byteStingIMG); //inserts into db // here

                Intent intent = new Intent(NoteCreation.this, NotesPages.class);
                intent.putExtra("folderID", folderID);// key is used to get value in Second Activiy
                Log.d("Folder send cteation", String.valueOf(folderID));
                startActivity(intent);
            }

        });//
    }

    public boolean checkNoteTitle(String title) {
        String nameLookUp = dbHandler.searchNote(title);
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