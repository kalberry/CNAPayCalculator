package com.example.kyle.cnapaycalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DayTrackerListAdapter extends ArrayAdapter<DayTracker> {

    private Context mContext;
    int mResource;

    public DayTrackerListAdapter(Context context, int resource, List<DayTracker> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String date = getItem(position).getDate();
        int regMHours = getItem(position).getRegMHours();
        int regEHours = getItem(position).getRegEHours();
        int regOHours = getItem(position).getRegOHours();
        int OTMHours = getItem(position).getOTMHours();
        int OTEHours = getItem(position).getOTEHours();
        int OTOHours = getItem(position).getOTOHours();

        DayTracker dayTracker = new DayTracker(date, regMHours, regEHours, regOHours, OTMHours, OTEHours, OTOHours);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDate = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvRegHours = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvOTHours = (TextView) convertView.findViewById(R.id.textView3);

        tvDate.setText(date);
        tvRegHours.setText("Regular:\nM: " + regMHours + "\nE: " + regEHours + "\nO: " + regOHours);
        tvOTHours.setText("OT:\nM: " + OTMHours + "\nE: " + OTEHours + "\nO: " + OTOHours);

        return convertView;
    }
}
