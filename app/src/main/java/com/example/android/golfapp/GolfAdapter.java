package com.example.android.golfapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.golfapp.Data.GolfRecord;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GolfAdapter extends RecyclerView.Adapter<GolfAdapter.GolfViewHolder> {

    private static final String TAG = "GolfAdapter";
    //Variable to hold all the golf records
    private List<GolfRecord> mUnFilteredData = new ArrayList<>();
    //Variable to hold the golf records after they are filtered and sorted
    private List<GolfRecord> mFilteredData = new ArrayList<>();
    private Context mContext;

    public GolfAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public GolfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_list_item, parent, false);
        return new GolfViewHolder(view);
    }

    //Sets the viewholder views to hold the correct data for each item in the recyclerview
    @Override
    public void onBindViewHolder(@NonNull GolfViewHolder holder, int position) {
        holder.nameTextView.setText("Player: " + mFilteredData.get(position).getName());
        holder.courseTextView.setText("Course: " + mFilteredData.get(position).getCourse());
        holder.dateTextView.setText(getDateString(mFilteredData.get(position).getDate()));
        holder.parTextView.setText("Par of: " + mFilteredData.get(position).getPar());
        holder.scoreTextView.setText("Scored: " + mFilteredData.get(position).getScore());
    }

    //Helper method to format the date into a date string to display in the viewholder
    private String getDateString(Date d) {
        String result = "";
        result = result + d.getDate() + "/" + (d.getMonth() + 1) + "/" + (d.getYear() + 1900);
        return result;
    }

    @Override
    public int getItemCount() {
        return mFilteredData.size();
    }

    //Sets the data of the adapter. This is done from the list fragment class using a viewmodel to query the database.
    //It sets the unfiltered and the filtered data variables to be the data passed to it
    //The notifydatasetchanged tells the recyclerview/adapter that the data has changed and to reload it
    public void setmData(List<GolfRecord> mData) {
        this.mUnFilteredData = mData;
        mFilteredData = mUnFilteredData;
        notifyDataSetChanged();
    }

    public void updateFilteredData(List<GolfRecord> mData) {
        mFilteredData = mData;
        notifyDataSetChanged();
    }

    //This method is also called from the ListFragment. When the shared preferences change then the data is filtered/sorted according to these preferences
    //The first parameter is the filter by dates. The second if the filter by name, which is a set of names to include in the recyclerview
    //The third is the sort string to tell the adapter how to sort the data.
    public void filterData(String s, Set<String> x, String sort) {
        if (s.equals(mContext.getString(R.string.adapter_filter_all_rounds))) {
            ArrayList<GolfRecord> filtered = new ArrayList<>();
            for (GolfRecord g : mUnFilteredData) {
                    filtered.add(g);
            }
            ArrayList<GolfRecord> secondFiltered = new ArrayList<>();
            for (GolfRecord g2 : filtered) {
                String name = g2.getName();
                if (x != null && x.contains(name)) {
                    secondFiltered.add(g2);
                }
            }
            sortData(sort, secondFiltered);
            updateFilteredData(secondFiltered);
        } else if (s.equals(mContext.getString(R.string.adapter_filter_last_6_months))) {
            Log.d(TAG, "filterData: 6");
            ArrayList<GolfRecord> filtered = new ArrayList<>();
            Date currentDate = new Date();
            LocalDate currentLocalDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Log.d(TAG, "filterDates: " + currentLocalDate);
            for (GolfRecord g : mUnFilteredData) {
                Date recordDate = g.getDate();
                LocalDate currentRecordDate = recordDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Log.d(TAG, "filterDates: " + currentRecordDate);
                Period period = Period.between(currentLocalDate, currentRecordDate);
                Log.d(TAG, "filterDates: " + period);
                if (period.getYears() == 0 && period.getMonths() > -6 ) {
                    filtered.add(g);
                }
            }
            ArrayList<GolfRecord> secondFiltered = new ArrayList<>();
            for (GolfRecord g2 : filtered) {
                String name = g2.getName();
                if (x != null && x.contains(name)) {
                    secondFiltered.add(g2);
                }
            }
            sortData(sort, secondFiltered);
            updateFilteredData(secondFiltered);
            Log.d(TAG, "filterData: " + filtered);
            Log.d(TAG, "filterDates: " + mUnFilteredData);
        } else if (s.equals(mContext.getString(R.string.adapter_filter_last_3_months))) {
            Log.d(TAG, "filterData: 3");
            ArrayList<GolfRecord> filtered = new ArrayList<>();
            Date currentDate = new Date();
            LocalDate currentLocalDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            for (GolfRecord g : mUnFilteredData) {
                Date recordDate = g.getDate();
                LocalDate currentRecordDate = recordDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Period period = Period.between(currentLocalDate, currentRecordDate);
                if (period.getYears() == 0 && period.getMonths() > -3) {
                    filtered.add(g);
                }
            }
            ArrayList<GolfRecord> secondFiltered = new ArrayList<>();
            for (GolfRecord g2 : filtered) {
                String name = g2.getName();
                if (x != null && x.contains(name)) {
                    secondFiltered.add(g2);
                }
            }
            sortData(sort, secondFiltered);
            updateFilteredData(secondFiltered);
            Log.d(TAG, "filterData: " + filtered);
        }
    }

    public void sortData(String s, ArrayList<GolfRecord> arrayList) {
        if (s.equals("Most recent")) {
            Collections.sort(arrayList, new GolfRecord.DateComparator());
        } if (s.equals("Player - alphabetical")) {
            Collections.sort(arrayList, new GolfRecord.NameComparator());
        } if (s.equals("Score")) {
            Collections.sort(arrayList, new GolfRecord.ScoreComparator());
        }
    }

    public List<GolfRecord> getmData() {
        return mFilteredData;
    }

    class GolfViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView nameTextView;
        TextView courseTextView;
        TextView parTextView;
        TextView scoreTextView;

        public GolfViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_item_text_view);
            nameTextView = itemView.findViewById(R.id.name_item_text_view);
            courseTextView = itemView.findViewById(R.id.course_item_text_view);
            parTextView = itemView.findViewById(R.id.par_item_text_view);
            scoreTextView = itemView.findViewById(R.id.score_item_text_view);
        }
    }
}
