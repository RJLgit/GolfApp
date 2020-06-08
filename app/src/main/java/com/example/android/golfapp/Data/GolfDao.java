package com.example.android.golfapp.Data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GolfDao {
    @Query("SELECT * FROM GolfScores ORDER BY date")
    List<GolfRecord> loadAllTasks();

    //Can make list a set to remove duplicates later
    @Query("SELECT name from GolfScores")
    List<String> getAllNames();

    //Can make list a set to remove duplicates later
    @Query("SELECT course from GolfScores")
    List<String> getAllCourses();

    @Insert
    void insertGolfRecord(GolfRecord golfRecord);

    @Delete
    void deleteTask(GolfRecord golfRecord);
}
