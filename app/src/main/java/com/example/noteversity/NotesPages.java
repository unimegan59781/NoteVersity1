package com.example.noteversity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class NotesPages extends AppCompatActivity {

    private DbHandler dbHandler;

    //Shorthand for getApplicationContext()
    public android.content.Context cntx(){
        return getApplicationContext();
    }

    //Gets screen pixel density and uses it to turn dependent pixels into their relative pixel values for display
    public static int DPtoPixels(android.content.Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    @Override
    //Gets all notes from database, and creates/draws them on screen.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_page);
        FloatingActionButton createNoteButton = findViewById(R.id.newNoteBtn);
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton searchButton = findViewById(R.id.searchUser);

        dbHandler = new DbHandler(NotesPages.this);

        Intent intent = getIntent();
        int folderID = intent.getIntExtra("folderID", 0);
        int userID = intent.getIntExtra("userID", 0);

        Log.d("Folder get n pages", String.valueOf(folderID));

        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        List<String> allNotes = dbHandler.getFolderNotes(folderID);//Arrays.asList("dog", "cat");
        if (allNotes != null){
            for (String name : allNotes) {
                String noteIMG = dbHandler.getNoteImg(name);
                AppCompatButton newNote = createNote(name, noteIMG);
                grid.addView(newNote);
                noteInteractions(newNote, grid, folderID, userID);
            }
        } else {
            // help
        }


        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesPages.this, NoteCreation.class);
                intent.putExtra("folderID", folderID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesPages.this, Folders.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesPages.this, SearchUser.class);
                intent.putExtra("folderID", folderID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }});
    };

    //Configures how the user's inputs on a note get recieved, also links to database depending on action.
    public void noteInteractions(AppCompatButton note, GridLayout grid, int fID, int userID) {
        View noteView = getNote(note.getText().toString(), grid); // uses get function to find view of the folder from grid layout
        String noteName = note.getText().toString();
        noteView.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                            if (diffX < 0) {
                                onSwipeLeft();
                            }
                            return super.onFling(e1, e2, velocityX, velocityY);
                        }
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }

                public void onSwipeLeft() {
                    //noteView.setBackgroundColor(Color.RED);
                    deleteNote(noteView, grid, noteName);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    //noteView.setBackgroundColor(Color.BLUE);
                    // TO GO TO VIEW
                    String noteIMG = dbHandler.getNoteImg(noteName);
                    Intent intent = new Intent(NotesPages.this, NoteCreation.class);
                    intent.putExtra("noteIMG", noteIMG);// key is used to get value in Second Activiy
                    intent.putExtra("previousNote", true);
                    intent.putExtra("folderID", fID);
                    intent.putExtra("userID", userID);
                    Log.d("Folder send note page", String.valueOf(fID));
                    startActivity(intent);

                    return super.onSingleTapConfirmed(e);
                }

            });

            public boolean onTouch(View v, MotionEvent event) {
                gestDetector.onTouchEvent(event);
                return true;
            }
        });

    }
    // function that loops the views in folder grid layout to find location of view with desired name
    public View getNote(String noteName, GridLayout grid) {
        for(int i = 0; i < grid.getChildCount(); i++) {
            View noteView = grid.getChildAt(i);
            if(noteView instanceof Button) { // itterates through folders in grid
                Button gridNote = (Button) noteView; // casts to appcompatbutton (button) to see if folder exisits
                if(gridNote.getText().toString().equals(noteName)) { // use name as unquie identifyer of view
                    return noteView; // returns view of folderName
                }
            }
        }
        return null; // folder doesn't exist (error checker - shouldn't occcur)
    }

    //Function to delete a note and it's corresponding view on screen
    public void deleteNote(View noteView, GridLayout grid, String name) {
        grid.removeView(noteView); // removes view in grid layout folder with view thats be named
        dbHandler.deleteNote(name);

        };

    //Creates a new button, sets the visual content and respective margin/padding, and returns the note to be added to the screen
    public AppCompatButton createNote(String noteName, String noteIMG){
        AppCompatButton newNote = new AppCompatButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL,1f),
                GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));

        params.setMargins(DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),6));
        newNote.setLayoutParams(params);
        newNote.setBackgroundResource(R.drawable.folder_view); /// TO DO SET PNG
        newNote.setText(noteName); // use will name function
        //newNote.set
        newNote.setGravity(Gravity.START);
        newNote.setGravity(Gravity.CENTER_VERTICAL);
        newNote.setPadding(DPtoPixels(getApplicationContext(),15),0,0,0);
        return newNote;
    }
}



