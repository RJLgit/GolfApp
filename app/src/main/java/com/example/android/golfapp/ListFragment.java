package com.example.android.golfapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.golfapp.Data.AppExecutors;
import com.example.android.golfapp.Data.GolfDatabase;
import com.example.android.golfapp.Data.GolfRecord;
import com.example.android.golfapp.Data.GolfViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    RecyclerView myRecyclerView;
    GolfDatabase mDb;

    public ListFragment() {
        // Required empty public constructor
    }

    public ListFragment(GolfDatabase mDb) {
        this.mDb = mDb;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        myRecyclerView = v.findViewById(R.id.recyclerview);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final GolfAdapter adapter = new GolfAdapter(getContext());
        //Dummy data to test recyclerview
       /* ArrayList<GolfRecord> dummyData = new ArrayList<>();
        Date date = new Date();
        GolfRecord g1 = new GolfRecord("Bob", "baron", 72, 94, date);
        GolfRecord g2 = new GolfRecord("Tom", "baroness", 71, 102, date);
        dummyData.add(g1);
        dummyData.add(g2);*/
        myRecyclerView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<GolfRecord> records = adapter.getmData();
                        mDb.golfDao().deleteTask(records.get(position));
                    }
                });
            }
        }).attachToRecyclerView(myRecyclerView);

        GolfViewModel viewModel = new ViewModelProvider(getActivity()).get(GolfViewModel.class);
        viewModel.getRecords().observe(getActivity(), new Observer<List<GolfRecord>>() {
            @Override
            public void onChanged(List<GolfRecord> golfRecords) {
                Log.d(TAG, "onChanged: " + "updates from view model");
                adapter.setmData(golfRecords);
            }
        });


        return v;
    }

}
