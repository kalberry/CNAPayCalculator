package com.example.kyle.cnapaycalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    EditText morningPaySetting;
    EditText eveningPaySetting;
    EditText overnightPaySetting;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Set layout values
        morningPaySetting = findViewById(R.id.morningPaySetting);
        eveningPaySetting = findViewById(R.id.eveningPaySetting);
        overnightPaySetting = findViewById(R.id.overnightPaySetting);

        //Load data
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.kyle.cnapaycalculator", Context.MODE_PRIVATE);

        morningPaySetting.setText(Float.toString(sharedPreferences.getFloat("morning", MainActivity.MORNING_DEFAULT_RATE)));
        eveningPaySetting.setText(Float.toString(sharedPreferences.getFloat("evening", MainActivity.EVENING_DEFAULT_RATE)));
        overnightPaySetting.setText(Float.toString(sharedPreferences.getFloat("overnight", MainActivity.OVERNIGHT_DEFAULT_RATE)));

        //Create generic listener
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                update(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        //Set watchers
        morningPaySetting.addTextChangedListener(watcher);
        eveningPaySetting.addTextChangedListener(watcher);
        overnightPaySetting.addTextChangedListener(watcher);


    }

    public void update(CharSequence s)
    {
        if (s.length() > 0) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.kyle.cnapaycalculator", Context.MODE_PRIVATE);

            sharedPreferences.edit().putFloat("morning", Float.parseFloat(morningPaySetting.getText().toString())).apply();
            sharedPreferences.edit().putFloat("evening", Float.parseFloat(eveningPaySetting.getText().toString())).apply();
            sharedPreferences.edit().putFloat("overnight", Float.parseFloat(overnightPaySetting.getText().toString())).apply();
        }
    }
}
