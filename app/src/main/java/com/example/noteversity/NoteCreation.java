package com.example.noteversity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NoteCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_creation);

        Draw note = new Draw(this);


        ImageButton saveBut = (ImageButton) findViewById(R.id.newNoteBtn); // save button link

        RelativeLayout noteView = (RelativeLayout) findViewById(R.id.content); // create view that draw can be implemented on
        noteView.addView(note); // links draw class for user to write on screen and see buttons

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"SAVED BUTTON CLICKED", Toast.LENGTH_LONG).show(); // test message for clicked save but
                note.clean();
            }
        });

        //bitmap.compress(Bitmap.CompressFormat.PNG, quality, outStream);

    }

    public Bitmap saveScreen(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888); // gets screen dimensions + pixels
        Canvas canvas = new Canvas(bitmap); // canvas to save screen state
        view.draw(canvas);
        return bitmap;
    }



}