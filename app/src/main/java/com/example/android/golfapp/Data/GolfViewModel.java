package com.example.android.golfapp.Data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
//View Model class to allow fragments in the app to observe certain values in the database
public class GolfViewModel extends AndroidViewModel {

    private static final String TAG = "GolfViewModel";

    private LiveData<List<GolfRecord>> records;
    private LiveData<List<String>> names;
    private LiveData<List<String>> courses;
    private GolfDatabase database;

    public GolfViewModel(@NonNull Application application) {
        super(application);
        database = GolfDatabase.getInstance(getApplication());
        records = database.golfDao().loadAllRecords();
        names = database.golfDao().getAllNames();
        courses = database.golfDao().getAllCourses();
    }

    public LiveData<List<GolfRecord>> getRecords() {
        return records;
    }

    public LiveData<List<String>> getNames() {
        return names;
    }

    public LiveData<List<String>> getCourses() {
        return courses;
    }

    public void insertRecord(final GolfRecord record) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.golfDao().insertGolfRecord(record);
            }
        });
    }

    public void deleteRecord(final GolfRecord record) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.golfDao().deleteTask(record);
            }
        });
    }

    public LiveData<List<GolfRecord>> getSpecificName(final String n) {
        return database.golfDao().loadPersonRecords(n);
    }

}
