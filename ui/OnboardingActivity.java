package com.example.smartfoods.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoods.R;
import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.ProfileManager;
import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OnboardingActivity extends AppCompatActivity {

    private EditText displayNameInput;
    private Spinner sportSpinner;
    private RadioGroup competitionStatusGroup;
    private RadioButton inCompetitionRadio;
    private RadioButton outOfCompetitionRadio;
    private LinearLayout competitionDateLayout;
    private EditText competitionDateInput;
    private Button continueButton;
    private TextView errorText;
    
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        displayNameInput = findViewById(R.id.displayNameInput);
        sportSpinner = findViewById(R.id.sportSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(adapter);
        competitionStatusGroup = findViewById(R.id.competitionStatusGroup);
        inCompetitionRadio = findViewById(R.id.inCompetitionRadio);
        outOfCompetitionRadio = findViewById(R.id.outOfCompetitionRadio);
        competitionDateLayout = findViewById(R.id.competitionDateLayout);
        competitionDateInput = findViewById(R.id.competitionDateInput);
        continueButton = findViewById(R.id.continueButton);
        errorText = findViewById(R.id.errorText);

        // Set up radio button listener to show/hide competition date field
        competitionStatusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.inCompetitionRadio) {
                    competitionDateLayout.setVisibility(View.VISIBLE);
                } else {
                    competitionDateLayout.setVisibility(View.GONE);
                    competitionDateInput.setText("");
                }
            }
        });

        // Set up date picker for competition date
        competitionDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = displayNameInput.getText().toString().trim();
                final String sport = sportSpinner.getSelectedItem() == null ? "" : sportSpinner.getSelectedItem().toString();
                final boolean isInCompetition = inCompetitionRadio.isChecked();
                final String competitionDate = competitionDateInput.getText().toString().trim();

                // Validation
                if (name.isEmpty() || sport.isEmpty()) {
                    errorText.setText("Please enter your name and sport.");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }

                if (isInCompetition && competitionDate.isEmpty()) {
                    errorText.setText("Please select your next competition date.");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }

                errorText.setVisibility(View.GONE);

                // Create profile
                final String status = isInCompetition ? "in-competition" : "out-of-competition";
                final String nextCompetitionDate = isInCompetition ? competitionDate : null;

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Profile profile = new Profile(name, sport, status, nextCompetitionDate);
                        long newId = AppDatabase.getInstance(getApplicationContext()).profileDao().insert(profile);
                        ProfileManager.setActiveProfileId(getApplicationContext(), newId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent back = new Intent(OnboardingActivity.this, MainActivity.class);
                                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(back);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    competitionDateInput.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}


