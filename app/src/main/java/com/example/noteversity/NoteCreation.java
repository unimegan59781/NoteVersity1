package com.example.noteversity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NoteCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_creation);
        Draw note = new Draw(this);
        setContentView(note);
    }

    //  Functions that validates the note title fits within 0 - 16 characters
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