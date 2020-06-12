package com.example.android.golfapp;


import android.content.Context;
import android.os.Bundle;

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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
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
    TextView nameTextView;
    TextView recentRoundsTextView;
    GraphView graph;
    ArrayList<GolfRecord> allRecords = new ArrayList<>();
    Context mContext;
    String[] myNames;



    public StatsFragment() {
        // Required empty public constructor
    }

    public StatsFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        spinner = v.findViewById(R.id.stats_spinner);
        spinner.setOnItemSelectedListener(this);
        nameTextView = v.findViewById(R.id.stats_player_textview);
        recentRoundsTextView = v.findViewById(R.id.recent_rounds_textView);
        graph = v.findViewById(R.id.graph);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, myNames);
        spinner.setAdapter(spinnerArrayAdapter);


        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        String x = (String) adapterView.getItemAtPosition(pos);
        nameTextView.setText(x);
        graph.removeAllSeries();

        ArrayList<GolfRecord> playerResults = new ArrayList<>();

        for (GolfRecord g : allRecords) {
            if (g.getName().equals(x)) {
                playerResults.add(g);
            }
        }


        DataPoint[] myDataPoints = new DataPoint[playerResults.size()];

        for (int i = 0; i < playerResults.size(); i++) {
            DataPoint dp = new DataPoint(i + 1, playerResults.get(i).getScore());
            myDataPoints[i] = dp;
        }
        for (DataPoint d : myDataPoints) {
            Log.d(TAG, "onItemSelected: " + d);
        }


        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myDataPoints);

        graph.addSeries(series);
        //Put code here to populate the UI from the database
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
    }
}
