package com.example.smartfoods.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "substances", indices = {@Index(value = {"name"}, unique = true)})
public class Substance {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name; // canonical name

    @NonNull
    public String category; // e.g., S0-S11, M1-M3, P1-P3

    @NonNull
    public String aliasesCsv; // comma-separated aliases

    @NonNull
    public String description; // brief info

    public Substance(@NonNull String name,
                     @NonNull String category,
                     @NonNull String aliasesCsv,
                     @NonNull String description) {
        this.name = name;
        this.category = category;
        this.aliasesCsv = aliasesCsv;
        this.description = description;
    }
}


