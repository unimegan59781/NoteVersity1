package com.example.noteversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.GridLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Folders extends AppCompatActivity {

    public android.content.Context cntx() {
        return getApplicationContext();
    }

    public static int DPtoPixels(android.content.Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }


    //    public String folderTitle(){
//
//    }
    public void addFolder(android.view.View view) {
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        AppCompatButton newFolder = new AppCompatButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));


        params.setMargins(DPtoPixels(cntx(), 3), DPtoPixels(cntx(), 3), DPtoPixels(cntx(), 3), DPtoPixels(cntx(), 6));
        newFolder.setLayoutParams(params);
        newFolder.setBackgroundResource(R.drawable.folder_view);
        newFolder.setText("New Folder");
        newFolder.setGravity(Gravity.START);
        newFolder.setGravity(Gravity.CENTER_VERTICAL);
        newFolder.setPadding(DPtoPixels(getApplicationContext(), 15), 0, 0, 0);
        grid.addView(newFolder);
    }

    public void navBarController(){
        BottomNavigationView bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.homeButton);
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.notifButton) {
                    Intent intent = new Intent(Folders.this, Notifications.class);
                    startActivity(intent);
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
        navBarController();

    }

    //  Functions that validates the folder title fits within 0 - 32 characters
    public static String checkFolderTitle(String[] title) {
        if (title.length == 0) {
            return "Please enter a title";
        } else if (title.length > 32) {
            return "Please keep your title to less then 32 characters";
        } else {
            return "Title is acceptable";
        }
    }
}
