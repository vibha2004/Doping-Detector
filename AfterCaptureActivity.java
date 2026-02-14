package com.example.smartfoods;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smartfoods.ocr.OcrCaptureActivity;
import com.example.smartfoods.TextParser;
import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.RiskAssessor;
import com.example.smartfoods.data.WadaSearch;
import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.data.entities.ScanResult;
import com.example.smartfoods.data.entities.Substance;
import com.example.smartfoods.data.entities.ConsumptionLog;

import java.util.ArrayList;
import java.util.Locale;

public class AfterCaptureActivity extends AppCompatActivity {

    ArrayList<String> itemList;
    Button anotherPicture;
    Button textToSpeechButton;
    ImageView icon;
    TextView titleText;
    TextParser parser = new TextParser();
    LinearLayout badIngredientsBox;
    TextParser.WadaDetectionResult lastDetection;
    Profile lastProfile;
    String lastRisk;

    private void addSectionHeader(String title, int color) {
        TextView sectionHeader = new TextView(this);
        sectionHeader.setText(title);
        sectionHeader.setTextColor(color);
        sectionHeader.setTextSize(20);
        sectionHeader.setTypeface(null, Typeface.BOLD);
        sectionHeader.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        sectionHeader.setGravity(Gravity.START);
        sectionHeader.setPadding(32, 32, 32, 16);
        badIngredientsBox.addView(sectionHeader);

        // Add a decorative line under the header
        View line = new View(this);
        line.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2));
        line.setBackgroundColor(color);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) line.getLayoutParams();
        params.setMargins(32, 0, 32, 16);
        line.setLayoutParams(params);
        badIngredientsBox.addView(line);
    }

    private void addBulletItem(String text, int color) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setPadding(32, 12, 32, 12);

        TextView bullet = new TextView(this);
        bullet.setText("• ");
        bullet.setTextColor(color);
        bullet.setTextSize(16);
        bullet.setTypeface(null, Typeface.BOLD);
        row.addView(bullet);

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(color);
        tv.setTextSize(16);
        tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(tv);

        badIngredientsBox.addView(row);

        // Add a subtle separator
        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1));
        separator.setBackgroundColor(Color.argb(30,
                Color.red(color), Color.green(color), Color.blue(color)));
        LinearLayout.LayoutParams sepParams = (LinearLayout.LayoutParams) separator.getLayoutParams();
        sepParams.setMargins(32, 0, 32, 0);
        separator.setLayoutParams(sepParams);
        badIngredientsBox.addView(separator);
    }

    private void displayNegativeNested(ArrayList<ArrayList<String>> result) {
        // Add section header
        TextView sectionHeader = new TextView(this);
        sectionHeader.setText("Allergen Warnings");
        sectionHeader.setTextColor(Color.rgb(142, 68, 173)); // Purple color
        sectionHeader.setTextSize(20);
        sectionHeader.setTypeface(null, Typeface.BOLD);
        sectionHeader.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        sectionHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        sectionHeader.setPadding(0, 0, 0, 24);
        badIngredientsBox.addView(sectionHeader);

        // Add decorative line
        View line = new View(this);
        line.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2));
        line.setBackgroundColor(Color.rgb(142, 68, 173));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) line.getLayoutParams();
        params.setMargins(32, 0, 32, 24);
        line.setLayoutParams(params);
        badIngredientsBox.addView(line);

        // Rest of your existing code with improved item styling
        for (int i = 0; i < result.size() - 1; i++) {
            for (int j = 0; j < result.get(i).size(); j++) {
                String[] parts = result.get(i).get(j).split("\\|", 2);
                String ingredient = parts[0];
                String explanation = parts.length > 1 ? parts[1] : "";

                // Create a card for each allergen item
                CardView card = new CardView(this);
                card.setCardBackgroundColor(Color.argb(30, 142, 68, 173)); // Light purple background
                card.setCardElevation(4);
                card.setRadius(8);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                cardParams.setMargins(16, 8, 16, 8);
                card.setLayoutParams(cardParams);

                // Create a horizontal layout for each allergen item
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                itemLayout.setPadding(16, 16, 16, 16);

                // Add warning icon
                ImageView icon = new ImageView(this);
                icon.setImageResource(R.drawable.ic_warning);
                icon.setLayoutParams(new LinearLayout.LayoutParams(
                        36, 36));
                icon.setPadding(0, 0, 16, 0);
                icon.setColorFilter(Color.rgb(142, 68, 173));
                itemLayout.addView(icon);

                // Add ingredient text
                TextView text = new TextView(this);
                text.setText(ingredient);
                text.setTextColor(Color.rgb(142, 68, 173));
                text.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                text.setGravity(Gravity.START);
                text.setTextSize(16);
                text.setTypeface(null, Typeface.BOLD);
                text.setPaintFlags(text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                // Add click listener
                text.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Allergen Warning");
                    builder.setMessage(explanation);
                    builder.setPositiveButton("OK", null);

                    // Style the dialog with purple theme
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Customize button color
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if (positiveButton != null) {
                        positiveButton.setTextColor(Color.rgb(142, 68, 173));
                    }
                });

                itemLayout.addView(text);
                card.addView(itemLayout);
                badIngredientsBox.addView(card);
            }
        }
    }

    Drawable check;
    Drawable negative;
    String preferences;
    TextToSpeech ts;
    StringBuilder speechText = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_capture);

        anotherPicture = (Button) findViewById(R.id.AnotherPicture);
        // No cosmetic preference flow anymore

        itemList = (ArrayList<String>) getIntent().getSerializableExtra("ING-LIST");
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        icon = (ImageView) findViewById(R.id.icon);
        titleText = (TextView) findViewById(R.id.TitleText);
        badIngredientsBox = (LinearLayout) findViewById(R.id.BadIngredientsBox);
        textToSpeechButton = (Button) findViewById(R.id.TextToSpeech);

        try {
            check = ContextCompat.getDrawable(this, R.drawable.check);
            negative = ContextCompat.getDrawable(this, R.drawable.negative);
        } catch (Exception e) {
            Log.e("AfterCapture", "Drawable load failed: " + e.getMessage());
        }

        ts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    ts.setLanguage(Locale.US);
                }
            }
        });

        ts.setSpeechRate(0.9f);

        for (int i = 0; i < itemList.size(); i++) {
            Log.i("ITEM " + i, itemList.get(i));
        }
        // Initialize WADA-only UI state
        titleText.setText("Analyzing product for WADA-prohibited substances...");
        titleText.setTextColor(Color.rgb(66, 66, 66));
        badIngredientsBox.removeAllViews();

        if (itemList.isEmpty()) {
            // If nothing was detected, inform the user and allow taking another picture
            titleText.setText("No text detected. Try a clearer photo or better lighting.");
            return;
        }

        // No filter toggle: sections are always shown

        // Store scan with basic WADA risk evaluation
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Profile profile = AppDatabase.getInstance(getApplicationContext()).profileDao().getPrimaryProfile();
                    int days = RiskAssessor.daysToNextCompetition(profile);
                    WadaSearch search = new WadaSearch(getApplicationContext());
                    ArrayList<Substance> matched = new ArrayList<Substance>();
                    for (String line : itemList) {
                        matched.addAll(search.searchFuzzy(line, 5));
                    }
                    String risk = RiskAssessor.assessRiskForSubstances(matched, days);
                    String detectedJson = RiskAssessor.toJsonArrayOfNames(matched);
                    ScanResult sr = new ScanResult(profile == null ? 0 : profile.id,
                            System.currentTimeMillis(),
                            joinList(itemList, "\n"),
                            detectedJson,
                            risk);
                    AppDatabase.getInstance(getApplicationContext()).scanResultDao().insert(sr);

                    // Compute WADA detection off the UI thread to avoid Room main-thread access
                    TextParser.WadaDetectionResult detection = parser.detectWadaSubstances(
                            getApplicationContext(),
                            itemList,
                            (profile == null) ? null : profile.sport);

                    // Update UI with WADA breakdown
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isFinishing() || isDestroyed()) return;

                            lastDetection = detection;
                            lastProfile = profile;
                            lastRisk = risk;
                            renderWadaSections(detection, profile, risk, true);

                            promptAndStoreProduct(profile, risk);

                        }
                    });
                } catch (final Exception e) {
                    Log.e("Risk", "Failed to assess/store risk: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isFinishing() || isDestroyed()) return;
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AfterCaptureActivity.this);
                            builder.setTitle("Scan Error")
                                    .setMessage("Something went wrong while analyzing the scan.\n" + e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    });
                }
            }
        }).start();

        anotherPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterCaptureActivity.this, OcrCaptureActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textToSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ts != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ts.speak(speechText.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        ts.speak(speechText.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (ts != null) {
                ts.stop();
                ts.shutdown();
            }
        } catch (Exception ignored) {}
    }

    private boolean noBadIngredients(ArrayList<ArrayList<String>> allergenItems,
                                     ArrayList<String> lactoseItems,
                                     ArrayList<String> veganItems,
                                     ArrayList<String> vegetarianItems,
                                     ArrayList<String> glutenItems) {
        // Only check the first list in allergenItems for actual allergens
        boolean hasAllergens = !allergenItems.get(0).isEmpty();
        boolean hasLactose = !lactoseItems.isEmpty();
        boolean hasVegan = !veganItems.isEmpty();
        boolean hasVegetarian = !vegetarianItems.isEmpty();
        boolean hasGluten = !glutenItems.isEmpty();

        Log.i("Allergen Check", "Has allergens: " + hasAllergens);
        Log.i("Allergen Check", "Has lactose: " + hasLactose);
        Log.i("Allergen Check", "Has vegan: " + hasVegan);
        Log.i("Allergen Check", "Has vegetarian: " + hasVegetarian);
        Log.i("Allergen Check", "Has gluten: " + hasGluten);

        return !(hasAllergens || hasLactose || hasVegan || hasVegetarian || hasGluten);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AfterCaptureActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void displayNegative(ArrayList<String> result) {
        Log.i("Cheese", "in" + result.size());
        for (int i = 0; i < result.size() - 1; i++) {
            Log.i("Cheese", result.get(i));
        }

        for (int i = 0; i < result.size() - 1; i++) {
            Log.i("OK", result.get(i));

            // Create a card for each negative item
            CardView card = new CardView(this);
            card.setCardBackgroundColor(Color.argb(30, 142, 68, 173)); // Light purple background
            card.setCardElevation(4);
            card.setRadius(8);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(16, 8, 16, 8);
            card.setLayoutParams(cardParams);

            TextView text = new TextView(this);
            text.setText(result.get(i));
            text.setTextColor(Color.rgb(142, 68, 173));
            text.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setGravity(Gravity.CENTER);
            text.setPadding(16, 16, 16, 16);
            text.setTextSize(16);
            text.setTypeface(null, Typeface.BOLD);

            card.addView(text);
            badIngredientsBox.addView(card);
        }
        speechText.append(result.get(result.size() - 1));
        speechText.append(" ");
    }

    private String joinList(ArrayList<String> list, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(sep);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private void renderWadaSections(TextParser.WadaDetectionResult detection, Profile profile, String risk, boolean showAlert) {
        if (detection == null) return;
        badIngredientsBox.removeAllViews();
        // Rebuild speech text each render
        speechText.setLength(0);

        if (detection.allTimes.isEmpty() && detection.inCompetition.isEmpty() && detection.sportSpecific.isEmpty()) {
            titleText.setText("No WADA-prohibited substances detected");
            titleText.setTextColor(Color.rgb(78, 185, 140));

            // Add a success card
            CardView successCard = new CardView(this);
            successCard.setCardBackgroundColor(Color.argb(30, 78, 185, 140));
            successCard.setCardElevation(4);
            successCard.setRadius(12);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(16, 16, 16, 16);
            successCard.setLayoutParams(cardParams);

            LinearLayout successLayout = new LinearLayout(this);
            successLayout.setOrientation(LinearLayout.VERTICAL);
            successLayout.setPadding(24, 24, 24, 24);

            ImageView successIcon = new ImageView(this);
            successIcon.setImageResource(R.drawable.check);
            successIcon.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            successIcon.setColorFilter(Color.rgb(78, 185, 140));
            successLayout.addView(successIcon);

            TextView successText = new TextView(this);
            successText.setText("This product appears to be safe for athletic use based on WADA guidelines");
            successText.setTextColor(Color.rgb(78, 185, 140));
            successText.setTextSize(16);
            successText.setGravity(Gravity.CENTER);
            successText.setTypeface(null, Typeface.BOLD);
            successText.setPadding(0, 16, 0, 0);
            successLayout.addView(successText);

            successCard.addView(successLayout);
            badIngredientsBox.addView(successCard);

            return;
        }

        // Add summary card at the top
        CardView summaryCard = new CardView(this);
        summaryCard.setCardBackgroundColor(Color.argb(30, 142, 68, 173));
        summaryCard.setCardElevation(6);
        summaryCard.setRadius(12);
        LinearLayout.LayoutParams summaryParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        summaryParams.setMargins(16, 16, 16, 24);
        summaryCard.setLayoutParams(summaryParams);

        LinearLayout summaryLayout = new LinearLayout(this);
        summaryLayout.setOrientation(LinearLayout.VERTICAL);
        summaryLayout.setPadding(24, 24, 24, 24);

        TextView summaryTitle = new TextView(this);
        summaryTitle.setText("WADA Compliance Summary");
        summaryTitle.setTextColor(Color.rgb(142, 68, 173));
        summaryTitle.setTextSize(18);
        summaryTitle.setTypeface(null, Typeface.BOLD);
        summaryTitle.setGravity(Gravity.CENTER);
        summaryLayout.addView(summaryTitle);

        // Add risk assessment
        TextView riskText = new TextView(this);
        riskText.setText("Risk Level: Unsafe");
        riskText.setTextColor(Color.rgb(142, 68, 173));
        riskText.setTextSize(16);
        riskText.setGravity(Gravity.CENTER);
        riskText.setPadding(0, 16, 0, 0);
        riskText.setTypeface(null, Typeface.BOLD);
        summaryLayout.addView(riskText);

        // Add detection counts
        LinearLayout statsLayout = new LinearLayout(this);
        statsLayout.setOrientation(LinearLayout.HORIZONTAL);
        statsLayout.setGravity(Gravity.CENTER);
        statsLayout.setPadding(0, 16, 0, 0);

        addStatView(statsLayout, "Always Prohibited", detection.allTimes.size(), Color.rgb(142, 68, 173));
        addStatView(statsLayout, "In Competition", detection.inCompetition.size(), Color.rgb(142, 68, 173));
        addStatView(statsLayout, "Sport Specific", detection.sportSpecific.size(), Color.rgb(142, 68, 173));

        summaryLayout.addView(statsLayout);
        summaryCard.addView(summaryLayout);
        badIngredientsBox.addView(summaryCard);

        addSectionHeader("WADA Prohibited Substances", Color.rgb(142, 68, 173));

        if (!detection.allTimes.isEmpty()) {
            addSectionHeader("Prohibited at all times (S0–S5)", Color.rgb(142, 68, 173));
            for (String txt : detection.allTimes) addBulletItem(txt, Color.rgb(142, 68, 173));
        }

        if (!detection.inCompetition.isEmpty()) {
            addSectionHeader("In-competition prohibited (S6–S9)", Color.rgb(142, 68, 173));
            for (String txt : detection.inCompetition) addBulletItem(txt, Color.rgb(142, 68, 173));
        }

        if (!detection.sportSpecific.isEmpty()) {
            String sportNote = (profile != null && profile.sport != null && profile.sport.length() > 0)
                    ? (" (your sport: " + profile.sport + ")") : "";
            addSectionHeader("Prohibited in specific sports (P1)" + sportNote, Color.rgb(142, 68, 173));
            for (String txt : detection.sportSpecific) addBulletItem(txt, Color.rgb(142, 68, 173));
        }

        // Build TTS summary
        try {
            speechText.append("WADA prohibited substances detected. ");
            if (!detection.allTimes.isEmpty()) {
                speechText.append("Prohibited at all times: ");
                for (int i = 0; i < detection.allTimes.size(); i++) {
                    speechText.append(detection.allTimes.get(i));
                    if (i < detection.allTimes.size() - 1) speechText.append(", "); else speechText.append(". ");
                }
            }
            if (!detection.inCompetition.isEmpty()) {
                speechText.append("In competition only: ");
                for (int i = 0; i < detection.inCompetition.size(); i++) {
                    speechText.append(detection.inCompetition.get(i));
                    if (i < detection.inCompetition.size() - 1) speechText.append(", "); else speechText.append(". ");
                }
            }
            if (!detection.sportSpecific.isEmpty()) {
                speechText.append("Sport specific bans: ");
                for (int i = 0; i < detection.sportSpecific.size(); i++) {
                    speechText.append(detection.sportSpecific.get(i));
                    if (i < detection.sportSpecific.size() - 1) speechText.append(", "); else speechText.append(". ");
                }
            }
        } catch (Exception ignored) {}

        if (showAlert) {
            int inCompCountForSummary = detection.inCompetition.size();
            int total = detection.allTimes.size() + inCompCountForSummary + detection.sportSpecific.size();
            if (total > 0) {
                StringBuilder msg = new StringBuilder();
                if (!detection.allTimes.isEmpty()) {
                    msg.append("All-times (S0–S5): ").append(detection.allTimes.size()).append("\n");
                }
                if (inCompCountForSummary > 0) {
                    msg.append("In-competition (S6–S9): ").append(inCompCountForSummary).append("\n");
                }
                if (!detection.sportSpecific.isEmpty()) {
                    msg.append("Sport-specific (P1): ").append(detection.sportSpecific.size());
                    if (profile != null && profile.sport != null && profile.sport.length() > 0) msg.append(" for sport ").append(profile.sport);
                }

                // If in-competition substances detected, warn about days to next competition
                if (inCompCountForSummary > 0) {
                    int daysToComp = RiskAssessor.daysToNextCompetition(profile);
                    if (daysToComp != Integer.MAX_VALUE) {
                        msg.append("\n\nYour competition is in ")
                           .append(daysToComp)
                           .append(" days — please take heed.");
                    }
                }

                AlertDialog dialog = new AlertDialog.Builder(AfterCaptureActivity.this)
                        .setTitle("Prohibited substances detected")
                        .setMessage(msg.toString())
                        .setPositiveButton("OK", null)
                        .create();
                dialog.show();

                // Customize button color
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setTextColor(Color.rgb(142, 68, 173));
                }
            }
        }
        // Lightweight debug log
        Log.d("WADA-DEBUG", "S0-5=" + detection.allTimes.size() + ", S6-9=" + detection.inCompetition.size() + ", P1=" + detection.sportSpecific.size());
    }

    private void addStatView(LinearLayout container, String label, int count, int color) {
        LinearLayout statLayout = new LinearLayout(this);
        statLayout.setOrientation(LinearLayout.VERTICAL);
        statLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(8, 0, 8, 0);
        statLayout.setLayoutParams(params);

        TextView countText = new TextView(this);
        countText.setText(String.valueOf(count));
        countText.setTextColor(color);
        countText.setTextSize(20);
        countText.setTypeface(null, Typeface.BOLD);
        countText.setGravity(Gravity.CENTER);
        statLayout.addView(countText);

        TextView labelText = new TextView(this);
        labelText.setText(label);
        labelText.setTextColor(color);
        labelText.setTextSize(12);
        labelText.setGravity(Gravity.CENTER);
        labelText.setPadding(0, 4, 0, 0);
        statLayout.addView(labelText);

        container.addView(statLayout);
    }

    private void promptAndStoreProduct(Profile profile, String risk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter product name");
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("e.g., Cold Relief Tablets");
        builder.setView(input);
        builder.setCancelable(false);
        builder.setPositiveButton("Save", (dialog, which) -> {
            final String productName = input.getText().toString().trim();
            if (productName.isEmpty()) return;
            new Thread(() -> {
                try {
                    long now = System.currentTimeMillis();
                    long profileId = (profile == null) ? 0 : profile.id;
                    // Store a basic consumption log entry without dosage for the scan event
                    ConsumptionLog log = new ConsumptionLog(profileId, now, productName, "", 0.0, "unit");
                    AppDatabase.getInstance(getApplicationContext()).consumptionLogDao().insert(log);

                    // If in-competition and already scanned twice this week, ask weekly consumption count
                    if (profile != null && profile.isInCompetition()) {
                        long oneWeekMs = 7L * 24L * 60L * 60L * 1000L;
                        long since = now - oneWeekMs;
                        int count = AppDatabase.getInstance(getApplicationContext())
                                .consumptionLogDao()
                                .countByProductSince(profileId, productName, since);
                        if (count >= 2) {
                            runOnUiThread(() -> promptWeeklyConsumptionAndEvaluate(risk));
                        }
                    }
                } catch (Exception ex) {
                    Log.e("ProductLog", "Failed to store product: " + ex.getMessage());
                }
            }).start();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button colors
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(Color.rgb(142, 68, 173));
        }
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(Color.rgb(142, 68, 173));
        }
    }

    private void promptWeeklyConsumptionAndEvaluate(String risk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Weekly consumption count");
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("How many times this week?");
        builder.setView(input);
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String val = input.getText().toString().trim();
            int times = 0;
            try { times = Integer.parseInt(val); } catch (NumberFormatException ignored) {}
            final boolean permissible = evaluatePermissible(risk, times);
            AlertDialog.Builder res = new AlertDialog.Builder(this);
            res.setTitle("Consumption assessment");
            res.setMessage(permissible ? "Within permissible limit." : "Not within permissible limit.");
            res.setPositiveButton("OK", null);

            AlertDialog resultDialog = res.create();
            resultDialog.show();

            // Customize button color
            Button okButton = resultDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (okButton != null) {
                okButton.setTextColor(Color.rgb(142, 68, 173));
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button colors
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(Color.rgb(142, 68, 173));
        }
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(Color.rgb(142, 68, 173));
        }
    }

    private boolean evaluatePermissible(String risk, int weeklyCount) {
        if ("prohibited".equalsIgnoreCase(risk)) return false;
        if ("caution".equalsIgnoreCase(risk)) return weeklyCount <= 2;
        return true;
    }
}