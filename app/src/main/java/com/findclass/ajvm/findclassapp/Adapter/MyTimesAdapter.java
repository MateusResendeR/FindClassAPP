package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.R;

import java.sql.Time;
import java.util.List;

public class MyTimesAdapter extends RecyclerView.Adapter<MyTimesAdapter.MyViewHolder> {

    private List<String> myStartTimes;
    private List<String> myEndTimes;
    private Context context;

    public MyTimesAdapter(List<String> myStartTimes, List<String> myEndTimes, Context context) {
        this.myStartTimes = myStartTimes;
        this.myEndTimes = myEndTimes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_my_times,
                parent,
                false
        );
        return new MyViewHolder(itemList);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String time1 = myStartTimes.get(position);
        String time2 = myEndTimes.get(position);

        holder.startTime.setText(time1);
        holder.endTime.setText(time2);

    }

    @Override
    public int getItemCount() {
        return myStartTimes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView startTime;
        TextView endTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.timeTextView1);
            endTime = itemView.findViewById(R.id.timeTextView2);
        }
    }
}
