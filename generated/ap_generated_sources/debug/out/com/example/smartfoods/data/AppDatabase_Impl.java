package com.example.smartfoods.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.smartfoods.data.dao.ConsumptionLogDao;
import com.example.smartfoods.data.dao.ConsumptionLogDao_Impl;
import com.example.smartfoods.data.dao.ProfileDao;
import com.example.smartfoods.data.dao.ProfileDao_Impl;
import com.example.smartfoods.data.dao.ScanResultDao;
import com.example.smartfoods.data.dao.ScanResultDao_Impl;
import com.example.smartfoods.data.dao.SportBanDao;
import com.example.smartfoods.data.dao.SportBanDao_Impl;
import com.example.smartfoods.data.dao.SubstanceDao;
import com.example.smartfoods.data.dao.SubstanceDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ProfileDao _profileDao;

  private volatile ScanResultDao _scanResultDao;

  private volatile ConsumptionLogDao _consumptionLogDao;

  private volatile SubstanceDao _substanceDao;

  private volatile SportBanDao _sportBanDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `profiles` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `displayName` TEXT NOT NULL, `sport` TEXT NOT NULL, `competitionStatus` TEXT NOT NULL, `nextCompetitionDate` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `scan_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `profileId` INTEGER NOT NULL, `timestampMs` INTEGER NOT NULL, `rawText` TEXT NOT NULL, `detectedSubstancesJson` TEXT NOT NULL, `riskLevel` TEXT NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_scan_results_timestampMs` ON `scan_results` (`timestampMs`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `consumption_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `profileId` INTEGER NOT NULL, `timestampMs` INTEGER NOT NULL, `productName` TEXT NOT NULL, `substanceName` TEXT NOT NULL, `dosageAmount` REAL NOT NULL, `dosageUnit` TEXT NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_consumption_logs_timestampMs` ON `consumption_logs` (`timestampMs`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `substances` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `category` TEXT NOT NULL, `aliasesCsv` TEXT NOT NULL, `description` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_substances_name` ON `substances` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sport_bans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category` TEXT NOT NULL, `sportLower` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_sport_bans_category_sportLower` ON `sport_bans` (`category`, `sportLower`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '66d932f156d242e66c66b8c85af647b1')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `profiles`");
        db.execSQL("DROP TABLE IF EXISTS `scan_results`");
        db.execSQL("DROP TABLE IF EXISTS `consumption_logs`");
        db.execSQL("DROP TABLE IF EXISTS `substances`");
        db.execSQL("DROP TABLE IF EXISTS `sport_bans`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsProfiles = new HashMap<String, TableInfo.Column>(5);
        _columnsProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("sport", new TableInfo.Column("sport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("competitionStatus", new TableInfo.Column("competitionStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("nextCompetitionDate", new TableInfo.Column("nextCompetitionDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProfiles = new TableInfo("profiles", _columnsProfiles, _foreignKeysProfiles, _indicesProfiles);
        final TableInfo _existingProfiles = TableInfo.read(db, "profiles");
        if (!_infoProfiles.equals(_existingProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "profiles(com.example.smartfoods.data.entities.Profile).\n"
                  + " Expected:\n" + _infoProfiles + "\n"
                  + " Found:\n" + _existingProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsScanResults = new HashMap<String, TableInfo.Column>(6);
        _columnsScanResults.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("profileId", new TableInfo.Column("profileId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("timestampMs", new TableInfo.Column("timestampMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("rawText", new TableInfo.Column("rawText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("detectedSubstancesJson", new TableInfo.Column("detectedSubstancesJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("riskLevel", new TableInfo.Column("riskLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScanResults = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScanResults = new HashSet<TableInfo.Index>(1);
        _indicesScanResults.add(new TableInfo.Index("index_scan_results_timestampMs", false, Arrays.asList("timestampMs"), Arrays.asList("ASC")));
        final TableInfo _infoScanResults = new TableInfo("scan_results", _columnsScanResults, _foreignKeysScanResults, _indicesScanResults);
        final TableInfo _existingScanResults = TableInfo.read(db, "scan_results");
        if (!_infoScanResults.equals(_existingScanResults)) {
          return new RoomOpenHelper.ValidationResult(false, "scan_results(com.example.smartfoods.data.entities.ScanResult).\n"
                  + " Expected:\n" + _infoScanResults + "\n"
                  + " Found:\n" + _existingScanResults);
        }
        final HashMap<String, TableInfo.Column> _columnsConsumptionLogs = new HashMap<String, TableInfo.Column>(7);
        _columnsConsumptionLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConsumptionLogs.put("profileId", new TableInfo.Column("profileId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConsumptionLogs.put("timestampMs", new TableInfo.Column("timestampMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConsumptionLogs.put("productName", new TableInfo.Column("productName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConsumptionLogs.put("substanceName", new TableInfo.Column("substanceName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConsumptionLogs.put("dosageAmount", new TableInfo.Column("dosageAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConsumptionLogs.put("dosageUnit", new TableInfo.Column("dosageUnit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysConsumptionLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesConsumptionLogs = new HashSet<TableInfo.Index>(1);
        _indicesConsumptionLogs.add(new TableInfo.Index("index_consumption_logs_timestampMs", false, Arrays.asList("timestampMs"), Arrays.asList("ASC")));
        final TableInfo _infoConsumptionLogs = new TableInfo("consumption_logs", _columnsConsumptionLogs, _foreignKeysConsumptionLogs, _indicesConsumptionLogs);
        final TableInfo _existingConsumptionLogs = TableInfo.read(db, "consumption_logs");
        if (!_infoConsumptionLogs.equals(_existingConsumptionLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "consumption_logs(com.example.smartfoods.data.entities.ConsumptionLog).\n"
                  + " Expected:\n" + _infoConsumptionLogs + "\n"
                  + " Found:\n" + _existingConsumptionLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsSubstances = new HashMap<String, TableInfo.Column>(5);
        _columnsSubstances.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubstances.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubstances.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubstances.put("aliasesCsv", new TableInfo.Column("aliasesCsv", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubstances.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubstances = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSubstances = new HashSet<TableInfo.Index>(1);
        _indicesSubstances.add(new TableInfo.Index("index_substances_name", true, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoSubstances = new TableInfo("substances", _columnsSubstances, _foreignKeysSubstances, _indicesSubstances);
        final TableInfo _existingSubstances = TableInfo.read(db, "substances");
        if (!_infoSubstances.equals(_existingSubstances)) {
          return new RoomOpenHelper.ValidationResult(false, "substances(com.example.smartfoods.data.entities.Substance).\n"
                  + " Expected:\n" + _infoSubstances + "\n"
                  + " Found:\n" + _existingSubstances);
        }
        final HashMap<String, TableInfo.Column> _columnsSportBans = new HashMap<String, TableInfo.Column>(3);
        _columnsSportBans.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSportBans.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSportBans.put("sportLower", new TableInfo.Column("sportLower", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSportBans = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSportBans = new HashSet<TableInfo.Index>(1);
        _indicesSportBans.add(new TableInfo.Index("index_sport_bans_category_sportLower", true, Arrays.asList("category", "sportLower"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoSportBans = new TableInfo("sport_bans", _columnsSportBans, _foreignKeysSportBans, _indicesSportBans);
        final TableInfo _existingSportBans = TableInfo.read(db, "sport_bans");
        if (!_infoSportBans.equals(_existingSportBans)) {
          return new RoomOpenHelper.ValidationResult(false, "sport_bans(com.example.smartfoods.data.entities.SportBan).\n"
                  + " Expected:\n" + _infoSportBans + "\n"
                  + " Found:\n" + _existingSportBans);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "66d932f156d242e66c66b8c85af647b1", "3b5d76c49189db448307dc6eeeeacfcc");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "profiles","scan_results","consumption_logs","substances","sport_bans");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `profiles`");
      _db.execSQL("DELETE FROM `scan_results`");
      _db.execSQL("DELETE FROM `consumption_logs`");
      _db.execSQL("DELETE FROM `substances`");
      _db.execSQL("DELETE FROM `sport_bans`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProfileDao.class, ProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScanResultDao.class, ScanResultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ConsumptionLogDao.class, ConsumptionLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SubstanceDao.class, SubstanceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SportBanDao.class, SportBanDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProfileDao profileDao() {
    if (_profileDao != null) {
      return _profileDao;
    } else {
      synchronized(this) {
        if(_profileDao == null) {
          _profileDao = new ProfileDao_Impl(this);
        }
        return _profileDao;
      }
    }
  }

  @Override
  public ScanResultDao scanResultDao() {
    if (_scanResultDao != null) {
      return _scanResultDao;
    } else {
      synchronized(this) {
        if(_scanResultDao == null) {
          _scanResultDao = new ScanResultDao_Impl(this);
        }
        return _scanResultDao;
      }
    }
  }

  @Override
  public ConsumptionLogDao consumptionLogDao() {
    if (_consumptionLogDao != null) {
      return _consumptionLogDao;
    } else {
      synchronized(this) {
        if(_consumptionLogDao == null) {
          _consumptionLogDao = new ConsumptionLogDao_Impl(this);
        }
        return _consumptionLogDao;
      }
    }
  }

  @Override
  public SubstanceDao substanceDao() {
    if (_substanceDao != null) {
      return _substanceDao;
    } else {
      synchronized(this) {
        if(_substanceDao == null) {
          _substanceDao = new SubstanceDao_Impl(this);
        }
        return _substanceDao;
      }
    }
  }

  @Override
  public SportBanDao sportBanDao() {
    if (_sportBanDao != null) {
      return _sportBanDao;
    } else {
      synchronized(this) {
        if(_sportBanDao == null) {
          _sportBanDao = new SportBanDao_Impl(this);
        }
        return _sportBanDao;
      }
    }
  }
}
