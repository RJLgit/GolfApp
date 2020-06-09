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

    //Can make list a set to remove duplicates later
    @Query("SELECT name from golfscores")
    List<String> getAllNames();

    //Can make list a set to remove duplicates later
    @Query("SELECT course from golfscores")
    List<String> getAllCourses();

    @Insert
    void insertGolfRecord(GolfRecord golfRecord);

    @Delete
    void deleteTask(GolfRecord golfRecord);
}
