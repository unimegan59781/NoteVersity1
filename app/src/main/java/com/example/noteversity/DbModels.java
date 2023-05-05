package com.example.noteversity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.List;

public class DbModels { // TO COMMENT

    static List<String> tableList = Arrays.asList("USERS", "FOLDERS", "NOTES","UFLINK","NOTIFICATIONS"); //list of table names -- KEEP ORDER FOR CROSS REFERNCING

    // USERS TABLE
    public static final String U_ID = "userID";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    // FOLDER TABLE
    public static final String F_ID = "folderID";
    public static final String FOLDER = "folderName";
    public static final String TIMEDATE = "timeDate"; //////TOFIGURE OUT

    // NOTES TABLE
    public static final String N_ID = "noteID";
    public static final String NAME = "noteName";
    public static final String NOTEIMG = "noteImg";

    // NOTES TABLE
    public static final String UF_ID = "ufID";

    // NOTIFICATIONS TABLE
    public static final String NF_ID = "notificationID";
    public static final String SENDER_ID  = "senderID";
    public static final String RECIPIENT_ID = "recipientID";
    public static final String FOLDER_ID = "folderID";

    // TABLE CREATION
    public static String CREATEUSERS = "create table " + tableList.get(0)
            + "(" + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EMAIL + " TEXT NOT NULL, "
            + USERNAME  + " TEXT NOT NULL, "
            + PASSWORD  + " TEXT NOT NULL);";

    public static String CREATEFOLDERS = "create table " + tableList.get(1)
            + "(" + F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + U_ID + " INTEGER NOT NULL, "
            + FOLDER + " TEXT NOT NULL, "
            + TIMEDATE  + " NOT NULL);";

    public static String CREATENOTES = "create table " + tableList.get(2)
            + "(" + N_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + U_ID + " INTEGER NOT NULL, "
            + F_ID + " INTEGER NOT NULL, "
            + NAME + " TEXT NOT NULL, "
            + NOTEIMG + " BLOB, "
            + TIMEDATE  + " NOT NULL);";

    public static String CREATEUFLINK = "create table " + tableList.get(3)
            + "(" + UF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + F_ID + " INTEGER NOT NULL, "
            + U_ID + " INTEGER NOT NULL);";

    public static String CREATENOTIFICATIONS = "create table " + tableList.get(4)
            + "(" + NF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SENDER_ID + " INTEGER NOT NULL, "
            + RECIPIENT_ID + " INTEGER NOT NULL, "
            + FOLDER_ID + " INTEGER NOT NULL, "
            + TIMEDATE  + " NOT NULL);";

}