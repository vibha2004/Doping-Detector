package com.example.smartfoods.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "consumption_logs", indices = {@Index("timestampMs")})
public class ConsumptionLog {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long profileId;

    public long timestampMs;

    @NonNull
    public String productName;

    @NonNull
    public String substanceName; // canonical substance or alias

    public double dosageAmount; // numeric amount

    @NonNull
    public String dosageUnit; // e.g., mg, ml, tabs

    public ConsumptionLog(long profileId,
                          long timestampMs,
                          @NonNull String productName,
                          @NonNull String substanceName,
                          double dosageAmount,
                          @NonNull String dosageUnit) {
        this.profileId = profileId;
        this.timestampMs = timestampMs;
        this.productName = productName;
        this.substanceName = substanceName;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
    }
}


