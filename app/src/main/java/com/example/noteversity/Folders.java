package com.example.noteversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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

public class Folders extends AppCompatActivity {


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

    @SuppressLint("ClickableViewAccessibility")
    public void folderCRUD(android.view.View view){
        GridLayout folder = (GridLayout) findViewById(R.id.grid);
        AppCompatButton newFolder = new AppCompatButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL,1f),
                GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));


        params.setMargins(DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),6));
        newFolder.setLayoutParams(params);
        newFolder.setBackgroundResource(R.drawable.folder_view);
        newFolder.setText(nameFolder()); // use will name function
        newFolder.setGravity(Gravity.START);
        newFolder.setGravity(Gravity.CENTER_VERTICAL);
        newFolder.setPadding(DPtoPixels(getApplicationContext(),15),0,0,0);
        folder.addView(newFolder);

        newFolder.setOnTouchListener(new View.OnTouchListener() {
            View folderView = getFolder(newFolder.getText().toString(), folder); // uses get function to find view of the folder from grid layout
            private GestureDetector gestDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override // swipe left to delete folder
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float distanceX = e2.getX() - e1.getX(); // uses event points base code
                    float distanceY = e2.getY() - e1.getY();
                    if (Math.abs(distanceX) > Math.abs(distanceY) && // 100 is min threashold of speed + distance user swiped
                            Math.abs(distanceX) > 100 &&
                            Math.abs(velocityX) > 100) {
                        if (distanceX < 0) { // if negative swiped to left (>0 for right if needed)
                            deleteFolder(folderView, folder); // link to deleteFolder pass folder (grid layout) + selected folder
                        }
                    }
                    return super.onFling(e1, e2, velocityX, velocityY); // passes as super so overides touch
                }

                @Override // double tap to edit name
                public boolean onDoubleTap(MotionEvent e) {
                    //folderView.
                    return super.onDoubleTap(e); // passes event as super to overide touch
                }

            });

            @Override // link to selected folder
            public boolean onTouch(View v, MotionEvent event) {  // default touch in any way other than swip left + double tap therefore public
                folderView.setBackgroundColor(Color.BLUE);
                // TO DO LINK TO NOTES IN THIS FOLDER (NEW PAGE)
                gestDetector.onTouchEvent(event); // links to onTouch type gesture
                return true;
            }
        });
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
        return null; // folder doesn't exist
    }

    public void deleteFolder(View folderName, GridLayout folder) {
        folder.removeView(folderName); // removes view in grid layout folder with view thats be named
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);
        BottomNavigationView navBar = findViewById(R.id.bottomBar);
        navBar.setSelectedItemId(R.id.homeButton);
        //add a functino to query folders from database
    }

}


