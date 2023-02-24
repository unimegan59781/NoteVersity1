package com.example.noteversity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.List;

public class DbModels extends SQLiteOpenHelper {
    static final int DB_VERSION = 1; //db version control

    static List<String> tableList = Arrays.asList("USERS", "FOLDERS", "NOTES", "UFLINK"); //list of table names -- KEEP ORDER FOR CROSS REFERNCING

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
    public static final String NOTE = "noteIMG";


    // TABLE CREATION
    private static final String CREATEUSERS = "create table " + tableList.get(0)
            + "(" + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EMAIL + " TEXT NOT NULL, "
            + USERNAME  + " TEXT NOT NULL, "
            + PASSWORD  + " TEXT NOT NULL);";

    private static final String CREATEFOLDERS = "create table " + tableList.get(1)
            + "(" + F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + U_ID + " INTEGER NOT NULL, "
            + FOLDER + " TEXT NOT NULL, "
            + TIMEDATE  + " NOT NULL);";

    private static final String CREATENOTES = "create table " + tableList.get(2)
            + "(" + N_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + U_ID + " INTEGER NOT NULL, "
            + F_ID + " INTEGER NOT NULL, "
            + NAME + " TEXT NOT NULL, "
            + NOTE + " TEXT NOT NULL, "
            + TIMEDATE  + " NOT NULL);";

    private static final String CREATEUFLINK = "create table " + tableList.get(3)
            + "(" + F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + U_ID + " INTEGER NOT NULL);";


    public DbModels(Context context) { // Blank constructor
        super(context,  "NoteVersity.DB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATEUSERS);
        db.execSQL(CREATEFOLDERS);
        db.execSQL(CREATENOTES);
        db.execSQL(CREATEUFLINK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String name : tableList) { // itterates through and checks for table updates if non then doesnt update/create that table
            db.execSQL("DROP TABLE IF EXISTS " + name);
        }
        onCreate(db);
    }
}