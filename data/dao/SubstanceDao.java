package com.example.smartfoods.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.smartfoods.data.entities.Substance;

import java.util.List;

@Dao
public interface SubstanceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Substance substance);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(List<Substance> substances);

    @Query("SELECT * FROM substances WHERE name LIKE '%' || :query || '%' OR aliasesCsv LIKE '%' || :query || '%' ORDER BY name LIMIT :limit")
    List<Substance> searchByNameOrAlias(String query, int limit);

    @Query("SELECT * FROM substances WHERE category = :category ORDER BY name")
    List<Substance> getByCategory(String category);

    @Query("SELECT COUNT(*) FROM substances")
    int count();

    @Query("DELETE FROM substances")
    void deleteAll();
}


