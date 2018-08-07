package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.Time_Date;
import com.findclass.ajvm.findclassapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AvailabilityListAdapter extends RecyclerView.Adapter<AvailabilityListAdapter.MyViewHolder> {
    private List<Time_Date> time_dates;
    private Context context;

    public AvailabilityListAdapter(List<Time_Date> listTimeDate, Context c) {
        this.time_dates = listTimeDate;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_availability,parent,false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Time_Date dt = time_dates.get(position);
        String dateString = dt.getDate_status().getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.date.setText( dateFormat.format(date));
        holder.day.setText(dt.getTime().getDay());
        holder.startTime.setText(dt.getTime().getStartTime());
        holder.endTime.setText(dt.getTime().getStartTime());
        holder.price.setText(dt.getTime().getPrice());



    }

    @Override
    public int getItemCount() {
        return time_dates.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, day, startTime, endTime, price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.textViewStartTime);
            endTime = itemView.findViewById(R.id.textViewEndTime);
            price = itemView.findViewById(R.id.textViewPrice);
            date = itemView.findViewById(R.id.textViewDate);
            day = itemView.findViewById(R.id.textViewDay);
        }
    }
}
