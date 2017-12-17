package com.example.kru13.sokoview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    int speed = 0;
    int level = 1;
    boolean soundOn = true;
    boolean vibrationOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinnerLevel = (Spinner) findViewById(R.id.levels_spinner);

        ArrayAdapter<CharSequence> adapterSpinnerLevel = ArrayAdapter.createFromResource(this,
                R.array.levels_array, android.R.layout.simple_spinner_item);
        adapterSpinnerLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerSpeed = (Spinner) findViewById(R.id.speeds_spinner);

        ArrayAdapter<CharSequence> adapterSpinnerSpeed = ArrayAdapter.createFromResource(this,
                R.array.speeds_array, android.R.layout.simple_spinner_item);
        adapterSpinnerLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLevel.setAdapter(adapterSpinnerLevel);
        spinnerSpeed.setAdapter(adapterSpinnerSpeed);

        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                adapterView.getItemAtPosition(i);
                level = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                adapterView.getItemAtPosition(i);
                switch (i) {
                    case 0:
                        speed = 500;
                        break;
                    case 1:
                        speed = 300;
                        break;
                    case 2:
                        speed = 100;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Button button = (Button) findViewById(R.id.buttonPlay);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("speed", speed);
                dataBundle.putInt("level", level);
                dataBundle.putBoolean("sound", soundOn);
                dataBundle.putBoolean("vibration", vibrationOn);
                Intent gameActivity = new Intent(getApplicationContext(), MainActivity.class);
                gameActivity.putExtras(dataBundle);
                startActivity(gameActivity);
            }
        });

    }
}
