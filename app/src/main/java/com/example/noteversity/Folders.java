package com.example.noteversity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Folders extends AppCompatActivity {
    private DbHandler dbHandler; // imports db handler


    public android.content.Context cntx(){
        return getApplicationContext();
    }
    public static int DPtoPixels(android.content.Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public String nameFolder (){ // function to name folder + name validation //REUSE NOTES
        EditText name = (EditText) findViewById(R.id.folderNameEdit);
        String title = name.getText().toString();

//        if (title.length() == 0) {
//            return "Please enter a title";
//        } else if (title.length() > 32) {
//            return "Please keep your title to less then 32 characters";
//        } else {
//            return "Title is acceptable";
//        }
        name.setText(""); // clears the text edit
        return title; // returns title of folder to set folder name in folderCRUD
    }

    public AppCompatButton createFolder(String folderName){
        AppCompatButton newFolder = new AppCompatButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL,1f),
                GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));


        params.setMargins(DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),6));
        newFolder.setLayoutParams(params);
        newFolder.setBackgroundResource(R.drawable.folder_view);
        newFolder.setText(folderName); // use will name function
        newFolder.setGravity(Gravity.START);
        newFolder.setGravity(Gravity.CENTER_VERTICAL);
        newFolder.setPadding(DPtoPixels(getApplicationContext(),15),0,0,0);
        return newFolder;

    }


    @SuppressLint("ClickableViewAccessibility")
    public void folderAdd(android.view.View view){
        GridLayout folder = (GridLayout) findViewById(R.id.grid);
        String folderName = nameFolder();
        AppCompatButton newFolder = createFolder(folderName);
        folder.addView(newFolder);
        folderInteractions(newFolder, folder);
        dbHandler.insertFolder(1, newFolder.getText().toString()); // inserts new folder into db folders table

     }

    public View getFolder(String folderName, GridLayout folder) { // function that loops the views in folder grid layout to find location of view with desired name
        for(int i = 0; i < folder.getChildCount(); i++) {
            View folderLocation = folder.getChildAt(i);
            if(folderLocation instanceof Button) { // itterates through folders in grid
                Button gridFolder = (Button) folderLocation; // casts to appcompatbutton (button) to see if folder exisits
                if(gridFolder.getText().toString().equals(folderName)) { // use name as unquie identifyer of view
                    return folderLocation; // returns view of folderName
                }
            }
        }
        return null; // folder doesn't exist (error checker - shouldn't occcur)
    }

    public void deleteFolder(View folderName, GridLayout folder) { // delete folder from view
        folder.removeView(folderName); // removes view in grid layout folder with view thats be named
        // TO ADD DB DELETE CODE
    };
    @SuppressLint("ClickableViewAccessibility")
    public void folderInteractions(AppCompatButton newFolder, GridLayout folder){
        View folderView = getFolder(newFolder.getText().toString(), folder); // uses get function to find view of the folder from grid layout
        //int folderID = dbHandler.getFolderID(newFolder.getText().toString());
        newFolder.setOnTouchListener(new View.OnTouchListener() {

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
                    deleteFolder(folderView, folder);
                    //dbHandler.deleteFolder(folderID);
                }

                @Override // double tap to edit name
                public boolean onDoubleTap(MotionEvent e) {
                    folderView.setBackgroundColor(Color.YELLOW);
                    // TO CHNAGE FOLDER NAME
                    return super.onDoubleTap(e); // passes event as super to overide touch
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    folderView.setBackgroundColor(Color.BLUE);
                    // TO GO TO VIEW
                    return super.onSingleTapConfirmed(e);
                }

            });

            public boolean onTouch(View v, MotionEvent event) {
                gestDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);
        BottomNavigationView navBar = findViewById(R.id.bottomBar);
        navBar.setSelectedItemId(R.id.homeButton);
        dbHandler = new DbHandler(Folders.this);
        //add a functino to query folders from database
        GridLayout folder = (GridLayout) findViewById(R.id.grid);
        List<String> allFolderNames = dbHandler.getAllFolders(1);
        for (String name : allFolderNames) {
            AppCompatButton newFolder = createFolder(name);
            folder.addView(newFolder);
            folderInteractions(newFolder, folder);
        }



    }

}


