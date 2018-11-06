package com.example.kyle.cnapaycalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

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


    //Declare global variables used through different functions
    double morningHours, eveningHours, overnightHours, morningOTHours, eveningOTHours,
            overnightOTHours, regularHours, overtimeHours, totalHours, regularPay, overtimePay,
            grossPay, morningRate, eveningRate, overnightRate;
    EditText morningHoursET, eveningHoursET, overnightHoursET, morningHoursOTET, eveningHoursOTET,
            overnightHoursOTET;
    TextView totalHoursTV, totalHoursOutput, grossPayOutput, totalHoursOutputOTTV, grossPayOTTV,
            totalGrossPayTV, totalNetPayTV;
    SharedPreferences sharedPreferences;

    public static final float MORNING_DEFAULT_RATE = 11.54f;
    public static final float EVENING_DEFAULT_RATE = 12.64f;
    public static final float OVERNIGHT_DEFAULT_RATE = 13.04f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set layout variables
        morningHoursET = (EditText) findViewById(R.id.morningET);
        eveningHoursET = (EditText) findViewById(R.id.eveningET);
        overnightHoursET = (EditText) findViewById(R.id.overnightET);
        morningHoursOTET = (EditText) findViewById(R.id.morningOTET);
        eveningHoursOTET = (EditText) findViewById(R.id.eveningOTET);
        overnightHoursOTET = (EditText) findViewById(R.id.overnightOTET);
        totalHoursOutput = (TextView) findViewById(R.id.totalHoursOutputTV);
        grossPayOutput = (TextView) findViewById(R.id.grossPayTV);
        totalHoursOutputOTTV = (TextView) findViewById(R.id.totalHoursOutputOTTV);
        grossPayOTTV = (TextView) findViewById(R.id.grossPayOTTV);
        totalGrossPayTV = (TextView) findViewById(R.id.totalGrossPayTV);
        totalNetPayTV = (TextView) findViewById(R.id.totalNetPayTV);

        //Load data
        getSharedPrefData();

        //Create generic listener
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CalculateAll(morningHoursET, eveningHoursET, overnightHoursET, morningHoursOTET,
                        eveningHoursOTET, overnightHoursOTET, totalHoursOutput, grossPayOutput,
                        totalHoursOutputOTTV, grossPayOTTV, totalGrossPayTV, totalNetPayTV,
                        morningRate, eveningRate, overnightRate);
                if (s.length() > 0)
                    saveSharedPrefData(morningHoursET, eveningHoursET, overnightHoursET, morningHoursOTET, eveningHoursOTET, overnightHoursOTET);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //Apply the watcher to the text boxes
        morningHoursET.addTextChangedListener(watcher);
        eveningHoursET.addTextChangedListener(watcher);
        overnightHoursET.addTextChangedListener(watcher);
        morningHoursOTET.addTextChangedListener(watcher);
        eveningHoursOTET.addTextChangedListener(watcher);
        overnightHoursOTET.addTextChangedListener(watcher);
    }

    public void saveSharedPrefData(EditText morningHoursET, EditText eveningHoursET, EditText overnightHoursET, EditText morningHoursOTET, EditText eveningHoursOTET, EditText overnightHoursOTET)
    {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.kyle.cnapaycalculator", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("morningRHours", morningHoursET.getText().toString()).apply();
        sharedPreferences.edit().putString("eveningRHours", eveningHoursET.getText().toString()).apply();
        sharedPreferences.edit().putString("overnightRHours", overnightHoursET.getText().toString()).apply();
        sharedPreferences.edit().putString("morningOHours", morningHoursOTET.getText().toString()).apply();
        sharedPreferences.edit().putString("eveningOHours", eveningHoursOTET.getText().toString()).apply();
        sharedPreferences.edit().putString("overnightOHours", overnightHoursOTET.getText().toString()).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSharedPrefData();
    }

    public void getSharedPrefData()
    {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.kyle.cnapaycalculator", Context.MODE_PRIVATE);

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

    public static void CalculateAll(EditText morningHoursET, EditText eveningHoursET,
                                    EditText overnightHoursET, EditText morningHoursOTET,
                                    EditText eveningHoursOTET, EditText overnightHoursOTET,
                                    TextView totalHoursOutput, TextView grossPayOutput,
                                    TextView totalHoursOutputOTTV, TextView grossPayOTTV,
                                    TextView totalGrossPayTV, TextView totalNetPayTV,
                                    double morningRate, double eveningRate, double overnightRate)
    {
        double morningHours, eveningHours, overnightHours, morningOTHours, eveningOTHours,
                overnightOTHours, regularPay, overtimePay, grossPay, netPay, FICA, federalTax,
                stateTax;
        double regularHours, overtimeHours, totalHours;

        //Convert user changed EditText box to double
        morningHours = ETtoDouble(morningHoursET);
        eveningHours = ETtoDouble(eveningHoursET);
        overnightHours = ETtoDouble(overnightHoursET);
        morningOTHours = ETtoDouble(morningHoursOTET);
        eveningOTHours = ETtoDouble(eveningHoursOTET);
        overnightOTHours = ETtoDouble(overnightHoursOTET);

        //Calculate hours
        regularHours = morningHours + eveningHours + overnightHours;
        overtimeHours = morningOTHours + eveningOTHours + overnightOTHours;
        totalHours = regularHours + overtimeHours;

        //Calculate gross pay
        regularPay = (morningHours * morningRate) + (eveningHours * eveningRate) +
                (overnightHours * overnightRate);
        overtimePay = (morningOTHours * morningRate * 1.5) + (eveningOTHours * eveningRate * 1.5) +
                (overnightOTHours * overnightRate * 1.5);
        grossPay = regularPay + overtimePay;

        //calculate taxes
        FICA = grossPay * 0.0765;
        double withhold = 952.5;
        double plusXofExcess = .12;
        double federalTaxGross = grossPay-2*159.6;
        double periodsPerYear = 26;
        double excessOver = 13225;
        federalTax = (withhold+(plusXofExcess*(federalTaxGross*periodsPerYear-excessOver)))/periodsPerYear;
        stateTax = federalTaxGross * .0882;

        //Calculate net pay
        netPay = (grossPay - FICA - stateTax - federalTax);

        totalHoursOutput.setText(String.format("%.2f", regularHours));
        totalHoursOutputOTTV.setText(String.format("%.2f", overtimeHours));

        grossPayOutput.setText(String.format("%s%.2f", "$", regularPay));
        grossPayOTTV.setText(String.format("%s%.2f", "$", overtimePay));
        totalGrossPayTV.setText(String.format("%s%.2f", "$", grossPay));
        if (totalGrossPayTV != null)
            totalNetPayTV.setText(String.format("%s%.2f", "$", netPay));
    }

    public static double ETtoDouble(EditText ET)
    {
        if (ET.length() > 0)
        {
            return Double.parseDouble(ET.getText().toString());
        } else
            return 0;

    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.settings)
        {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);

            return true;
        } else {
            return false;
        }
    }
}
