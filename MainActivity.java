package com.example.smartfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.smartfoods.ocr.OcrCaptureActivity;
import com.example.smartfoods.ui.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";
    private Button profileButton;
    private Button chatbotButton;
    private String preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileButton = findViewById(R.id.profile_button);
        chatbotButton = findViewById(R.id.chatbot_button);

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        chatbotButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wada-chatbot.vercel.app/"));
            startActivity(browserIntent);
        });

        preferences = getPreferencesFromActivity();

        findViewById(R.id.read_text).setOnClickListener(this);
    }

    private String getPreferencesFromActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("preferences") != null) {
                return extras.getString("preferences");
            }
        }
        return "0000000000";
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, true);
            intent.putExtra(OcrCaptureActivity.UseFlash, false);
            intent.putExtra("preferences", preferences);
            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
    }
}