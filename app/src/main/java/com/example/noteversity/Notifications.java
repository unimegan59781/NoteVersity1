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
    public ArrayList<Notification> Notis = new ArrayList<Notification>();
    //public List<List<String>> stringNotis;
    public List<List<String>> stringNotis;
    public ArrayList<Notification> fetchedNotis = new ArrayList<Notification>();

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
/*
        dbHandler.deleteNotification(1);
        dbHandler.deleteNotification(2);
        dbHandler.deleteNotification(3);
        dbHandler.deleteNotification(4);
        dbHandler.deleteNotification(5);
        dbHandler.deleteNotification(6);
        dbHandler.deleteNotification(7);



        Notis.add(new Notification(1,2,3,1));
        Notis.add(new Notification(3,4,3,3));
        Notis.add(new Notification(2,2,6,7));

         for (int i = 0; i < 3; i++){
            dbHandler.insertNotification(Notis.get(i).senderID,Notis.get(i).recipientID,Notis.get(i).folderID);
        }


*/
        dbHandler.insertNotification(2,1,1);


        stringNotis = dbHandler.getNotifications(1);

        addNotiRows();

    }

    public class Notification {
        public int notiID;
        public int folderID;
        public int recipientID;
        public String message;
        public Notification(int notiID,int senderID,int recipientID,int folderID,String message) {
            this.notiID = notiID;
            this.recipientID = recipientID;
            this.folderID = folderID;
            this.message = message;
        }
    }

    public void openDialog(List<String> noti) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Folder Invitation:")
                // Below will be a premade message fetched from the get Notifications
                //.setMessage(String.valueOf(noti.senderID)+" has shared Folder "+noti.folderID+" with you,\nDo you want to accept the invitation?")
                .setNegativeButton("Decline", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dbHandler.deleteNotification(Integer.parseInt(noti.get(0)));
                        String ufID = dbHandler.getUFLink(folderID, userID);
                        dbHandler.deleteUFlink(Integer.parseInt(ufID));

                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //dbHandler.deleteUFlink(fID);
                        findViewById(R.id.grid).setVisibility(View.INVISIBLE);
                    }
                });
        builder.show();
    }
    public android.content.Context cntx() {
        return getApplicationContext();
    }

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
        for (int i = 0; i < stringNotis.size(); i++) {
            String message = stringNotis.get(i).get(4);
            TableRow newRow = new TableRow(this);
            AppCompatButton notiButton = createNotiButton(message);
            notiButton.setId(i);
            notiButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    openDialog(stringNotis.get(notiButton.getId()));
                }
            });
            table.addView(notiButton);
        }
    }

}



