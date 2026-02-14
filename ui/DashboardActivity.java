package com.example.smartfoods.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoods.R;
import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.ocr.OcrCaptureActivity;
import com.example.smartfoods.data.WadaRepository;
import com.example.smartfoods.ui.profile.ProfileActivity;
import com.example.smartfoods.MainActivity;
import com.example.smartfoods.data.ProfileManager;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private TextView nextCompetitionText;
    private Button scanButton;
    private Button historyButton;
    private Button chooseProfileButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeText = (TextView) findViewById(R.id.welcomeText);
        nextCompetitionText = (TextView) findViewById(R.id.nextCompetitionText);
        scanButton = (Button) findViewById(R.id.scanButton);
        historyButton = (Button) findViewById(R.id.historyButton);
        chooseProfileButton = (Button) findViewById(R.id.chooseProfileButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Opening scanner...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashboardActivity.this, OcrCaptureActivity.class);
                i.putExtra(OcrCaptureActivity.AutoFocus, true);
                i.putExtra(OcrCaptureActivity.UseFlash, false);
                startActivity(i);
            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
            }
        });

        if (chooseProfileButton != null) {
            chooseProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DashboardActivity.this, ProfileChooserActivity.class));
                }
            });
        }

        // If navigated here after creating a new profile, show a confirmation toast
        if (getIntent() != null && getIntent().getBooleanExtra("new_profile", false)) {
            Toast.makeText(this, "New profile created", Toast.LENGTH_SHORT).show();
        }

        loadAndBindProfileOrRedirect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh UI in case user changed active profile from chooser
        loadAndBindProfileOrRedirect();
    }

    private void loadAndBindProfileOrRedirect() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                new WadaRepository(getApplicationContext()).seedIfEmpty();
                long activeId = ProfileManager.getActiveProfileId(getApplicationContext());
                Profile p = null;
                if (activeId > 0) {
                    p = AppDatabase.getInstance(getApplicationContext()).profileDao().getById(activeId);
                }
                if (p == null) {
                    p = AppDatabase.getInstance(getApplicationContext()).profileDao().getPrimaryProfile();
                }
                final Profile profile = p;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (profile == null) {
                            Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
                            startActivity(i);
                            finish();
                            return;
                        }
                        welcomeText.setText("Welcome, " + profile.displayName);
                        nextCompetitionText.setText("Sport: " + profile.sport);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create_profile) {
            // Navigate directly to profile creation screen
            Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


