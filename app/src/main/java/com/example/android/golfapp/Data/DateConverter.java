package com.example.android.golfapp.Data;

import java.util.Date;

import androidx.room.TypeConverter;

//Type converter class as cannot save dates into room database
public class DateConverter {
    //Room uses this when reading from the database
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    //Room uses this when writing to the database
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
