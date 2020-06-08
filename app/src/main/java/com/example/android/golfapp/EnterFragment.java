package com.example.android.golfapp;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterFragment extends Fragment {
    //UI elements
    AutoCompleteTextView nameEditText;
    AutoCompleteTextView courseEditText;
    EditText parEditText;
    EditText scoreEditText;
    EditText dateEditText;
    Button enterButton;

    //Auto-complete arrays of strings for each auto Complete text view
    private String[] names = new String[]{};
    private String[] courses = new String[] {};

    private EnterFragmentListener listener;
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
        scoreEditText = v.findViewById(R.id.score_edit_text);
        parEditText = v.findViewById(R.id.par_edit_text);
        courseEditText = v.findViewById(R.id.course_edit_text);
        nameEditText = v.findViewById(R.id.name_edit_text);
        //Sets date to today by default. When user clicks it allow them to select date via a data picker alert
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateEditText.setText(dateFormat.format(new Date()));




        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Need to add checks that not null
                String name = nameEditText.getText().toString();
                String course = courseEditText.getText().toString();
                int par = Integer.parseInt(parEditText.getText().toString());
                int score = Integer.parseInt(scoreEditText.getText().toString());
                SimpleDateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                try {
                    date = myDateFormat.parse(dateEditText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (listener != null) {
                    listener.onRecordSent(name, course, par, score, date);
                }

            }
        });

        return v;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public String[] getCourses() {
        return courses;
    }

    public void setCourses(String[] courses) {
        this.courses = courses;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public interface EnterFragmentListener {
        void onRecordSent(String name, String course, int par, int score, Date date);
    }

}
