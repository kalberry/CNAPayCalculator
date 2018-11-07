package com.example.kyle.cnapaycalculator;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class WorkDayEditorActivity extends AppCompatActivity {

    EditText etDate, etRegMHours, etRegEHours, etRegOHours, etOTMHours, etOTEHours, etOTOHours;
    Spinner dropdown;
    int workID;
    int dayId;
    ArrayList<String> date;
    AlertDialog.Builder alert;
    AlertDialog alertDialog;
    boolean temp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_day_editor);
        etRegMHours = (EditText) findViewById(R.id.etRegMHours);
        etRegEHours = (EditText) findViewById(R.id.etRegEHours);
        etRegOHours = (EditText) findViewById(R.id.etRegOHours);
        etOTMHours = (EditText) findViewById(R.id.etOTMHours);
        etOTEHours = (EditText) findViewById(R.id.etOTEHours);
        etOTOHours = (EditText) findViewById(R.id.etOTOHours);
        dropdown = findViewById(R.id.spinner1);

        Intent intent = getIntent();
        dayId = intent.getIntExtra("dayId", -1);
        date = intent.getStringArrayListExtra("dateArray");
        //Log.i("New Date", date.get(0));

        if (dayId != -1) {
            etRegMHours.setText(String.valueOf(PayPeriodActivity.dayTrackers.get(dayId).getRegMHours()));
            etRegEHours.setText(String.valueOf(PayPeriodActivity.dayTrackers.get(dayId).getRegEHours()));
            etRegOHours.setText(String.valueOf(PayPeriodActivity.dayTrackers.get(dayId).getRegOHours()));
            etOTMHours.setText(String.valueOf(PayPeriodActivity.dayTrackers.get(dayId).getOTMHours()));
            etOTEHours.setText(String.valueOf(PayPeriodActivity.dayTrackers.get(dayId).getOTEHours()));
            etOTOHours.setText(String.valueOf(PayPeriodActivity.dayTrackers.get(dayId).getOTOHours()));

        }

        ArrayAdapter dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, date);
        dropdown.setAdapter(dateAdapter);
    }

    public void create(View view)
    {
        final String date = dropdown.getSelectedItem().toString();
        final int regMHours = setNum(etRegMHours);
        final int regEHours = setNum(etRegEHours);
        final int regOHours = setNum(etRegOHours);
        final int OTMHours = setNum(etOTMHours);
        final int OTEHours = setNum(etOTEHours);
        final int OTOHours = setNum(etOTOHours);

        if (dayId != -1)
        {
            PayPeriodActivity.dayTrackers.get(dayId).setDate(date);
            PayPeriodActivity.dayTrackers.get(dayId).setRegMHours(regMHours);
            PayPeriodActivity.dayTrackers.get(dayId).setRegEHours(regEHours);
            PayPeriodActivity.dayTrackers.get(dayId).setRegOHours(regOHours);
            PayPeriodActivity.dayTrackers.get(dayId).setOTMHours(OTMHours);
            PayPeriodActivity.dayTrackers.get(dayId).setOTEHours(OTEHours);
            PayPeriodActivity.dayTrackers.get(dayId).setOTOHours(OTOHours);
        } else {
            PayPeriodActivity.dayTrackers.add(new DayTracker(date, regMHours, regEHours, regOHours, OTMHours, OTEHours, OTOHours));
        }
        finish();
    }

    public int setNum(EditText et)
    {
        int num;

        if (et.length() < 1)
        {
            num = 0;
        }
        else
        {
            num = Integer.parseInt(et.getText().toString());
        }

        return num;
    }
}
