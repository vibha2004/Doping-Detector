package com.example.smartfoods.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.smartfoods.data.dao.ConsumptionLogDao;
import com.example.smartfoods.data.dao.SportBanDao;
import com.example.smartfoods.data.dao.ProfileDao;
import com.example.smartfoods.data.dao.ScanResultDao;
import com.example.smartfoods.data.dao.SubstanceDao;
import com.example.smartfoods.data.entities.ConsumptionLog;
import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.data.entities.ScanResult;
import com.example.smartfoods.data.entities.Substance;
import com.example.smartfoods.data.entities.SportBan;

@Database(
        entities = {
                Profile.class,
                ScanResult.class,
                ConsumptionLog.class,
                Substance.class,
                SportBan.class
        },
        version = 5,
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProfileDao profileDao();
    public abstract ScanResultDao scanResultDao();
    public abstract ConsumptionLogDao consumptionLogDao();
    public abstract SubstanceDao substanceDao();
    public abstract SportBanDao sportBanDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "dopant_scanner.db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


