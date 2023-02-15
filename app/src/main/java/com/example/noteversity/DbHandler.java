package com.example.noteversity;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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


}