package com.example.noteversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Folders extends AppCompatActivity {
    private DbHandler dbHandler; // imports db handler

    public int userID; // sets userID that can be get from previous

    public android.content.Context cntx() {
        return getApplicationContext();
    }

    public static int DPtoPixels(android.content.Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    // checks validates name sends error popup toast message
    public boolean nameCheck(String title){
        String nameLookUp = dbHandler.searchFolder(title);
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


    // + button at bottom right that creates usbale button in grid view to create visual aid of a folder for user that crud can be applied to
    public AppCompatButton createFolder(String folderName) {
        AppCompatButton newFolder = new AppCompatButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));


        params.setMargins(DPtoPixels(cntx(), 3), DPtoPixels(cntx(), 3), DPtoPixels(cntx(), 3), DPtoPixels(cntx(), 6));
        newFolder.setLayoutParams(params);
        newFolder.setBackgroundResource(R.drawable.folder_view);
        newFolder.setText(folderName); // use will name function
        newFolder.setGravity(Gravity.START);
        newFolder.setGravity(Gravity.CENTER_VERTICAL);
        newFolder.setPadding(DPtoPixels(getApplicationContext(), 15), 0, 0, 0);
        return newFolder;

    }


    // adds new folder to view and to database - name check
    @SuppressLint("ClickableViewAccessibility")
    public void folderAdd(android.view.View view) {
        GridLayout folder = (GridLayout) findViewById(R.id.grid);
        String folderName = "change";
        EditText name = (EditText) findViewById(R.id.folderNameEdit);
        String title = name.getText().toString();
        if (nameCheck(title)){
            folderName = title;
            name.setText("");
        } else {
            return;
        }
        AppCompatButton newFolder = createFolder(folderName);
        folder.addView(newFolder);
        folderInteractions(newFolder, folder);
        dbHandler.insertFolder(userID, folderName); // inserts new folder into db folders table
        List<String> folderList = dbHandler.getFolder(folderName);
        int fID = Integer.parseInt(folderList.get(0));
        dbHandler.insertUFlink(userID, fID);

    }

    // gets folder from grid view folder searching using the naem of the folder
    public View getFolder(String folderName, GridLayout folder) { // function that loops the views in folder grid layout to find location of view with desired name
        for (int i = 0; i < folder.getChildCount(); i++) {
            View folderLocation = folder.getChildAt(i);
            if (folderLocation instanceof Button) { // itterates through folders in grid
                Button gridFolder = (Button) folderLocation; // casts to appcompatbutton (button) to see if folder exisits
                if (gridFolder.getText().toString().equals(folderName)) { // use name as unquie identifyer of view
                    return folderLocation; // returns view of folderName
                }
            }
        }
        return null; // folder doesn't exist (error checker - shouldn't occcur)
    }

    // deletes folder visually in grid and on db
    public void deleteFolder(View folderName, GridLayout folder, String name) { // delete folder from view
        folder.removeView(folderName); // removes view in grid layout folder with view thats be named
        dbHandler.deleteFolder(name);
    }

    // folder intercations to crud the folders in database
    @SuppressLint("ClickableViewAccessibility")
    public void folderInteractions(AppCompatButton newFolder, GridLayout folder) {
        View folderView = getFolder(newFolder.getText().toString(), folder); // uses get function to find view of the folder from grid layout
        String folderName = newFolder.getText().toString();

        newFolder.setOnTouchListener(new View.OnTouchListener() {

            // uses gesture detector to get more specific details on user grid intercation than clicking
            private final GestureDetector gestDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                // user swipe to left to delete folder
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

                // swipe left to link to db delete of folder
                public void onSwipeLeft() {
                    deleteFolder(folderView, folder, folderName);
                }

                // double tap to change name of folder
                @Override // double tap to edit name
                public boolean onDoubleTap(MotionEvent e) {

                    String newName;
                    EditText name = (EditText) findViewById(R.id.folderNameEdit);
                    String title = name.getText().toString();
                    if (nameCheck(title)){
                        newName = title;
                        name.setText("");
                    } else {
                        newName = folderName;
                    }
                    newFolder.setText(newName);

                    List<String> folderList = dbHandler.getFolder(folderName);
                    int folderID = Integer.parseInt(folderList.get(0));

                    dbHandler.changeFoldername(newName, folderID);

                    return super.onDoubleTap(e); // passes event as super to overide touch
                }

                // single tap takes user to notes page and loads notes from folder id sent through intent
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    List<String> folderList = dbHandler.getFolder(folderName);
                    int folderID = Integer.parseInt(folderList.get(0));

                    Intent intent = new Intent(Folders.this, NotesPages.class);
                    intent.putExtra("folderID", folderID);
                    intent.putExtra("userID", userID);
                    Log.d("Folder folder page", String.valueOf(folderID));
                    startActivity(intent);

                    return super.onSingleTapConfirmed(e);
                }

            });

            // on touch master to known folder interacted with on grid
            public boolean onTouch(View v, MotionEvent event) {
                gestDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    // nav bar to go through pages at bottom menu
    public void navBarController() {
        BottomNavigationView bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.homeButton);
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.notifButton) {

                    startActivity(new Intent(Folders.this, Notifications.class));
                    return true;
                }

                if (id == R.id.profileButton) {
                    startActivity(new Intent(Folders.this, Profile.class));
                    return true;
                }

                return false;
            }
        });
    }

    // on create when page loaded gets user intent from previous, and loads all folders from database or none if none
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);

        BottomNavigationView navBar = findViewById(R.id.bottomBar);
        navBar.setSelectedItemId(R.id.homeButton);
        dbHandler = new DbHandler(Folders.this);

        Intent intent = getIntent();
        int userID = intent.getIntExtra("userID", 0);

        GridLayout folder = (GridLayout) findViewById(R.id.grid);
        List<String> folderIDs = dbHandler.getUsersFolders(userID);
        if (folderIDs != null) {
            for (String id : folderIDs) {
                int fID = Integer.parseInt(id);
                String folderName = dbHandler.getFolderName(fID);
                AppCompatButton newFolder = createFolder(folderName);
                folder.addView(newFolder);
                folderInteractions(newFolder, folder);
            }
        }
        navBarController();
        System.out.println("onCreate");
    }
}
