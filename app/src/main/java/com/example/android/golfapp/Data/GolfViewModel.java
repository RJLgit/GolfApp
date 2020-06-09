package com.example.android.golfapp.Data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GolfViewModel extends AndroidViewModel {

    private static final String TAG = "GolfViewModel";

    private LiveData<List<GolfRecord>> records;
    private GolfDatabase database;

    public GolfViewModel(@NonNull Application application) {
        super(application);
        database = GolfDatabase.getInstance(getApplication());
        records = database.golfDao().loadAllRecords();
    }

    public LiveData<List<GolfRecord>> getRecords() {
        return records;
    }

    public void insertRecord(final GolfRecord record) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.golfDao().insertGolfRecord(record);
            }
        });
    }
}
