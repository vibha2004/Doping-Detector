package com.example.smartfoods.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smartfoods.data.entities.ConsumptionLog;

import java.util.List;

@Dao
public interface ConsumptionLogDao {
    @Insert
    long insert(ConsumptionLog log);

    @Update
    int update(ConsumptionLog log);

    @Delete
    int delete(ConsumptionLog log);

    @Query("SELECT * FROM consumption_logs WHERE profileId = :profileId ORDER BY timestampMs DESC")
    List<ConsumptionLog> getByProfile(long profileId);

    @Query("SELECT * FROM consumption_logs ORDER BY timestampMs DESC LIMIT :limit")
    List<ConsumptionLog> getRecent(int limit);

    @Query("SELECT COUNT(*) FROM consumption_logs WHERE profileId = :profileId AND productName = :productName AND timestampMs >= :sinceMs")
    int countByProductSince(long profileId, String productName, long sinceMs);
}


