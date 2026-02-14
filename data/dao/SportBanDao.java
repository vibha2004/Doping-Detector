package com.example.smartfoods.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.smartfoods.data.entities.SportBan;

import java.util.List;

@Dao
public interface SportBanDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(SportBan ban);

    @Query("SELECT * FROM sport_bans WHERE category = :category")
    List<SportBan> getByCategory(String category);

    @Query("SELECT COUNT(*) FROM sport_bans")
    int count();
}


