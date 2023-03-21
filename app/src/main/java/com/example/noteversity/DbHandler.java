package com.example.noteversity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;

public class DbHandler extends SQLiteOpenHelper {

        static final int DB_VERSION = 1; // db version control

        public DbHandler(Context context) { // Blank constructor
                super(context, "NoteVersity.DB", null, DB_VERSION);
        }

        SQLiteDatabase db = this.getReadableDatabase(); // gets db so can read/write to

        @Override // links dbModles on create to generate tables
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(DbModels.CREATEUSERS);
                db.execSQL(DbModels.CREATEFOLDERS);
                db.execSQL(DbModels.CREATENOTES);
                db.execSQL(DbModels.CREATEUFLINK);
        }

        public void insertUser(String email, String username, String password) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.EMAIL, email);
                contentValue.put(DbModels.USERNAME, username);
                contentValue.put(DbModels.PASSWORD, password);

                db.insert(DbModels.tableList.get(0), null, contentValue); // null for filling the id
        }

        public void insertFolder(int userID, String folderName) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.U_ID, userID);
                contentValue.put(DbModels.FOLDER, folderName);
                contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now

                db.insert(DbModels.tableList.get(1), null, contentValue); // null for auto fill id
        }


        public void insertNotes(int userID, int folderID, String noteName, String noteIMG) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.U_ID, userID);
                contentValue.put(DbModels.F_ID, folderID);
                contentValue.put(DbModels.NAME, noteName);
                contentValue.put(DbModels.NOTEIMG, noteIMG);
                contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now

                db.insert(DbModels.tableList.get(2), null, contentValue); // null for auto filling id
        }


//        public String getNotes(){
//                Cursor cursor = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(2) + " WHERE "+ N_ID + "=" + 1, new String[]{}); // just incase where clause not valid
//                cursor.moveToFirst();
//
//                return "cow";
//
//        }

        public void delete(Integer tablePos, Integer user) {
                db.delete(DbModels.tableList.get(tablePos), DbModels.U_ID + "=" + user, null);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + "USERS");
                db.execSQL("DROP TABLE IF EXISTS " + "FOLDERS");
                db.execSQL("DROP TABLE IF EXISTS " + "NOTES");
                db.execSQL("DROP TABLE IF EXISTS " + "UFLINK");
                onCreate(db);
        }

        // to do
        ///// link folder/user
        ///// update????????
        ///// searching

}