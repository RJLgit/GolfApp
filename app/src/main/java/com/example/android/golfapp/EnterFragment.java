package com.example.android.golfapp;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.golfapp.Data.GolfRecord;
import com.example.android.golfapp.Data.GolfViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "EnterFragment";
    //UI elements
    AutoCompleteTextView nameEditText;
    AutoCompleteTextView courseEditText;
    NumberPicker parNumberPicker;
    NumberPicker scoreNumberPicker;
    EditText dateEditText;
    Button enterButton;

    //Auto-complete arrays of strings for each auto Complete text view
    private String[] names = new String[]{};
    private String[] courses = new String[] {};

    private EnterFragmentListener listener;
    MainActivity theActivity;
    public EnterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_enter, container, false);
        enterButton = v.findViewById(R.id.enterButton);
        dateEditText = v.findViewById(R.id.date_edit_text);
        scoreNumberPicker = v.findViewById(R.id.score_number_picker);
        parNumberPicker = v.findViewById(R.id.par_number_picker);
        courseEditText = v.findViewById(R.id.course_edit_text);
        nameEditText = v.findViewById(R.id.name_edit_text);




        parNumberPicker.setMinValue(27);
        parNumberPicker.setMaxValue(80);
        scoreNumberPicker.setMinValue(25);
        scoreNumberPicker.setMaxValue(130);

        scoreNumberPicker.setValue(90);
        parNumberPicker.setValue(72);

        //Sets date to today by default. When user clicks it allow them to select date via a data picker alert
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateEditText.setText(dateFormat.format(new Date()));

        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Log.d(TAG, "onClick: date");
                    DialogFragment datePicker = new DatePickerFragment(EnterFragment.this);

                    datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
                }
            }
        });
        GolfViewModel viewModel = new ViewModelProvider(requireActivity()).get(GolfViewModel.class);
        viewModel.getNames().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> golfNames) {
                Log.d(TAG, "onChanged: " + "set names adapter");
                Set<String> removeDuplicatesSet = new HashSet<>(golfNames);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                setNames(arr);
            }
        });
        viewModel.getCourses().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> golfCourses) {
                Log.d(TAG, "onChanged: " + "set courses adapter");
                Set<String> removeDuplicatesSet = new HashSet<>(golfCourses);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                setCourses(arr);
            }
        });


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Need to add checks that not null

                try {
                    String name = nameEditText.getText().toString();
                    String course = courseEditText.getText().toString();
                    Log.d(TAG, "onClick: " + name);
                    if (name.equals("") || course.equals("")) {
                        Toast.makeText(getContext(), "Must enter a valid course and name", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: failed due to name");
                        return;
                    }
                    int par = parNumberPicker.getValue();
                    int score = scoreNumberPicker.getValue();
                    SimpleDateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = myDateFormat.parse(dateEditText.getText().toString());
                    if (listener != null) {
                        listener.onRecordSent(name, course, par, score, date);
                        clearAllFields();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Must enter a number for both the par and score", Toast.LENGTH_SHORT).show();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    Toast.makeText(getContext(), "Must enter a valid date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }



    private void clearAllFields() {
        nameEditText.getText().clear();
        courseEditText.getText().clear();
        scoreNumberPicker.setValue(90);
        parNumberPicker.setValue(72);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateEditText.setText(dateFormat.format(new Date()));
    }

    public String[] getNames() {
        return names;
    }



    //When names is set them it sets adapter to the edit text
    public void setNames(String[] names) {
        this.names = names;
        if (this.isAdded()) {
            ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, names);
            if (nameEditText != null) {
                nameEditText.setAdapter(namesAdapter);
                //Shows dropdown when has focus
                nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            nameEditText.showDropDown();
                        } else {
                            nameEditText.dismissDropDown();
                        }
                    }
                });
            }
        }
    }

    public String[] getCourses() {
        return courses;
    }

    //When courses is set then it sets the courses to the edit text
    public void setCourses(String[] courses) {
        this.courses = courses;
        if (this.isAdded()) {
            ArrayAdapter<String> coursesAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, courses);
            if (courseEditText !=null) {
                courseEditText.setAdapter(coursesAdapter);
                //Shows dropdown when has focus
                courseEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            courseEditText.showDropDown();
                        } else {
                            courseEditText.dismissDropDown();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            activity.hideMenus();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EnterFragmentListener");
        }
        if (context instanceof EnterFragmentListener) {
            listener = (EnterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EnterFragmentListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void dateSet(String format) {
        dateEditText.setText(format);
    }

    public interface EnterFragmentListener {
        void onRecordSent(String name, String course, int par, int score, Date date);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "onDateSet: ");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        Log.d(TAG, "onDateSet: " + c);
        Date d = c.getTime();
        Log.d(TAG, "onDateSet: " + d);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateSet(dateFormat.format(d));
    }
}
