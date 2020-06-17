package com.example.android.golfapp.Data;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
//Singleton pattern used in this class to creat the room database
@Database(entities = {GolfRecord.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class GolfDatabase extends RoomDatabase {

    private static final String TAG = GolfDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "golfdatabase";
    private static GolfDatabase mInstance;
    //Get instance method to ensure only a single database instance exists
    public static GolfDatabase getInstance(Context context) {
        if (mInstance == null) {
            //Stops 2 db objects being made at the same time
            synchronized (LOCK) {
                Log.d(TAG, "Creating new db instance");
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        GolfDatabase.class, GolfDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting db instance");
        return mInstance;
    }

    public abstract GolfDao golfDao();

}
