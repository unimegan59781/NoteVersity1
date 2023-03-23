package com.example.noteversity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

        static final int DB_VERSION = 1; // db version control

        public DbHandler(Context context) { // Blank constructor
                super(context, "NoteVersity.DB", null, DB_VERSION);
        }

        SQLiteDatabase db = this.getReadableDatabase(); // gets db so can read/write
        Cursor c;

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

        public List<String> getUser(int uID){ // gets folder with given id from raw query
                c =  db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(0) + " WHERE " + DbModels.U_ID + "=" + uID + "", null );
                c.moveToFirst();
                if (c == null) {
                        return null;
                }

                String userID = c.getString(0);
                String email = c.getString(1);
                String username = c.getString(2);
                String password = c.getString(3);
                List<String> folder = Arrays.asList(userID, email, username, password);
                c.close();

                return folder; // returns string in collunm order can change and make class/object if needed
        }

        public List<String> getFolder(int fID){ // gets folder with given id from raw query
                c =  db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(1) + " WHERE " + DbModels.F_ID + "=" + fID + "", null );
                c.moveToFirst();
                if (c == null) {
                        return null;
                }

                String folderID = c.getString(0);
                String userID = c.getString(1);
                String folderName = c.getString(2);
                String timedate = c.getString(3);
                List<String> folder = Arrays.asList(folderID, userID, folderName, timedate);
                c.close();

                return folder; // returns string in collunm order can change and make class/object if needed
        }

        public List<String> getNote(int nID){ // gets note with given note id from raw query
                c =  db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(2) + " WHERE " + DbModels.N_ID + "=" + nID + "", null );
                c.moveToFirst();
                if (c == null) {
                        return null;
                }
//                int nInt = c.getColumnIndex(DbModels.N_ID);
//                int fInt = c.getColumnIndex(DbModels.F_ID);
//                int uInt = c.getColumnIndex(DbModels.U_ID);
//                int nameInt = c.getColumnIndex(DbModels.NAME);
//                int imgInt = c.getColumnIndex(DbModels.NOTEIMG);
//                int timeDateInt = c.getColumnIndex(DbModels.TIMEDATE);
                String noteID = c.getString(0);
                String folderID = c.getString(1);
                String userID = c.getString(2);
                String noteName = c.getString(3);
                String noteImg = c.getString(4);
                String timedate = c.getString(5);
                List<String> note = Arrays.asList(noteID, folderID, userID, noteName, noteImg, timedate);
                c.close();

                return note; // returns string in collunm order can change and make class/object if needed

        }

        public void delete(Integer tablePos, Integer user) {
                db.delete(DbModels.tableList.get(tablePos), DbModels.U_ID + "=" + user, null);
        }

        @Override // stops tables being created again if already exist (user already has app installed)
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
        ///// delete alter

}