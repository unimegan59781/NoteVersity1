package com.example.noteversity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class NoteCreation extends AppCompatActivity {

    private DbHandler dbHandler; // imports db handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_creation);

        Draw note = new Draw(this); // creates new isntace of draw class so user can create note

        dbHandler = new DbHandler(NoteCreation.this); // links db handler to class and with variable dbHandler to call later


        ImageButton saveBut = (ImageButton) findViewById(R.id.newNoteBtn); // save button link

        RelativeLayout noteView = (RelativeLayout) findViewById(R.id.content); // create view that draw can be implemented on
        noteView.addView(note); // links draw class for user to write on screen and see buttons

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"SAVED BUTTON CLICKED", Toast.LENGTH_LONG).show(); // test message

                Bitmap noteBitmap = saveScreen.toBitmap(noteView); // turns view to bitmap - just draw view so buttons don't save in image
                byte[] noteByte = saveScreen.toBytes(noteBitmap); // turns bitmap to Byte to save in db

                //Bitmap ByteToBitmap = saveScreen.toImage(noteByte); // turns Byte to bitmap frob db so can set drawbale in notePng
                //BitmapDrawable notePng = new BitmapDrawable(getResources(), ByteToBitmap);
                //noteView.setBackground(notePng);

                note.clean();

                dbHandler.insertNotes(1, 1, "test", String.valueOf(noteByte)); //inserts into db

                Toast.makeText(getApplicationContext(), "save works", Toast.LENGTH_LONG).show(); // test message

            }
        });

        //

    }

    public class saveScreen {

        // draw view to bitmap
        public static Bitmap toBitmap(View view) {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888); // gets screen dimensions + pixels
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas); // saves screen state so when saving doesn't crash when user interacts with screen
            return bitmap;
        }


        // bitmap to byte array for DB save
        public static byte[] toBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream); // compress to png and links output stream so can save to db
            return stream.toByteArray();
        }

        // byte to bitmap
        public static Bitmap toImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length); // takes byte and turns to bitmap
        }
    }



}