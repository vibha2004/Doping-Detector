package com.example.smartfoods.ui.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartfoods.R;
import com.example.smartfoods.data.entities.Profile;
import android.content.Intent;
import com.example.smartfoods.ui.DashboardActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.example.smartfoods.data.ProfileManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private ProfileViewModel viewModel;
    private EditText nameEditText;
    private Spinner sportSpinner;
    private RadioGroup competitionStatusGroup;
    private RadioButton inCompetitionRadio;
    private EditText competitionDateEdit;
    private TextInputLayout competitionDateLayout;
    private Button saveButton;
    private Calendar competitionCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        sportSpinner = findViewById(R.id.sportSpinner);
        competitionStatusGroup = findViewById(R.id.competitionStatusGroup);
        inCompetitionRadio = findViewById(R.id.inCompetitionRadio);
        competitionDateEdit = findViewById(R.id.competitionDateEdit);
        competitionDateLayout = findViewById(R.id.competitionDateLayout);
        saveButton = findViewById(R.id.saveButton);

        // Set up sport spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(adapter);

        // Set up competition date picker
        competitionCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                competitionCalendar.set(Calendar.YEAR, year);
                competitionCalendar.set(Calendar.MONTH, monthOfYear);
                competitionCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateCompetitionDate();
            }
        };

        competitionDateEdit.setOnClickListener(v -> {
            new DatePickerDialog(ProfileActivity.this, dateSetListener,
                    competitionCalendar.get(Calendar.YEAR),
                    competitionCalendar.get(Calendar.MONTH),
                    competitionCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Set up competition status radio group listener
        competitionStatusGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.inCompetitionRadio) {
                competitionDateLayout.setVisibility(View.VISIBLE);
                // Prompt user to pick a date immediately when switching to in-competition
                new DatePickerDialog(ProfileActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                    competitionCalendar.set(Calendar.YEAR, year);
                    competitionCalendar.set(Calendar.MONTH, monthOfYear);
                    competitionCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateCompetitionDate();
                },
                        competitionCalendar.get(Calendar.YEAR),
                        competitionCalendar.get(Calendar.MONTH),
                        competitionCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } else {
                competitionDateLayout.setVisibility(View.GONE);
            }
        });

        // Load existing profile
        viewModel.getCurrentProfile().observe(this, profile -> {
            if (profile != null) {
                nameEditText.setText(profile.displayName);
                
                // Set selected sport
                for (int i = 0; i < sportSpinner.getCount(); i++) {
                    if (sportSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(profile.sport)) {
                        sportSpinner.setSelection(i);
                        break;
                    }
                }
                
                // Set competition status
                if (profile.competitionStatus.equals("in-competition")) {
                    competitionStatusGroup.check(R.id.inCompetitionRadio);
                    competitionDateLayout.setVisibility(View.VISIBLE);
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        competitionCalendar.setTime(sdf.parse(profile.nextCompetitionDate));
                        updateCompetitionDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    competitionStatusGroup.check(R.id.outOfCompetitionRadio);
                    competitionDateLayout.setVisibility(View.GONE);
                }
            }
        });

        // Set up save button
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void updateCompetitionDate() {
        String dateFormat = "MMM d, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        competitionDateEdit.setText(sdf.format(competitionCalendar.getTime()));
    }

    private void saveProfile() {
        String name = nameEditText.getText().toString().trim();
        String sport = sportSpinner.getSelectedItem().toString();
        boolean isInCompetition = inCompetitionRadio.isChecked();
        String competitionStatus = isInCompetition ? "in-competition" : "out-of-competition";
        
        String competitionDate = "";
        if (isInCompetition) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            competitionDate = sdf.format(competitionCalendar.getTime());
        }
        
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return;
        }
        
        viewModel.saveProfile(name, sport, competitionStatus, competitionDate);
        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
        // Navigate back to dashboard and indicate a new profile was created
        Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
        intent.putExtra("new_profile", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
