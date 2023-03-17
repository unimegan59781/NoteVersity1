package com.example.noteversity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.os.Bundle;
import android.view.Gravity;
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


//    public String folderTitle(){
//
//    }
    public void addFolder(android.view.View view){
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        AppCompatButton newFolder = new AppCompatButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL,1f),
                GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));


        params.setMargins(DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),6));
        newFolder.setLayoutParams(params);
        newFolder.setBackgroundResource(R.drawable.folder_view);
        newFolder.setText("New Folder");
        newFolder.setGravity(Gravity.START);
        newFolder.setGravity(Gravity.CENTER_VERTICAL);
        newFolder.setPadding(DPtoPixels(getApplicationContext(),15),0,0,0);
        grid.addView(newFolder);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);
        BottomNavigationView navBar = findViewById(R.id.bottomBar);
        navBar.setSelectedItemId(R.id.homeButton);
        //add a functino to query folders from database
    }
}
