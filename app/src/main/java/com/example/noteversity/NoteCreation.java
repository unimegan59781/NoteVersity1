package com.example.noteversity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NoteCreation extends AppCompatActivity {

    private DbHandler dbHandler; // imports db handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_creation);

        Intent intent = getIntent();
        String noteIMG = intent.getStringExtra("noteIMG");
        Boolean oldNote = intent.getBooleanExtra("previousNote", false);
        Log.d("NoteCreation", String.valueOf(oldNote));


        Draw note = new Draw(this); // creates new isntace of draw class so user can create note

        dbHandler = new DbHandler(NoteCreation.this); // links db handler to class and with variable dbHandler to call later


        ImageButton saveBut = (ImageButton) findViewById(R.id.newNoteBtn); // save button link

        RelativeLayout noteView = (RelativeLayout) findViewById(R.id.content); // create view that draw can be implemented on
         // links draw class for user to write on screen and see buttons
        if (oldNote) {
            byte[] byteIMG = noteIMG.getBytes(StandardCharsets.UTF_8);

            getScreen(byteIMG, noteView);
            //noteView.setBackground(backgroundIMG);

            //noteView.setBackground(notePng);
            //noteView.setBackgroundColor(Color.TRANSPARENT);
            noteView.addView(note);
        } else {
            noteView.addView(note);
        }

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "SAVED BUTTON CLICKED", Toast.LENGTH_LONG).show(); // test message

                byte[] byteIMG = saveScreen(noteView);
                note.clean();
                getScreen(byteIMG, noteView);
                //

                dbHandler.insertNotes(1, 2, "test171", String.valueOf(byteIMG)); //inserts into db

                //List<String> test = dbHandler.getNote(1); // gets note from db with note id 1
                //Log.d("NoteCreation", test.get(0)); // logcat test prove valid
                //startActivity(new Intent(NoteCreation.this, NotesPages.class));
            }
        });//

    }
    //  Functions that validates the note title fits within 0 - 16 characters

    public byte[] saveScreen(View noteView) {
        int viewWidth = noteView.getWidth();
        int viewHeight = noteView.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        noteView.draw(canvas);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void getScreen(byte[] byteArray, View noteView){
        // Set the byte array as the background of the other view
        Bitmap bitmapFromByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Drawable noteIMG = new BitmapDrawable(getResources(), bitmapFromByteArray);
        noteView.setBackground(noteIMG);
    }

    public static String checkNoteTitle(String[] title) {
        if (title.length == 0) {
            return "Please enter a title";
        } else if (title.length > 16) {
            return "Please keep your title to less then 32 characters";
        } else {
            return "Title is acceptable";
        }
    }
}