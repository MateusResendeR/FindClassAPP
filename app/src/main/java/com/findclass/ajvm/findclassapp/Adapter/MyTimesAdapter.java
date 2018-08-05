package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.Time;
import com.findclass.ajvm.findclassapp.R;

import java.util.List;

public class MyTimesAdapter extends RecyclerView.Adapter<MyTimesAdapter.MyViewHolder> {

    private List<Time> myTimes;
    private Context context;

    public MyTimesAdapter(List<Time> myTimes, Context context) {
        this.myTimes = myTimes;
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
        Time time = myTimes.get(position);

        holder.timeStartTime.setText(time.getStartTime());
        holder.timeEndTime.setText(time.getEndTime());
        holder.timeDay.setText(time.getDay());

    }

    @Override
    public int getItemCount() {
        return myTimes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView timeStartTime;
        TextView timeEndTime;
        TextView timeDay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStartTime = itemView.findViewById(R.id.timeStartTimeTextView);
            timeEndTime = itemView.findViewById(R.id.timeEndTimeTextView);
            timeDay = itemView.findViewById(R.id.timeDayTextView);
        }
    }
}
