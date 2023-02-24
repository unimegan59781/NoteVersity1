package com.example.noteversity;


import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.time.LocalDate;

public class DbHandler {

        private DbModels dbModels;

        private Context context;

        private SQLiteDatabase db;

        public DbHandler(Context c) {
                context = c;
        }

        public DbHandler open() throws SQLException {
                dbModels = new DbModels(context);
                db = dbModels.getWritableDatabase();
                return this;
        }

        public void close() {
                dbModels.close();
        }

        public void insertUser(String email, String username, String password) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(DbModels.EMAIL, email);
                contentValue.put(DbModels.USERNAME, username);
                contentValue.put(DbModels.PASSWORD, password);
                db.insert(DbModels.tableList.get(0), null, contentValue); //null for id
        }

        public void insertFolder(Integer userID, String folderName) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(DbModels.U_ID, userID);
                contentValue.put(DbModels.FOLDER, folderName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now
                }
                db.insert(DbModels.tableList.get(1), null, contentValue); //null for id
        }

        public void insertNotes(Integer userID, Integer folderID, String noteName, String noteIMG) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(DbModels.U_ID, userID);
                contentValue.put(DbModels.F_ID, folderID);
                contentValue.put(DbModels.NAME, noteName);
                contentValue.put(DbModels.NOTE, noteIMG);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        contentValue.put(DbModels.TIMEDATE, String.valueOf(LocalDate.now())); // date as YYYY-MM-DD for now
                }
                db.insert(DbModels.tableList.get(2), null, contentValue); //null for id
        }


        public void delete(Integer tablePos, Integer user) {
                db.delete(DbModels.tableList.get(tablePos), DbModels.U_ID + "=" + user, null);
        }

        // to do
        ///// link folder/user
        ///// update????????
        ///// searching

}