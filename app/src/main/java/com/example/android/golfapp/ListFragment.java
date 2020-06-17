package com.example.android.golfapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.golfapp.Data.AppExecutors;
import com.example.android.golfapp.Data.GolfDatabase;
import com.example.android.golfapp.Data.GolfRecord;
import com.example.android.golfapp.Data.GolfViewModel;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "ListFragment";


    RecyclerView myRecyclerView;
    
    GolfRecord deletedItem = null;

    GolfAdapter adapter;
    SharedPreferences sharedPreferences;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        myRecyclerView = v.findViewById(R.id.recyclerview);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new GolfAdapter(getContext());
        myRecyclerView.setAdapter(adapter);

        final GolfViewModel viewModel = new ViewModelProvider(requireActivity()).get(GolfViewModel.class);

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
                        deletedItem = records.get(position);
                        viewModel.deleteRecord(deletedItem);
                        Snackbar.make(viewHolder.itemView, "Removed item", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        viewModel.insertRecord(deletedItem);
                                    }
                                }).show();
                    }
                });
            }
        }).attachToRecyclerView(myRecyclerView);

        viewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<GolfRecord>>() {
            @Override
            public void onChanged(List<GolfRecord> golfRecords) {
                Log.d(TAG, "onChanged: " + "updates from view model");
                adapter.setmData(golfRecords);
                adapter.filterData(sharedPreferences.getString("time_filter_preference", "All rounds"), sharedPreferences.getStringSet("player_filter_preference", null),
                        sharedPreferences.getString("sort_preference", "Most recent"));
            }
        });

        return v;
    }
    //When the fragment is attached to the activity then the context is checked to make sure it is of type MainActivity.
    //If it is then the context is cast into MainActivity and showMenus() is called on it to show the menu item as sort/filter is required in this fragment.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            Log.d(TAG, "onAttach: ");
            activity.showMenus();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EnterFragmentListener");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("time_filter_preference")) {
            adapter.filterData(sharedPreferences.getString("time_filter_preference", "All rounds"), sharedPreferences.getStringSet("player_filter_preference", null)
                    , sharedPreferences.getString("sort_preference", "Most recent"));
        }
        if (key.equals("player_filter_preference")) {
            adapter.filterData(sharedPreferences.getString("time_filter_preference", "All rounds"), sharedPreferences.getStringSet("player_filter_preference", null)
                    , sharedPreferences.getString("sort_preference", "Most recent"));
        }
        if (key.equals("sort_preference")) {
            adapter.filterData(sharedPreferences.getString("time_filter_preference", "All rounds"), sharedPreferences.getStringSet("player_filter_preference", null)
                    , sharedPreferences.getString("sort_preference", "Most recent"));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

}
