package com.example.noteversity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

        static final int DB_VERSION = 1; // db version control

        public DbHandler(Context context) { // Blank constructor
                super(context, "NoteVersity.DB", null, DB_VERSION);
        }

        SQLiteDatabase db = this.getReadableDatabase(); // gets db so can read/write
        //SQLiteDatabase dbb = this.getWritableDatabase();
        Cursor c;

        @Override // links dbModles on create to generate tables
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(DbModels.CREATEUSERS);
                db.execSQL(DbModels.CREATEFOLDERS);
                db.execSQL(DbModels.CREATENOTES);
                db.execSQL(DbModels.CREATEUFLINK);
                db.execSQL(DbModels.CREATENOTIFICATIONS);
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

        public List<String> getFolder(String folderName){ // gets note with given note id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(1) + " WHERE " + DbModels.FOLDER + "=?", new String[]{folderName});
                c.moveToFirst();
                if (c == null) {
                        return null;
                }
                String folderID = c.getString(0);
                String userID = c.getString(1);
                String name = c.getString(2);
                String timedate = c.getString(3);
                List<String> folder = Arrays.asList(folderID, userID, name, timedate);
                c.close();

                return folder; // returns string in collunm order can change and make class/object if needed
        }

        public void deleteFolder(String folderName){
                //db.delete(DbModels.tableList.get(2), "F_ID=?", new String[]{String.valueOf(folderID)}); // delete all notes in folder
                List<String> folder = getFolder(folderName);
                String folderID = folder.get(0);
                db.delete(DbModels.tableList.get(1), DbModels.FOLDER + "=?", new String[]{folderName});
                db.delete(DbModels.tableList.get(2), DbModels.F_ID + "=?", new String[]{folderID});

                //db.delete(DbModels.tableList.get(1), "F_ID=?", new String[]{String.valueOf(folderID)}); // delete folder
                //db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(2) + " WHERE " + DbModels.F_ID + "=" + folderID + "", null );
        }

        public void deleteNote(String noteName){
                db.delete(DbModels.tableList.get(2), DbModels.NAME + "=?", new String[]{noteName});

                //db.delete(DbModels.tableList.get(1), "F_ID=?", new String[]{String.valueOf(folderID)}); // delete folder
                //db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(2) + " WHERE " + DbModels.F_ID + "=" + folderID + "", null );
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

        public void insertNotification(int notiID, int senderID, int recipientID, int folderID, String folderName, boolean invite) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.NF_ID, notiID);
                contentValue.put(DbModels.FOLDER_ID, folderID);
                contentValue.put(DbModels.RECIPIENT_ID, recipientID);
                contentValue.put(DbModels.SENDER_ID, senderID);
                contentValue.put(DbModels.FOLDER_NAME, folderName);
                contentValue.put(DbModels.INVITE, invite);
                contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now

                db.insert(DbModels.tableList.get(4), null, contentValue); // null for auto filling id
         }

        public String searchUser(String userName){ // gets folder with given id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(0) + " WHERE " + DbModels.USERNAME + "=?", new String[]{userName});
                c.moveToFirst();
                if (c == null) {
                        return null;
                }
                String userID = c.getString(0);
                c.close();

                return userID;
        }

        public String searchNote(String name){ // gets folder with given id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(2) + " WHERE " + DbModels.NAME + "=?", new String[]{name});
                c.moveToFirst();
                if (c == null) {
                        return null;
                }
                String note = c.getString(3);
                c.close();
                return note;
        }

        public String searchFolder(String name){ // gets folder with given id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(1) + " WHERE " + DbModels.FOLDER + "=?", new String[]{name});
                c.moveToFirst();
                if (c == null) {
                        return null;
                }
                String folderName = c.getString(2);
                c.close();
                return folderName;
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

        public List<String> getAllFolders(int uID){ // gets folder with given id from raw query
                List<String> allFolders = new ArrayList<>();
                c =  db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(1) + " WHERE " + DbModels.U_ID + "=" + uID + "", null );
                c.moveToFirst();

                if (c != null && c.moveToFirst()) {
                        do {
                                String folderName = c.getString(2);
                                allFolders.add(folderName);
                        } while (c.moveToNext());
                }
                if (c == null) {
                        return null;
                }
//                String folderID = c.getString(0);
//                String userID = c.getString(1);
//                String timedate = c.getString(3);
//                List<String> folder = Arrays.asList(folderID, userID, folderName, timedate);
                c.close();

                return allFolders; // returns string in collunm order can change and make class/object if needed
        }

        public List<String> getFolderNotes(int fID){ // gets note with given note id from raw query
                List<String> noteNames = new ArrayList<>();
                c =  db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(2) + " WHERE " + DbModels.F_ID + "=" + fID + "", null );
                c.moveToFirst();
                if (c != null && c.moveToFirst()) {
                        do {
                                String noteName = c.getString(3);
                                noteNames.add(noteName);
                        } while (c.moveToNext());
                }
                if (c == null) {
                        return null;
                }
                c.close();

                return noteNames; // returns string in collunm order can change and make class/object if needed
        }


        public String getNoteImg(String noteName){ // gets note with given note id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(2) + " WHERE " + DbModels.NAME + "=?", new String[] { noteName });
                c.moveToFirst();
                String noteImg = c.getString(4);
                if (c == null) {
                        return null;
                }
                c.close();

                return noteImg; // returns string in collunm order can change and make class/object if needed
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

        @Override // stops tables being created again if already exist (user already has app installed)
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + "USERS");
                db.execSQL("DROP TABLE IF EXISTS " + "FOLDERS");
                db.execSQL("DROP TABLE IF EXISTS " + "NOTES");
                db.execSQL("DROP TABLE IF EXISTS " + "UFLINK");
                db.execSQL("DROP TABLE IF EXISTS " + "NOTIFICATIONS");
                onCreate(db);
        }

        // to do
        ///// link folder/user
        ///// update????????
        ///// searching
        ///// delete alter

}