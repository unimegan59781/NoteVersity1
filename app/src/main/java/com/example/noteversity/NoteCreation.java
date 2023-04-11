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
        Log.d("NoteCreation", String.valueOf(oldNote));


        Draw note = new Draw(this); // creates new isntace of draw class so user can create note

        dbHandler = new DbHandler(NoteCreation.this); // links db handler to class and with variable dbHandler to call later


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
                Toast.makeText(getApplicationContext(), "SAVED BUTTON CLICKED", Toast.LENGTH_LONG).show(); // test message

                byte[] byteIMG = saveScreen(noteView);
                String strIMG = Base64.encodeToString(byteIMG, Base64.DEFAULT);

                Log.d("NoteCreation", "byteIMG contents: " + Arrays.toString(byteIMG));

                note.clean();
                //

                dbHandler.insertNotes(1, 2, "pleasee", strIMG); //inserts into db
                startActivity(new Intent(NoteCreation.this, NotesPages.class));
            }
        });//

    }
    //  Functions that validates the note title fits within 0 - 16 characters

    public byte[] saveScreen(View noteView) {
        Bitmap bitmapX = Bitmap.createBitmap(noteView.getWidth(), noteView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapX);
        noteView.draw(canvas);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmapX.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        return byteArray;
    }

    public Bitmap getScreen(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
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