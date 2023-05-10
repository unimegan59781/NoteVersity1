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
    
  public int userID;
    
    //Shorthand function for getApplicationContext()
    public android.content.Context cntx() {
        return getApplicationContext();
    }

    //Gets current screen pixel density, and uses it to convert dependent pixel values into pixels to be used within the layout
    public static int DPtoPixels(android.content.Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public String nameFolder() { // function to name folder + name validation //REUSE NOTES
        EditText name = (EditText) findViewById(R.id.folderNameEdit);
        String title = name.getText().toString();

        int valid = 0;

        name.setText("");
        return title;
    }

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


    //Creates a folder using gridLayout, sets the appropriate margin and paddings, and returns it to be added to the screen.
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

    public void deleteFolder(View folderName, GridLayout folder, String name) { // delete folder from view
        folder.removeView(folderName); // removes view in grid layout folder with view thats be named
        dbHandler.deleteFolder(name);
        // TO ADD DB DELETE CODE
    }

    ;

    @SuppressLint("ClickableViewAccessibility")
    public void folderInteractions(AppCompatButton newFolder, GridLayout folder) {
        View folderView = getFolder(newFolder.getText().toString(), folder); // uses get function to find view of the folder from grid layout
        String folderName = newFolder.getText().toString();

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
                    deleteFolder(folderView, folder, folderName);
                }

                @Override // double tap to edit name
                public boolean onDoubleTap(MotionEvent e) {

                    String name = nameFolder();
                    newFolder.setText(name);

                    List<String> folderList = dbHandler.getFolder(folderName);
                    int folderID = Integer.parseInt(folderList.get(0));
                    dbHandler.changeFoldername(name, folderID);

                    return super.onDoubleTap(e); // passes event as super to overide touch
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    //folderView.setBackgroundColor(Color.BLUE);
                    //startActivity(new Intent(Folders.this, NotesPages.class));
                    List<String> folderList = dbHandler.getFolder(folderName);
                    int folderID = Integer.parseInt(folderList.get(0));

                    Intent intent = new Intent(Folders.this, NotesPages.class);
                    intent.putExtra("folderID", folderID);
                    intent.putExtra("userID", userID);
                    Log.d("Folder folder page", String.valueOf(folderID));
                    startActivity(intent);

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
