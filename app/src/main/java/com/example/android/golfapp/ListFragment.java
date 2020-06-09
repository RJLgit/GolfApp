package com.example.android.golfapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.golfapp.Data.GolfDatabase;
import com.example.android.golfapp.Data.GolfRecord;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
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

        GolfAdapter adapter = new GolfAdapter(getContext());
        //Dummy data to test recyclerview
       /* ArrayList<GolfRecord> dummyData = new ArrayList<>();
        Date date = new Date();
        GolfRecord g1 = new GolfRecord("Bob", "baron", 72, 94, date);
        GolfRecord g2 = new GolfRecord("Tom", "baroness", 71, 102, date);
        dummyData.add(g1);
        dummyData.add(g2);*/

        adapter.setmData(mDb.golfDao().loadAllTasks());
        myRecyclerView.setAdapter(adapter);


        return v;
    }

}
