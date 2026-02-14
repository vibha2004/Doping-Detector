package com.example.smartfoods.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smartfoods.data.entities.Profile;

import java.util.List;

@Dao
public interface ProfileDao {
    @Insert
    long insert(Profile profile);

    @Update
    int update(Profile profile);

    @Delete
    int delete(Profile profile);

    @Query("SELECT * FROM profiles ORDER BY id LIMIT 1")
    Profile getPrimaryProfile();

    @Query("SELECT * FROM profiles")
    LiveData<List<Profile>> getAll();

    // Synchronous variant for non-LiveData usage
    @Query("SELECT * FROM profiles")
    List<Profile> getAllImmediate();

    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    Profile getById(long id);
}


