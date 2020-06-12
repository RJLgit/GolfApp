package com.example.android.golfapp.Data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GolfDao {
    @Query("SELECT * FROM golfscores ORDER BY date")
    LiveData<List<GolfRecord>> loadAllRecords();

    @Query("SELECT * FROM golfscores WHERE name LIKE :name ORDER BY date")
    LiveData<List<GolfRecord>> loadPersonRecords(String name);

    //Can make list a set to remove duplicates later
    @Query("SELECT name from golfscores")
    LiveData<List<String>> getAllNames();

    //Can make list a set to remove duplicates later
    @Query("SELECT course from golfscores")
    LiveData<List<String>> getAllCourses();

    @Insert
    void insertGolfRecord(GolfRecord golfRecord);

    @Delete
    void deleteTask(GolfRecord golfRecord);
}
