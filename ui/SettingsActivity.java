package com.example.smartfoods.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoods.PreferencesActivity;
import com.example.smartfoods.R;
import com.example.smartfoods.data.WadaRepository;

public class SettingsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button preferencesButton = (Button) findViewById(R.id.preferencesButton);
        Button seedWadaButton = (Button) findViewById(R.id.seedWadaButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, PreferencesActivity.class));
            }
        });

        seedWadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new WadaRepository(getApplicationContext()).seedIfEmpty();
                    }
                }).start();
            }
        });
    }
}


