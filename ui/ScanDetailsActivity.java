package com.example.smartfoods.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoods.R;
import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.entities.ScanResult;
import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.TextParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ScanDetailsActivity extends AppCompatActivity {

    private TextView productNameText;
    private TextView riskLevelText;
    private ListView substancesList;
    private TextView alwaysCount;
    private TextView inCompCount;
    private TextView sportSpecCount;

    private long profileId;
    private long timestampMs;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_details);

        productNameText = findViewById(R.id.productNameText);
        riskLevelText = findViewById(R.id.riskLevelText);
        substancesList = findViewById(R.id.substancesList);
        alwaysCount = findViewById(R.id.alwaysCount);
        inCompCount = findViewById(R.id.inCompCount);
        sportSpecCount = findViewById(R.id.sportSpecCount);

        profileId = getIntent().getLongExtra("profileId", 0);
        timestampMs = getIntent().getLongExtra("timestampMs", 0);
        productName = getIntent().getStringExtra("productName");

        if (productName != null) {
            productNameText.setText(productName);
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                final ScanResult sr = db
                        .scanResultDao()
                        .getLatestBefore(profileId, timestampMs);
                final Profile profile = (profileId > 0)
                        ? db.profileDao().getById(profileId)
                        : db.profileDao().getPrimaryProfile();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sr == null) {
                            riskLevelText.setText("No scan details available.");
                            substancesList.setAdapter(new android.widget.ArrayAdapter<>(
                                    ScanDetailsActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    new ArrayList<String>()
                            ));
                            alwaysCount.setText("Always: 0");
                            inCompCount.setText("In-Comp: 0");
                            sportSpecCount.setText("Sport-Specific: 0");
                            return;
                        }
                        riskLevelText.setText("Risk: " + sr.riskLevel);
                        List<String> substances = parseSubstances(sr.detectedSubstancesJson);
                        substancesList.setAdapter(new android.widget.ArrayAdapter<>(
                                ScanDetailsActivity.this,
                                android.R.layout.simple_list_item_1,
                                substances
                        ));

                        // Compute metrics using TextParser
                        try {
                            TextParser parser = new TextParser();
                            ArrayList<String> lines = new ArrayList<>();
                            if (sr.rawText != null && !sr.rawText.isEmpty()) {
                                lines.addAll(Arrays.asList(sr.rawText.split("\n")));
                            }
                            String sport = (profile != null && profile.sport != null) ? profile.sport : null;
                            TextParser.WadaDetectionResult det = parser.detectWadaSubstances(
                                    getApplicationContext(),
                                    lines,
                                    sport
                            );
                            alwaysCount.setText("Always: " + (det == null ? 0 : det.allTimes.size()));
                            inCompCount.setText("In-Comp: " + (det == null ? 0 : det.inCompetition.size()));
                            sportSpecCount.setText("Sport-Specific: " + (det == null ? 0 : det.sportSpecific.size()));
                        } catch (Exception e) {
                            alwaysCount.setText("Always: -");
                            inCompCount.setText("In-Comp: -");
                            sportSpecCount.setText("Sport-Specific: -");
                        }
                    }
                });
            }
        });
    }

    private List<String> parseSubstances(String json) {
        List<String> list = new ArrayList<>();
        if (json == null) return list;
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getString(i));
            }
        } catch (JSONException e) {
            // Fallback: show raw JSON snippet if parsing fails
            list.add(json);
        }
        return list;
    }
}
