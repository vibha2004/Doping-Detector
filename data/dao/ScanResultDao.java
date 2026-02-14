package com.example.smartfoods.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smartfoods.data.entities.ScanResult;

import java.util.List;

@Dao
public interface ScanResultDao {
    @Insert
    long insert(ScanResult scanResult);

    @Update
    int update(ScanResult scanResult);

    @Delete
    int delete(ScanResult scanResult);

    @Query("SELECT * FROM scan_results ORDER BY timestampMs DESC LIMIT :limit")
    List<ScanResult> getRecent(int limit);

    @Query("SELECT * FROM scan_results WHERE profileId = :profileId ORDER BY timestampMs DESC")
    List<ScanResult> getByProfile(long profileId);

    @Query("SELECT * FROM scan_results WHERE profileId = :profileId AND timestampMs <= :timestampMs ORDER BY timestampMs DESC LIMIT 1")
    ScanResult getLatestBefore(long profileId, long timestampMs);
}


