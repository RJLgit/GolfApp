package com.example.android.golfapp.Data;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GolfViewModel extends AndroidViewModel {

    private static final String TAG = "GolfViewModel";

    private LiveData<List<GolfRecord>> records;

    public GolfViewModel(@NonNull Application application) {
        super(application);
        GolfDatabase database = GolfDatabase.getInstance(getApplication());
        records = database.golfDao().loadAllRecords();
    }

    public LiveData<List<GolfRecord>> getRecords() {
        return records;
    }
}
