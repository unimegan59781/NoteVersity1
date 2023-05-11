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

        // Blank constructor
        public DbHandler(Context context) {
                super(context, "NoteVersity.DB", null, DB_VERSION);
        }

        // gets db so can read/write
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor for sql searchers through db
        Cursor c;

        // links dbModles on create to generate tables
        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(DbModels.CREATEUSERS);
                db.execSQL(DbModels.CREATEFOLDERS);
                db.execSQL(DbModels.CREATENOTES);
                db.execSQL(DbModels.CREATEUFLINK);
                db.execSQL(DbModels.CREATENOTIFICATIONS);
        }

        // insert user to folder
        public void insertUFlink(int uID, int fID) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.F_ID, fID);
                contentValue.put(DbModels.U_ID, uID);

                db.insert(DbModels.tableList.get(3), null, contentValue); // null for filling the id
        }

        // delete folder linked to user
        public void deleteUFlink(int ufID){
                db.delete(DbModels.tableList.get(3), DbModels.UF_ID + "=?", null);
        }

        public String getUFLink(int fID, int uID) {
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(3) + " WHERE " + DbModels.U_ID + "=" + uID + " AND " + DbModels.F_ID + "=" + fID, null);

                if (c.getCount() == 0) {
                        c.close();
                        return null;
                }

                c.moveToFirst();

                String ufID = c.getString(0);
                c.close();

                return ufID;
        }

        // insert user to db function (login page)
        public void insertUser(String email, String username, String password) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.EMAIL, email);
                contentValue.put(DbModels.USERNAME, username);
                contentValue.put(DbModels.PASSWORD, password);

                db.insert(DbModels.tableList.get(0), null, contentValue); // null for filling the id
        }

        // insert folder function with user that created it
        public void insertFolder(int userID, String folderName) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.U_ID, userID);
                contentValue.put(DbModels.FOLDER, folderName);
                contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now

                db.insert(DbModels.tableList.get(1), null, contentValue); // null for auto fill id
        }

        // return list string of folder details
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

        // return list of strings of folder ids user in
        public List<String> getUsersFolders(int uID){
                List<String> allFolders = new ArrayList<>();
                c =  db.rawQuery("SELECT * FROM " + DbModels.tableList.get(3) + " WHERE " + DbModels.U_ID + "=" + uID, null);
                if (c != null && c.moveToFirst()) {
                        do {
                                String folderID = c.getString(1);
                                allFolders.add(folderID);
                        } while (c.moveToNext());
                }
                c.close();
                return allFolders;
        }

        // returns list of userid in certain folder
        public List<String> getUsersInFolder(int fID){
                List<String> allUsers = new ArrayList<>();
                c =  db.rawQuery("SELECT * FROM " + DbModels.tableList.get(3) + " WHERE " + DbModels.F_ID + "=" + fID, null);
                if (c != null && c.moveToFirst()) {
                        do {
                                String userID = c.getString(2);
                                allUsers.add(userID);
                        } while (c.moveToNext());
                }
                c.close();
                return allUsers;
        }

        // gets name of folder from its id
        public String getFolderName(int fID){
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(1) + " WHERE " + DbModels.F_ID + "=" + fID, null);

                if (c.getCount() == 0) {
                        c.close();
                        return null;
                }

                c.moveToFirst();

                String name = c.getString(2);
                c.close();

                return name;

        }

        // deletes whole folder from id and notes inside
        public void deleteFolder(String folderName){
                List<String> folder = getFolder(folderName);
                String folderID = folder.get(0);
                db.delete(DbModels.tableList.get(1), DbModels.FOLDER + "=?", new String[]{folderName});
                db.delete(DbModels.tableList.get(2), DbModels.F_ID + "=?", new String[]{folderID});
        }

        // deletes note from name
        public void deleteNote(String noteName){
                db.delete(DbModels.tableList.get(2), DbModels.NAME + "=?", new String[]{noteName});
        }

        // inserts note into db getting info from note creation
        public void insertNotes(int userID, int folderID, String noteName, String noteIMG) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table

                contentValue.put(DbModels.U_ID, userID);
                contentValue.put(DbModels.F_ID, folderID);
                contentValue.put(DbModels.NAME, noteName);
                contentValue.put(DbModels.NOTEIMG, noteIMG);
                contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now

                db.insert(DbModels.tableList.get(2), null, contentValue); // null for auto filling id
        }

        // insert notification in search user that creates notification to send
        public void insertNotification(int senderID, int recipientID, int folderID) {

                ContentValues contentValue = new ContentValues(); // ContentValues class to insert collumns into table
                contentValue.put(DbModels.SENDER_ID, senderID);
                contentValue.put(DbModels.RECIPIENT_ID, recipientID);
                contentValue.put(DbModels.FOLDER_ID, folderID);
                contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now

                db.insert(DbModels.tableList.get(4), null, contentValue); // null for auto filling id
        }

        // deletes notiifcation from database
        public void deleteNotification(int notiID){
                String n_ID = String.valueOf(notiID);
                db.delete(DbModels.tableList.get(4), DbModels.RECIPIENT_ID + "=?",new String[]{n_ID}); // delete all notes in folder
        }

        // creates notiifcation message to display
        public String notiMessage(int folderID,String senderID){
                String sender = getUserName(Integer.parseInt(senderID));
                String folder = getFolderName(folderID);
                return sender + " has shared the folder " + folder + " with you";
        }

        // returns list of notifications that user has to display
        public List<List<String>> getNotifications(int userID) {
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(4) + " WHERE " + DbModels.RECIPIENT_ID + "=" + userID + "", null);
                c.moveToFirst();
                List<List<String>> notificationList = new ArrayList<List<String>>();
                List<String> notification = null;
                if (c != null && c.moveToFirst()) {
                        do {
                                String nfID = c.getString(0);
                                String senderID = c.getString(1);
                                String recipientID = c.getString(2);
                                String folderID = c.getString(3);
                                String message = notiMessage(Integer.parseInt(folderID), senderID);
                                notification = Arrays.asList(nfID, senderID, recipientID, folderID,message);
                                notificationList.add(notification);
                        } while (c.moveToNext());
                }
                if (c == null) {
                        return null;
                }
                c.close();

                return notificationList; // returns string in column order can change and make class/object if needed
        }

        // takes username and searches if user in database if not returns null
        public String searchUser(String userName){ // gets folder with given id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(0) + " WHERE " + DbModels.USERNAME + "=?", new String[]{userName});
                if (c == null) {
                        return null;
                }
                if (c.moveToFirst()) {
                        String name = c.getString(1);
                        c.close();
                        return name;
                }
                c.close();
                return null;
        }

        // takes email and searches if user in database if not returns null
        public String searchEmail(String email) {
                // gets folder with given id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(0) + " WHERE " + DbModels.EMAIL + "=?", new String[]{email});

                // Check if cursor has any rows
                if (c.getCount() == 0) {
                        c.close();
                        return null;
                }

                c.moveToFirst();
                String userID = c.getString(0);
                c.close();
                return userID;
        }

        // checks if name of note in db to name validate
        public String searchNote(String name){ // gets folder with given id from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(2) + " WHERE " + DbModels.NAME + "=?", new String[]{name});
                if (c == null) {
                        return null;
                }
                if (c.moveToFirst()) {
                        String folderName = c.getString(2);
                        c.close();
                        return folderName;
                }
                c.close();
                return null;
        }

        // checks if name of the folder already in db to name validate
        public String searchFolder(String name){
                // gets folder with given name from raw query
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(1) + " WHERE " + DbModels.FOLDER + "=?", new String[]{name});
                if (c == null) {
                        return null;
                }
                if (c.moveToFirst()) {
                        String noteName = c.getString(3);
                        c.close();
                        return noteName;
                }
                c.close();
                return null;
        }

        // gets result of whole user from its id
        public List<String> getUser(int uID){
                c =  db.rawQuery( "SELECT * FROM " + DbModels.tableList.get(0) + " WHERE " + DbModels.U_ID + "=" + uID + "", null );
                c.moveToFirst();
                if (c == null) {
                        return null;
                }
                String userID = c.getString(0);
                String email = c.getString(1);
                String username = c.getString(2);
                String password = c.getString(3);
                List<String> user = Arrays.asList(userID, email, username, password);
                c.close();

                return user; // returns string in collunm order can change and make class/object if needed
        }

        // gets username from user id id db for message
        public String getUserName(int uID) {
                c = db.rawQuery("SELECT * FROM " + DbModels.tableList.get(0) + " WHERE " + DbModels.U_ID + "=" + uID, null);

                if (c.getCount() == 0) {
                        c.close();
                        return null;
                }

                c.moveToFirst();

                String name = c.getString(1);
                c.close();

                return name;
        }

        // gets all notes in folder to display (returns list of names of notes)
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

        // returns image string (byte[]) to turn into bitmap and set as background to edit
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

        // change username on profile page
        public void changeUserName(String username, int uID){
                String sql = "UPDATE USERS SET username = " + "'"+username+"' " + "WHERE userID = " + uID;
                System.out.println(sql);
                db.execSQL(sql);
        }

        // change foldername when double tap folder page
        public void changeFoldername(String name, int fID){
                String sql = "UPDATE FOLDERS SET folderName = " + "'"+name+"' " + "WHERE folderID = " + fID;
                //System.out.println(sql);
                db.execSQL(sql);
        }

        // stops tables being created again if already exist (user already has app installed)
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + "USERS");
                db.execSQL("DROP TABLE IF EXISTS " + "FOLDERS");
                db.execSQL("DROP TABLE IF EXISTS " + "NOTES");
                db.execSQL("DROP TABLE IF EXISTS " + "UFLINK");
                db.execSQL("DROP TABLE IF EXISTS " + "NOTIFICATIONS");
                onCreate(db);
        }

}