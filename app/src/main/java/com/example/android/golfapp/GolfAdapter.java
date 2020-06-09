package com.example.android.golfapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.golfapp.Data.GolfRecord;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GolfAdapter extends RecyclerView.Adapter<GolfAdapter.GolfViewHolder> {

    private List<GolfRecord> mData = new ArrayList<>();
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
        holder.nameTextView.setText(mData.get(position).getName());
        holder.courseTextView.setText(mData.get(position).getCourse());
        holder.dateTextView.setText(mData.get(position).getDate().toString());
        holder.parTextView.setText(mData.get(position).getPar() + "");
        holder.scoreTextView.setText(mData.get(position).getScore() + "");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmData(List<GolfRecord> mData) {
        this.mData = mData;
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
