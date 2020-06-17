package com.example.android.golfapp.Data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
//Room Dao interface for interacting with the db
@Dao
public interface GolfDao {
    //Gets all the records in the db
    @Query("SELECT * FROM golfscores ORDER BY date")
    LiveData<List<GolfRecord>> loadAllRecords();

    //Gets a single players records from the db
    @Query("SELECT * FROM golfscores WHERE name LIKE :name ORDER BY date")
    LiveData<List<GolfRecord>> loadPersonRecords(String name);

    //Gets all the names in the db
    @Query("SELECT name from golfscores")
    LiveData<List<String>> getAllNames();

    //Gets all the courses in the db
    @Query("SELECT course from golfscores")
    LiveData<List<String>> getAllCourses();

    //Inserts a record into the db
    @Insert
    void insertGolfRecord(GolfRecord golfRecord);

    //Deletes a record from the db
    @Delete
    void deleteTask(GolfRecord golfRecord);
}
