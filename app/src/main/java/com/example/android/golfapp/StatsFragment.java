package com.example.android.golfapp;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.golfapp.Data.GolfDatabase;
import com.example.android.golfapp.Data.GolfRecord;
import com.example.android.golfapp.Data.GolfViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "StatsFragment";
    GolfDatabase mDb;

    Spinner spinner;
    Spinner courseSpinner;
    TextView nameTextView;
    TextView courseTextView;
    TextView recentRoundsTextView;
    GraphView graph;
    ArrayList<GolfRecord> allRecords = new ArrayList<>();
    //Context mContext;
    String[] myNames;
    String[] myCourses;
    String name;
    String course;


    public StatsFragment() {
        // Required empty public constructor
    }

    //public StatsFragment(Context context) {
        //mContext = context;
    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        spinner = v.findViewById(R.id.stats_spinner);
        spinner.setOnItemSelectedListener(this);
        courseSpinner = v.findViewById(R.id.stats_course_spinner);
        courseSpinner.setOnItemSelectedListener(this);
        nameTextView = v.findViewById(R.id.stats_player_textview);
        recentRoundsTextView = v.findViewById(R.id.recent_rounds_textView);
        courseTextView = v.findViewById(R.id.courseTextView);
        graph = v.findViewById(R.id.graph);
        Log.d(TAG, "onCreateView: " + myNames);
        //Log.d(TAG, "onCreateView: " + mContext);

        if (savedInstanceState != null) {
            myNames = savedInstanceState.getStringArray("namesArray");
            myCourses = savedInstanceState.getStringArray("coursesArray");
            allRecords = (ArrayList<GolfRecord>) savedInstanceState.getSerializable("recordsList");
        }
        GolfViewModel viewModel = new ViewModelProvider(requireActivity()).get(GolfViewModel.class);
        viewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<GolfRecord>>() {
            @Override
            public void onChanged(List<GolfRecord> golfRecords) {
                Log.d(TAG, "onChanged: " + "set courses adapter");
                ArrayList<GolfRecord> myRecords = new ArrayList<>(golfRecords);
                setRecords(myRecords);
            }
        });

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
                setMyCourses(arr);
            }
        });







        return v;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putStringArray("namesArray", myNames);
        outState.putStringArray("coursesArray", myCourses);
        outState.putSerializable("recordsList", allRecords);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

            name = "";
            course = "";
            if (spinner.getSelectedItem() != null) {
                name = spinner.getSelectedItem().toString();
            }
            if (spinner.getSelectedItem() != null) {
                course = courseSpinner.getSelectedItem().toString();
            }

            //name = (String) adapterView.getItemAtPosition(pos);

            courseTextView.setText(course);
            nameTextView.setText(name);




        graph.removeAllSeries();

        ArrayList<GolfRecord> playerResults = new ArrayList<>();
        if (course.equals("All Courses")) {
            for (GolfRecord g : allRecords) {
                if (g.getName().equals(name)) {
                    playerResults.add(g);
                }
            }
        } else {
            for (GolfRecord g : allRecords) {
                if (g.getName().equals(name) && g.getCourse().equals(course)) {
                    playerResults.add(g);
                }
            }
        }

        Collections.sort(playerResults, new GolfRecord.DateComparator());
        int size = 5;
        if (playerResults.size() < 5) {
            size = playerResults.size();
        }
        String lastFiveResult = "";
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                lastFiveResult = lastFiveResult + playerResults.get(i).getScore();
            } else {
                lastFiveResult = lastFiveResult + ", " + playerResults.get(i).getScore();
            }
        }
        recentRoundsTextView.setText(lastFiveResult);
        //reverse for the graph
        Collections.reverse(playerResults);


        ArrayList<Integer> rollingAverage;
        rollingAverage = getRollingAverage(playerResults);

        DataPoint[] myDataPoints = new DataPoint[rollingAverage.size()];

        for (int i = 0; i < rollingAverage.size(); i++) {
            DataPoint dp = new DataPoint(i, rollingAverage.get(i));
            myDataPoints[i] = dp;
        }
        for (DataPoint d : myDataPoints) {
            Log.d(TAG, "onItemSelected: " + d);
        }


        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myDataPoints);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        /*viewport.setMaxXAxisSize(series.getHighestValueX());
        viewport.setMaxYAxisSize(series.getHighestValueY());*/
       viewport.setMaxX(series.getHighestValueX());
       viewport.setMaxY(series.getHighestValueY() + 10);
       viewport.setMinY(series.getLowestValueY() - 10);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Least recent to most recent");
        //gridLabel.setVerticalAxisTitle("Average score");
        gridLabel.setHorizontalLabelsVisible(false);


        graph.addSeries(series);



        //Put code here to populate the UI from the database
    }

    public ArrayList<Integer> getRollingAverage(ArrayList<GolfRecord> theRecords) {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < theRecords.size(); i++) {
            if (i == 0) {
                result.add(theRecords.get(i).getScore());
            } else if (i == 1) {
                result.add((theRecords.get(i).getScore() + theRecords.get(i - 1).getScore()) / 2);
            } else {
                result.add((theRecords.get(i).getScore() + theRecords.get(i - 1).getScore() + theRecords.get(i - 2).getScore()) / 3);
            }
        }
        Log.d(TAG, "getRollingAverage: " + result);
        return result;



   /*     ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < theRecords.size(); i++) {
            int rollingAverageSum = 0;
            for (int j = i; j > i - 3 && j > 0; j--) {
                rollingAverageSum = rollingAverageSum + theRecords.get(j).getScore();
            }
            int averageScore;
            if (i == 0) {
                averageScore = rollingAverageSum / 1;
            } else if (i == 1) {
                averageScore = rollingAverageSum / 2;
            } else {
                averageScore = rollingAverageSum / 3;
            }


            result.add(averageScore);
        }
        Log.d(TAG, "getRollingAverage: " + result);
        return result;*/
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    }

    public void setRecords(ArrayList<GolfRecord> myRecords) {
        allRecords = myRecords;
    }

    public void setNames(String[] arr) {
            myNames = arr;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, myNames);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void setMyCourses(String[] myParamCourses) {
        String[] newStringArray = new String[myParamCourses.length + 1];
        newStringArray[0] = "All Courses";
        for (int i = 0; i < myParamCourses.length; i++) {
            newStringArray[i + 1] = myParamCourses[i];
        }
        this.myCourses = newStringArray;
        ArrayAdapter<String> spinnerCourseArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, myCourses);
        courseSpinner.setAdapter(spinnerCourseArrayAdapter);
    }
}
