package com.example.noteversity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Notifications extends AppCompatActivity {

    private int folderID;
    private int userID;

    public void navBarController(){
        BottomNavigationView bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.notifButton);
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.homeButton){
                    Intent intent = new Intent(Notifications.this, Folders.class);
                    startActivity(intent);
                    return true;
                }

                if (id == R.id.profileButton) {
                    Intent intent = new Intent(Notifications.this, Profile.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });
    }

    private DbHandler dbHandler; // imports db handler //// here
    public List<List<String>> stringNotis;

    private Button openButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        int folderID = intent.getIntExtra("folderID", 0);
        int userID = intent.getIntExtra("userID", 0);

        setContentView(R.layout.notifications);
        dbHandler = new DbHandler(Notifications.this); // here // links db handler to class and with variable dbHandler to call later
        navBarController();

        // Get each of the relevant notifications and store as a string array of length 5
        stringNotis = dbHandler.getNotifications(userID);
        addNotiRows();
    }

    public void openDialog(List<String> noti) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Folder Invitation:")

        // Allow the user to accept or decline the invitation to a shared folder
        .setNegativeButton("Decline", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dbHandler.deleteNotification(Integer.parseInt(noti.get(0)));
                String ufID = dbHandler.getUFLink(folderID, userID);
                dbHandler.deleteUFlink(Integer.parseInt(ufID));
                // Get the uf link id and then delete the uf link if the user declined the invite
            }
        })
        .setPositiveButton("Accept", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                findViewById(R.id.grid).setVisibility(View.INVISIBLE);
            }
        });
        builder.show();
    }
    public android.content.Context cntx() {
        return getApplicationContext();
    }
    //Shorthand for getApplicationContext()

    //Gets screen pixel density and uses it to turn dependent pixels into their relative pixel values for display
    public static int DPtoPixels(android.content.Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public AppCompatButton createNotiButton(String notiMessage){
        AppCompatButton newNoti = new AppCompatButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(
        GridLayout.UNDEFINED,GridLayout.FILL,1f),
        GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
        // Create a material button that takes up the width of the screen

        // Set the variables for the material button, the text value is equal to the message of the notification
        params.setMargins(DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),3),DPtoPixels(cntx(),6));
        newNoti.setLayoutParams(params);
        newNoti.setBackgroundResource(R.drawable.folder_view);
        newNoti.setText(notiMessage); // use will name function
        newNoti.setGravity(Gravity.START);
        newNoti.setGravity(Gravity.CENTER_VERTICAL);
        newNoti.setPadding(DPtoPixels(getApplicationContext(),15),0,0,0);
        return newNoti;
    }

    public void addNotiRows(){
        GridLayout table = findViewById(R.id.grid);

        // Create a notification button for each notification meant for the specific user
        for (int i = 0; i < stringNotis.size(); i++) {
            String message = stringNotis.get(i).get(4);
            TableRow newRow = new TableRow(this);
            AppCompatButton notiButton = createNotiButton(message);
            notiButton.setId(i);
            // ID of the button corresponds to each notification

            notiButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    openDialog(stringNotis.get(notiButton.getId()));
                    // ID of the button decides the notification that is passed to the dialog button
                }
            });
            table.addView(notiButton);
            // Add the notification button to grid within the scrollview
        }
    }

}



