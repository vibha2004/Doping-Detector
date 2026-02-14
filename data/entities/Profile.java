package com.example.smartfoods.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a user profile in the database.
 * Stores user's name, sport, competition status, and competition date.
 */
@Entity(tableName = "profiles")
public class Profile {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String displayName;

    @NonNull
    public String sport;

    // "in-competition" or "out-of-competition"
    @NonNull
    public String competitionStatus;

    // ISO-8601 date string for next competition (only if in-competition)
    public String nextCompetitionDate;

    /**
     * Constructor for creating a new Profile.
     * @param displayName The user's display name
     * @param sport The user's sport
     * @param competitionStatus Either "in-competition" or "out-of-competition"
     * @param nextCompetitionDate The date of the next competition (can be null if out of competition)
     */
    public Profile(@NonNull String displayName,
                  @NonNull String sport,
                  @NonNull String competitionStatus,
                  String nextCompetitionDate) {
        this.displayName = displayName;
        this.sport = sport;
        this.competitionStatus = competitionStatus;
        this.nextCompetitionDate = nextCompetitionDate;
    }

    /**
     * Check if the user is currently in competition.
     * @return true if in competition, false otherwise
     */
    public boolean isInCompetition() {
        return "in-competition".equals(competitionStatus);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", sport='" + sport + '\'' +
                ", competitionStatus='" + competitionStatus + '\'' +
                ", nextCompetitionDate='" + nextCompetitionDate + '\'' +
                '}';
    }


}
