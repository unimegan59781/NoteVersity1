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
}