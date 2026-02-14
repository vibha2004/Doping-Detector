package com.example.smartfoods.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoods.R;
import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.ProfileManager;
import com.example.smartfoods.data.entities.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileChooserActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Profile> profiles = new ArrayList<>();
    private ArrayList<Long> profileIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_chooser);
        listView = findViewById(R.id.profileList);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, new ArrayList<String>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Profile p = profiles.get(position);
                ProfileManager.setActiveProfileId(getApplicationContext(), p.id);
                Toast.makeText(ProfileChooserActivity.this, "Active profile: " + p.displayName, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                profiles = AppDatabase.getInstance(getApplicationContext()).profileDao().getAllImmediate();
                final ArrayList<String> names = new ArrayList<>();
                profileIds.clear();
                for (Profile p : profiles) {
                    names.add(p.displayName + " (" + p.sport + ")");
                    profileIds.add(p.id);
                }
                final long activeId = ProfileManager.getActiveProfileId(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        adapter.addAll(names);
                        adapter.notifyDataSetChanged();
                        // Preselect the active profile if present
                        if (!profileIds.isEmpty()) {
                            int checked = -1;
                            for (int i = 0; i < profileIds.size(); i++) {
                                if (profileIds.get(i) == activeId) { checked = i; break; }
                            }
                            if (checked >= 0) listView.setItemChecked(checked, true);
                        }
                    }
                });
            }
        });
    }
}


