package com.example.kyle.cnapaycalculator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;

public class PayPeriodActivity extends Fragment {

    int year, month, day;
    static DayTrackerListAdapter adapter;
    static ArrayAdapter<String> dateAdapter;
    static ArrayList<DayTracker> dayTrackers;
    static ArrayList<String> date;
    Intent intent;
    TextView payPeriodStartDate, payPeriodRange;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar cal;
    CalculatePay pay;
    TextView GrossPayTV, NetPayTV, PayDateTV;
    ListView listView;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_pay_period, container, false);

        listView = rootView.findViewById(R.id.listView);
        payPeriodStartDate = rootView.findViewById(R.id.payPeriodStartDate);
        payPeriodRange = rootView.findViewById(R.id.payPeriodRange);
        NetPayTV = rootView.findViewById(R.id.NetPayTV);
        PayDateTV = rootView.findViewById(R.id.PayDateTV);
        setHasOptionsMenu(true);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), WorkDayEditorActivity.class);
                intent.putExtra("dateArray", date);
                startActivity(intent);
            }
        });

        //Add the objects to an ArrayList
        dayTrackers = new ArrayList<>();
        date = new ArrayList<>();

        adapter = new DayTrackerListAdapter(getActivity(), R.layout.adapter_view_layout, dayTrackers);
        listView.setAdapter(adapter);

        dateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, date);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("dayId", position);
                intent.putExtra("dateArray", date);
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this day?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dayTrackers.remove(position);
                                adapter.notifyDataSetChanged();
                                calcPay();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        payPeriodStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                Calendar startCal = new GregorianCalendar();
                startCal.set(Calendar.DAY_OF_MONTH, d);
                startCal.set(Calendar.MONTH, m);
                startCal.set(Calendar.YEAR, y);

                Calendar newCal = new GregorianCalendar();
                String startDate = (m + 1) + "/" + d + "/" + y;
                newCal.set(Calendar.DAY_OF_MONTH, d + 13);
                newCal.set(Calendar.MONTH, m);
                newCal.set(Calendar.YEAR, y);
                String endDate = (newCal.get(Calendar.MONTH) + 1) + "/" + newCal.get(Calendar.DAY_OF_MONTH) + "/" + newCal.get(Calendar.YEAR);

                Calendar payDayCal = new GregorianCalendar();
                payDayCal.set(Calendar.DAY_OF_MONTH, d + 20);
                payDayCal.set(Calendar.MONTH, m);
                payDayCal.set(Calendar.YEAR, y);
                String payDate = (payDayCal.get(Calendar.MONTH) + 1) + "/" + payDayCal.get(Calendar.DAY_OF_MONTH) + "/" + payDayCal.get(Calendar.YEAR);

                payPeriodStartDate.setText(startDate);
                payPeriodRange.setText(startDate + " - " + endDate);
                PayDateTV.setText(payDate + ":");

                dateAdapter.clear();
                setDateArray(startCal);
            }
        };



        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pay_period, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.deleteAll:
                adapter.clear();
                calcPay();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setDateArray(Calendar cal)
    {
        for (int i = 0; i < 14; i++) {
            date.add((cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        }

        dateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

        calcPay();
    }

    public void calcPay()
    {
        double regMHours = 0, regEHours = 0, regOHours = 0, OTMHours = 0, OTEHours = 0, OTOHours = 0;

        for(int i = 0; i < adapter.getCount(); i++)
        {
            regMHours += adapter.getItem(i).getRegMHours();
            regEHours += adapter.getItem(i).getRegEHours();
            regOHours += adapter.getItem(i).getRegOHours();
            OTMHours += adapter.getItem(i).getOTMHours();
            OTEHours += adapter.getItem(i).getOTEHours();
            OTOHours += adapter.getItem(i).getOTOHours();
        }

        pay = new CalculatePay(regMHours, regEHours, regOHours, OTMHours, OTEHours, OTOHours);

        NetPayTV.setText(String.format("%s%.2f", "$", pay.GetNetPay()));
    }
}


