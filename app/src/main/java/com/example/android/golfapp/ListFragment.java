package com.example.android.golfapp;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.android.golfapp.Data.GolfRecord;
import com.example.android.golfapp.Data.GolfViewModel;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "ListFragment";
    //RecyclerView variables
    RecyclerView myRecyclerView;
    GolfAdapter adapter;

    //Variable to hold golf records when swiped
    GolfRecord deletedItem = null;

    //Shared preferences object
    SharedPreferences sharedPreferences;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        //Creates the shared preferences object and assigns the listener, which is this fragment as it implements the OnSharedPreferenceChangeListener interface
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        //Creates the RV and adapter
        myRecyclerView = v.findViewById(R.id.recyclerview);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GolfAdapter(getContext());
        myRecyclerView.setAdapter(adapter);
        //Obtains a GolfViewModel to interact with the database
        final GolfViewModel viewModel = new ViewModelProvider(requireActivity()).get(GolfViewModel.class);
        //An ontouch listener which is attached to the recyclerview
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Deletes the golf record when swiped
                int position = viewHolder.getAdapterPosition();
                List<GolfRecord> records = adapter.getmData();
                deletedItem = records.get(position);
                viewModel.deleteRecord(deletedItem);
                //Pops up snackbar which allows the user to undo the deletion and add the record back to the database
                Snackbar.make(viewHolder.itemView, "Removed item", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewModel.insertRecord(deletedItem);
                            }
                        }).show();
            }
        }).attachToRecyclerView(myRecyclerView);

        //Viewmodel obtains the GolfRecord objects held in the database and sends them to the adapter for the RV
        viewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<GolfRecord>>() {
            @Override
            public void onChanged(List<GolfRecord> golfRecords) {
                Log.d(TAG, "onChanged: " + "updates from view model");
                adapter.setmData(golfRecords);
                adapter.filterData(sharedPreferences.getString(getString(R.string.preference_time_filter_key), "All rounds"), sharedPreferences.getStringSet(getString(R.string.preference_player_filter_key), null),
                        sharedPreferences.getString(getString(R.string.preference_sort_key), "Most recent"));
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

    //When shared preferences change then the adapter is sent the new preferences dictating how to filter and sort the recyclerview items
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            adapter.filterData(sharedPreferences.getString(getString(R.string.preference_time_filter_key), "All rounds"), sharedPreferences.getStringSet(getString(R.string.preference_player_filter_key), null)
                    , sharedPreferences.getString(getString(R.string.preference_sort_key), "Most recent"));
    }

    //Unregisters the Onsharedpreferencelistener when the fragment view is destroyed to avoid memory leaks
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

}
