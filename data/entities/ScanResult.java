package com.example.smartfoods.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_results", indices = {@Index("timestampMs")})
public class ScanResult {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long profileId; // 0 if not associated

    public long timestampMs;

    @NonNull
    public String rawText; // OCR text block

    @NonNull
    public String detectedSubstancesJson; // JSON array of detected substance ids/names

    @NonNull
    public String riskLevel; // "safe", "caution", "prohibited"

    public ScanResult(long profileId,
                      long timestampMs,
                      @NonNull String rawText,
                      @NonNull String detectedSubstancesJson,
                      @NonNull String riskLevel) {
        this.profileId = profileId;
        this.timestampMs = timestampMs;
        this.rawText = rawText;
        this.detectedSubstancesJson = detectedSubstancesJson;
        this.riskLevel = riskLevel;
    }
}


