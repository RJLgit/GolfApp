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
import java.util.Date;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GolfAdapter extends RecyclerView.Adapter<GolfAdapter.GolfViewHolder> {

    private static final String TAG = "GolfAdapter";
    private List<GolfRecord> mUnFilteredData = new ArrayList<>();
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

    @Override
    public void onBindViewHolder(@NonNull GolfViewHolder holder, int position) {
        holder.nameTextView.setText(mFilteredData.get(position).getName());
        holder.courseTextView.setText(mFilteredData.get(position).getCourse());
        holder.dateTextView.setText(mFilteredData.get(position).getDate().toString());
        holder.parTextView.setText(mFilteredData.get(position).getPar() + "");
        holder.scoreTextView.setText(mFilteredData.get(position).getScore() + "");
    }

    @Override
    public int getItemCount() {
        return mFilteredData.size();
    }

    public void setmData(List<GolfRecord> mData) {
        this.mUnFilteredData = mData;
        mFilteredData = mUnFilteredData;
        notifyDataSetChanged();
    }

    public void updateFilteredData(List<GolfRecord> mData) {
        mFilteredData = mData;
        notifyDataSetChanged();
    }



    public void filterDates(String s) {

        if (s.equals("All rounds")) {
                updateFilteredData(mUnFilteredData);
        } else if (s.equals("Last 6 months")) {
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
            Log.d(TAG, "filterData: " + filtered);
            Log.d(TAG, "filterDates: " + mUnFilteredData);
            updateFilteredData(filtered);
        } else if (s.equals("Last 3 months")) {
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
            Log.d(TAG, "filterData: " + filtered);
            updateFilteredData(filtered);
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
