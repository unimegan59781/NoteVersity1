package com.example.noteversity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    public ArrayList<String> UserIDs = new ArrayList<String>();
    public ArrayList<Notification> Notis = new ArrayList<Notification>();
    private Button openButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        UserIDs.add("Henry");
        UserIDs.add("Josh");
        UserIDs.add("Fred Harper Morgan Christie-Cooper");
        UserIDs.add("Chris");
        UserIDs.add("Jack");
        Notis.add(new Notification(1,"Folder 1",2,3,true));
        Notis.add(new Notification(2,"Folder 1",2,4));
        Notis.add(new Notification(3,"Folder 3",4,3,true));
        /*openButton = (Button) findViewById(R.id.btnOpen);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

         */
    }

    public class Notification {
        public int notiID;
        public String folderName;
        public int folderID;
        public int senderID;
        public int recipientID;
        public Boolean invite;
        public Notification(int notiID,String folderN,int senderID,int recipientID,Boolean invite){
           this.notiID = notiID;
           this.senderID = senderID;
           this.recipientID = recipientID;
           this.folderName = folderN;
           if (invite==null){invite=false;}
            else this.invite = invite;
        }

        public Notification(int notiID,String folderN,int senderID,int recipientID){
            this.notiID = notiID;
            this.senderID = senderID;
            this.recipientID = recipientID;
            this.folderName = folderN;
            this.invite = false;
        }
    }

    public void open(View view) {
        TableLayout table = findViewById(R.id.tableLayout);
        ArrayList<Integer> checked = new ArrayList<Integer>();
        for (int i=0;i<table.getChildCount()-1;i++) {
            CheckBox checkBox = findViewById(i);
            if (checkBox.isChecked() == true) {
                checked.add(i);
            }
        }
        if (checked.size()!=1){}
            else openDialog(view,Notis.get(checked.get(0)));


        /*
        if number of notifications selected > 1 then
            print "No more than 1
        else
        invitation = table.getView(index of selected notification

        Pop up menu
        "invitation.SenderID.name has shared folder name with you. Do you wish to accept the invitation?
            Accept           Decline
                "Are you sure you want to accept/decline?"

         */
    }

    public void openDialog(View view,Notification noti) {
        TextView text = findViewById(R.id.textNotiCount);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Folder Invitation:")
                .setMessage(String.valueOf(noti.senderID)+" has shared "+noti.folderName+" with you,\nDo you want to accept the invitation?")
                .setNegativeButton("Decline", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        text.setText("Declined!!");
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        text.setText("Accepted!");
                    }
                });
        builder.show();
    }

    /*
            Sample TableRows

            <TableRow
                android:layout_width="match_parent"
                android:layout_height="match_parent">

                <CheckBox
                    android:id="@+id/checkBoxRow1"
                    style="@style/Widget.Material3.CompoundButton.CheckBox"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:checked="false"
                    android:drawable="@color/material_dynamic_neutral_variant30" />

                <TextView
                    android:id="@+id/textView"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Henry"
                    android:textColor="@color/white" />

            </TableRow>

            <TableRow
                android:layout_width="match_parent"
                android:layout_height="match_parent">

                <CheckBox
                    android:id="@+id/checkBoxRow2"
                    style="@android:style/Widget.CompoundButton.CheckBox"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:shadowColor="@color/white" />

                <TextView
                    android:id="@+id/textView2"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Jack"
                    android:textColor="@color/white" />

            </TableRow>
     */

    public void addNameRows(View view){
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for (int i = 0; i < Notis.size(); i++) {
            TextView text = new TextView(this);
            String message = String.valueOf(Notis.get(i).senderID)+" has shared folder '"+Notis.get(i).folderName+"' with you.";
            text.setText(message);
            TextView textInvite = new TextView(this);
            if (Notis.get(i).invite==true){textInvite.setText("Invite");}
                else textInvite.setText("Folder update");
            text.setTextColor(Color.WHITE);
            textInvite.setTextColor(Color.WHITE);
            text.setTextSize(14);
            textInvite.setTextSize(14);
            TableRow newRow = new TableRow(this);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(i);
            newRow.addView(checkBox);
            newRow.addView(text);
            newRow.addView(textInvite);
            table.addView(newRow, table.getChildCount() - 1);
        /*
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for (int i = 0; i < UserIDs.size(); i++) {
            TextView text = new TextView(this);
            text.setText(UserIDs.get(i));
            text.setTextColor(Color.WHITE);
            text.setTextSize(14);
            TableRow newRow = new TableRow(this);
            CheckBox checkBox = new CheckBox(this);
            newRow.addView(checkBox);
            newRow.addView(text);
            table.addView(newRow, table.getChildCount() - 1);
            */
        }
    }

    public void addTableRow(View view){
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        table.removeViewAt(3);
    }
}
