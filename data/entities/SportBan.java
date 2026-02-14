package com.example.smartfoods.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sport_bans", indices = {@Index(value = {"category", "sportLower"}, unique = true)})
public class SportBan {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String category; // e.g., P2

    @NonNull
    public String sportLower; // normalized lowercase sport name

    public SportBan(@NonNull String category, @NonNull String sportLower) {
        this.category = category;
        this.sportLower = sportLower;
    }
}


