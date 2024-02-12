package com.example.datastorage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserTable.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;

    // Constant for the database name
    private static final String databaseName = "User Registration";

    public synchronized static AppDatabase getInstance(Context context)
    {
        if(database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,databaseName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract UserDao userDao();
}
