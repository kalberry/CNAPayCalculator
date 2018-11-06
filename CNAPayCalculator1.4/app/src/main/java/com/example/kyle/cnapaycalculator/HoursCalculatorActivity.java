package com.example.kyle.cnapaycalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class HoursCalculatorActivity extends Fragment {

    // Project Creator: Kyle Alberry
    // Project Description: Create an app that gets hours from user and translates into pay.

    // Version 1.0:   Basic foundation to calculate hours and gross pay for 0-80 hours.
    // Version 1.0.1: Adjusted small font error where the user enters hours.
    // Version 1.1:   Implemented overtime. Updated UI without clicking the button. Made UI Changes
    // Version 1.2:   Bug: Deleting hours causes some hours to still be in memory. Update UI with
    //                total overtime hours. Update UI with total overtime pay. Efficient background
    //                processing.
    // Version 1.3    Implement net pay with default settings. Settings menu with custom pay
    //                settings. Save settings and hours preferences.

    // Future Implimentations: clean up code, custom settings for net pay, better UI, Show small summary of taxes taken out
    //                         add bottom navigation, add pay period calculator with listview and listarrays


    //Declare global variables used through different functions
    static double morningHours, eveningHours, overnightHours, morningOTHours, eveningOTHours,
            overnightOTHours, regularHours, overtimeHours, totalHours, regularPay, overtimePay,
            grossPay, morningRate, eveningRate, overnightRate;
    static EditText morningHoursET, eveningHoursET, overnightHoursET, morningHoursOTET, eveningHoursOTET,
            overnightHoursOTET;
    static TextView totalHoursTV, totalHoursOutput, grossPayOutput, totalHoursOutputOTTV, grossPayOTTV,
            totalGrossPayTV, totalNetPayTV;
    SharedPreferences sharedPreferences;
    View rootView;

    public static final float MORNING_DEFAULT_RATE = 11.54f;
    public static final float EVENING_DEFAULT_RATE = 12.64f;
    public static final float OVERNIGHT_DEFAULT_RATE = 13.04f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_hours_calculator, container, false);

        //region Set layout variables
        morningHoursET = (EditText) rootView.findViewById(R.id.morningET);
        eveningHoursET = (EditText) rootView.findViewById(R.id.eveningET);
        overnightHoursET = (EditText) rootView.findViewById(R.id.overnightET);
        morningHoursOTET = (EditText) rootView.findViewById(R.id.morningOTET);
        eveningHoursOTET = (EditText) rootView.findViewById(R.id.eveningOTET);
        overnightHoursOTET = (EditText) rootView.findViewById(R.id.overnightOTET);
        totalHoursOutput = (TextView) rootView.findViewById(R.id.totalHoursOutputTV);
        grossPayOutput = (TextView) rootView.findViewById(R.id.grossPayTV);
        totalHoursOutputOTTV = (TextView) rootView.findViewById(R.id.totalHoursOutputOTTV);
        grossPayOTTV = (TextView) rootView.findViewById(R.id.grossPayOTTV);
        totalGrossPayTV = (TextView) rootView.findViewById(R.id.totalGrossPayTV);
        totalNetPayTV = (TextView) rootView.findViewById(R.id.totalNetPayTV);
        //endregion

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setHasOptionsMenu(true);

        //Create generic listener
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if (!s.equals('.'));
                    CalculateAll();
                if (s.length() > 0)
                    saveSharedPrefData(morningHoursET, eveningHoursET, overnightHoursET, morningHoursOTET, eveningHoursOTET, overnightHoursOTET);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //region Apply the watcher to the text boxes
        morningHoursET.addTextChangedListener(watcher);
        eveningHoursET.addTextChangedListener(watcher);
        overnightHoursET.addTextChangedListener(watcher);
        morningHoursOTET.addTextChangedListener(watcher);
        eveningHoursOTET.addTextChangedListener(watcher);
        overnightHoursOTET.addTextChangedListener(watcher);
        //endregion

        //Load data
        getSharedPrefData();

        return rootView;
    }

    public void saveSharedPrefData(EditText morningHoursET, EditText eveningHoursET, EditText overnightHoursET, EditText morningHoursOTET, EditText eveningHoursOTET, EditText overnightHoursOTET)
    {
        sharedPreferences = getActivity().getSharedPreferences("com.example.kyle.cnapaycalculator", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("morningRHours", morningHoursET.getText().toString()).apply();
        sharedPreferences.edit().putString("eveningRHours", eveningHoursET.getText().toString()).apply();
        sharedPreferences.edit().putString("overnightRHours", overnightHoursET.getText().toString()).apply();
        sharedPreferences.edit().putString("morningOHours", morningHoursOTET.getText().toString()).apply();
        sharedPreferences.edit().putString("eveningOHours", eveningHoursOTET.getText().toString()).apply();
        sharedPreferences.edit().putString("overnightOHours", overnightHoursOTET.getText().toString()).apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        getSharedPrefData();


    }

    public void getSharedPrefData()
    {
        sharedPreferences = getActivity().getSharedPreferences("com.example.kyle.cnapaycalculator", Context.MODE_PRIVATE);

        morningRate = sharedPreferences.getFloat("morning", MORNING_DEFAULT_RATE);
        eveningRate = sharedPreferences.getFloat("evening", EVENING_DEFAULT_RATE);
        overnightRate = sharedPreferences.getFloat("overnight", OVERNIGHT_DEFAULT_RATE);



        morningHoursET.setText(sharedPreferences.getString("morningRHours", ""));
        eveningHoursET.setText(sharedPreferences.getString("eveningRHours", ""));
        overnightHoursET.setText(sharedPreferences.getString("overnightRHours", ""));
        morningHoursOTET.setText(sharedPreferences.getString("morningOHours", ""));
        eveningHoursOTET.setText(sharedPreferences.getString("eveningOHours", ""));
        overnightHoursOTET.setText(sharedPreferences.getString("overnightOHours", ""));
    }

    public static void CalculateAll()
    {
        double morningHours, eveningHours, overnightHours, morningOTHours, eveningOTHours,
                overnightOTHours;

        //Convert user changed EditText box to double
        morningHours = ETtoDouble(morningHoursET);
        eveningHours = ETtoDouble(eveningHoursET);
        overnightHours = ETtoDouble(overnightHoursET);
        morningOTHours = ETtoDouble(morningHoursOTET);
        eveningOTHours = ETtoDouble(eveningHoursOTET);
        overnightOTHours = ETtoDouble(overnightHoursOTET);

        CalculatePay pay = new CalculatePay(morningHours, eveningHours, overnightHours,
                morningOTHours, eveningOTHours, overnightOTHours);

        totalHoursOutput.setText(String.format("%.2f", pay.GetRegularHours()));
        totalHoursOutputOTTV.setText(String.format("%.2f", pay.GetOvertimeHours()));

        grossPayOutput.setText(String.format("%s%.2f", "$", pay.GetRegularPay()));
        grossPayOTTV.setText(String.format("%s%.2f", "$", pay.GetOvertimePay()));
        totalGrossPayTV.setText(String.format("%s%.2f", "$", pay.GetGrossPay()));
        if (totalGrossPayTV != null)
            totalNetPayTV.setText(String.format("%s%.2f", "$", pay.GetNetPay()));
    }

    public static double ETtoDouble(EditText ET)
    {
        if (ET.length() > 0 && !ET.getText().toString().equals("."))
        {
            return Double.parseDouble(ET.getText().toString());
        } else
            return 0;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_hours_calculator, menu);
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
            case R.id.reset:
                morningHoursET.setText("0");
                eveningHoursET.setText("0");
                overnightHoursET.setText("0");
                morningHoursOTET.setText("0");
                eveningHoursOTET.setText("0");
                overnightHoursOTET.setText("0");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
