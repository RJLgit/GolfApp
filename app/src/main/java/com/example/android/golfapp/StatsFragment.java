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
    ArrayList<GolfRecord> playerResults = new ArrayList<>();


    public StatsFragment() {
        // Required empty public constructor
    }

    public StatsFragment(GolfDatabase mDb) {
        this.mDb = mDb;
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

        GolfViewModel viewModel = new ViewModelProvider(getActivity()).get(GolfViewModel.class);
        viewModel.getNames().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> golfNames) {
                Log.d(TAG, "onChanged: " + "updates from view model");
                if (getContext() != null) {
                    Set<String> removeDuplicatesSet = new HashSet<>(golfNames);
                    String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                    Log.d(TAG, "onChanged: " + arr);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arr);
                    spinner.setAdapter(spinnerArrayAdapter);
                }

            }
        });



        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        String x = (String) adapterView.getItemAtPosition(pos);
        nameTextView.setText(x);

        GolfViewModel viewModel = new ViewModelProvider(getActivity()).get(GolfViewModel.class);
        viewModel.getSpecificName(x).observe(getActivity(), new Observer<List<GolfRecord>>() {
            @Override
            public void onChanged(List<GolfRecord> golfRecords) {
                Log.d(TAG, "onChanged: " + "updates from view model");
                if (getContext() != null) {
                    playerResults = new ArrayList<>(golfRecords);
                }
            }
        });


        DataPoint[] myDataPoints = new DataPoint[playerResults.size()];

        for (int i = 0; i < playerResults.size(); i++) {
            DataPoint dp = new DataPoint(playerResults.get(i).getDate().getTime(), playerResults.get(i).getScore());
            myDataPoints[i] = dp;
        }
        for (DataPoint d : myDataPoints) {
            Log.d(TAG, "onItemSelected: " + d);
        }


        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myDataPoints);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1,4),
                new DataPoint(3,3),
                new DataPoint(5,6),
                new DataPoint(8,9)

        });


        graph.addSeries(series2);
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

}
