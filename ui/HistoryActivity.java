package com.example.smartfoods.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoods.R;
import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.entities.ConsumptionLog;
import com.example.smartfoods.data.entities.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<ConsumptionLog> logs = new ArrayList<>();
    private LogsAdapter adapter;
    private Map<Long, String> riskCache = new HashMap<>();
    private DateFormat dateFormat;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (ListView) findViewById(R.id.historyList);

        adapter = new LogsAdapter();
        listView.setAdapter(adapter);

        dateFormat = android.text.format.DateFormat.getMediumDateFormat(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                logs = AppDatabase.getInstance(getApplicationContext()).consumptionLogDao().getRecent(100);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                                if (position >= 0 && position < logs.size()) {
                                    ConsumptionLog selected = logs.get(position);
                                    Intent intent = new Intent(HistoryActivity.this, ScanDetailsActivity.class);
                                    intent.putExtra("profileId", selected.profileId);
                                    intent.putExtra("timestampMs", selected.timestampMs);
                                    intent.putExtra("productName", selected.productName);
                                    startActivity(intent);
                                }
                            }
                        });
                        adapter.notifyDataSetChanged();
                        fetchRisksAsync();
                    }
                });
            }
        });
    }

    private class LogsAdapter extends BaseAdapter {
        @Override
        public int getCount() { return logs.size(); }

        @Override
        public Object getItem(int position) { return logs.get(position); }

        @Override
        public long getItemId(int position) { return logs.get(position).id; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.item_history, parent, false);
                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.itemName);
                holder.subtitle = convertView.findViewById(R.id.itemSubtitle);
                holder.riskBadge = convertView.findViewById(R.id.riskBadge);
                holder.delete = convertView.findViewById(R.id.deleteButton);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ConsumptionLog log = logs.get(position);
            holder.name.setText(log.productName);
            String risk = riskCache.get(log.id);
            String dateStr = dateFormat.format(new Date(log.timestampMs));
            holder.subtitle.setText(dateStr + (risk != null ? (" • " + displayRiskText(risk)) : ""));
            // Update risk badge
            if (risk == null || risk.isEmpty()) {
                holder.riskBadge.setText("--");
                holder.riskBadge.setBackgroundColor(Color.DKGRAY);
            } else {
                String up = displayRiskText(risk).toUpperCase();
                holder.riskBadge.setText(up);
                holder.riskBadge.setBackgroundColor(colorForRisk(risk));
            }
            holder.delete.setOnClickListener(v -> deleteLogAt(position));
            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView subtitle;
            TextView riskBadge;
            Button delete;
        }
    }

    private void deleteLogAt(int position) {
        if (position < 0 || position >= logs.size()) return;
        final ConsumptionLog toDelete = logs.get(position);
        AsyncTask.execute(() -> {
            AppDatabase.getInstance(getApplicationContext()).consumptionLogDao().delete(toDelete);
            logs.remove(position);
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    private void fetchRisksAsync() {
        AsyncTask.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            for (ConsumptionLog log : logs) {
                ScanResult sr = db.scanResultDao().getLatestBefore(log.profileId, log.timestampMs);
                if (sr != null) {
                    riskCache.put(log.id, sr.riskLevel);
                }
            }
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    private int colorForRisk(String risk) {
        if (risk == null) return Color.DKGRAY;
        String r = risk.toLowerCase();
        switch (r) {
            case "safe":
                return Color.parseColor("#4EB98C"); // green
            case "caution":
                return Color.parseColor("#F39C12"); // orange
            case "prohibited":
                return Color.parseColor("#E74C3C"); // red
            default:
                return Color.DKGRAY;
        }
    }

    private String displayRiskText(String risk) {
        if (risk == null) return "--";
        String r = risk.toLowerCase();
        if ("prohibited".equals(r)) return "High Risk";
        if ("caution".equals(r)) return "Caution";
        if ("safe".equals(r)) return "Safe";
        return risk;
    }
}


