package com.example.android.golfapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private static final String TAG = "DatePickerFragment";
    private EnterFragment fragment;
    public DatePickerFragment(EnterFragment enterFragment) {
        fragment = enterFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "onCreateDialog: " + getContext());
        Log.d(TAG, "onCreateDialog: " + getParentFragment());
        DatePickerDialog result = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) fragment, year, month, day);
        result.getDatePicker().setMaxDate(System.currentTimeMillis());
        return result;
    }
}
