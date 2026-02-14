package com.example.smartfoods.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.smartfoods.data.entities.ScanResult;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ScanResultDao_Impl implements ScanResultDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScanResult> __insertionAdapterOfScanResult;

  private final EntityDeletionOrUpdateAdapter<ScanResult> __deletionAdapterOfScanResult;

  private final EntityDeletionOrUpdateAdapter<ScanResult> __updateAdapterOfScanResult;

  public ScanResultDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScanResult = new EntityInsertionAdapter<ScanResult>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `scan_results` (`id`,`profileId`,`timestampMs`,`rawText`,`detectedSubstancesJson`,`riskLevel`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ScanResult entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.profileId);
        statement.bindLong(3, entity.timestampMs);
        if (entity.rawText == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.rawText);
        }
        if (entity.detectedSubstancesJson == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.detectedSubstancesJson);
        }
        if (entity.riskLevel == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.riskLevel);
        }
      }
    };
    this.__deletionAdapterOfScanResult = new EntityDeletionOrUpdateAdapter<ScanResult>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `scan_results` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ScanResult entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfScanResult = new EntityDeletionOrUpdateAdapter<ScanResult>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `scan_results` SET `id` = ?,`profileId` = ?,`timestampMs` = ?,`rawText` = ?,`detectedSubstancesJson` = ?,`riskLevel` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ScanResult entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.profileId);
        statement.bindLong(3, entity.timestampMs);
        if (entity.rawText == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.rawText);
        }
        if (entity.detectedSubstancesJson == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.detectedSubstancesJson);
        }
        if (entity.riskLevel == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.riskLevel);
        }
        statement.bindLong(7, entity.id);
      }
    };
  }

  @Override
  public long insert(final ScanResult scanResult) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfScanResult.insertAndReturnId(scanResult);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final ScanResult scanResult) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __deletionAdapterOfScanResult.handle(scanResult);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final ScanResult scanResult) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfScanResult.handle(scanResult);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ScanResult> getRecent(final int limit) {
    final String _sql = "SELECT * FROM scan_results ORDER BY timestampMs DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfTimestampMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMs");
      final int _cursorIndexOfRawText = CursorUtil.getColumnIndexOrThrow(_cursor, "rawText");
      final int _cursorIndexOfDetectedSubstancesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedSubstancesJson");
      final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
      final List<ScanResult> _result = new ArrayList<ScanResult>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ScanResult _item;
        final long _tmpProfileId;
        _tmpProfileId = _cursor.getLong(_cursorIndexOfProfileId);
        final long _tmpTimestampMs;
        _tmpTimestampMs = _cursor.getLong(_cursorIndexOfTimestampMs);
        final String _tmpRawText;
        if (_cursor.isNull(_cursorIndexOfRawText)) {
          _tmpRawText = null;
        } else {
          _tmpRawText = _cursor.getString(_cursorIndexOfRawText);
        }
        final String _tmpDetectedSubstancesJson;
        if (_cursor.isNull(_cursorIndexOfDetectedSubstancesJson)) {
          _tmpDetectedSubstancesJson = null;
        } else {
          _tmpDetectedSubstancesJson = _cursor.getString(_cursorIndexOfDetectedSubstancesJson);
        }
        final String _tmpRiskLevel;
        if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
          _tmpRiskLevel = null;
        } else {
          _tmpRiskLevel = _cursor.getString(_cursorIndexOfRiskLevel);
        }
        _item = new ScanResult(_tmpProfileId,_tmpTimestampMs,_tmpRawText,_tmpDetectedSubstancesJson,_tmpRiskLevel);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ScanResult> getByProfile(final long profileId) {
    final String _sql = "SELECT * FROM scan_results WHERE profileId = ? ORDER BY timestampMs DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, profileId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfTimestampMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMs");
      final int _cursorIndexOfRawText = CursorUtil.getColumnIndexOrThrow(_cursor, "rawText");
      final int _cursorIndexOfDetectedSubstancesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedSubstancesJson");
      final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
      final List<ScanResult> _result = new ArrayList<ScanResult>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ScanResult _item;
        final long _tmpProfileId;
        _tmpProfileId = _cursor.getLong(_cursorIndexOfProfileId);
        final long _tmpTimestampMs;
        _tmpTimestampMs = _cursor.getLong(_cursorIndexOfTimestampMs);
        final String _tmpRawText;
        if (_cursor.isNull(_cursorIndexOfRawText)) {
          _tmpRawText = null;
        } else {
          _tmpRawText = _cursor.getString(_cursorIndexOfRawText);
        }
        final String _tmpDetectedSubstancesJson;
        if (_cursor.isNull(_cursorIndexOfDetectedSubstancesJson)) {
          _tmpDetectedSubstancesJson = null;
        } else {
          _tmpDetectedSubstancesJson = _cursor.getString(_cursorIndexOfDetectedSubstancesJson);
        }
        final String _tmpRiskLevel;
        if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
          _tmpRiskLevel = null;
        } else {
          _tmpRiskLevel = _cursor.getString(_cursorIndexOfRiskLevel);
        }
        _item = new ScanResult(_tmpProfileId,_tmpTimestampMs,_tmpRawText,_tmpDetectedSubstancesJson,_tmpRiskLevel);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ScanResult getLatestBefore(final long profileId, final long timestampMs) {
    final String _sql = "SELECT * FROM scan_results WHERE profileId = ? AND timestampMs <= ? ORDER BY timestampMs DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, profileId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, timestampMs);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfTimestampMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMs");
      final int _cursorIndexOfRawText = CursorUtil.getColumnIndexOrThrow(_cursor, "rawText");
      final int _cursorIndexOfDetectedSubstancesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedSubstancesJson");
      final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
      final ScanResult _result;
      if (_cursor.moveToFirst()) {
        final long _tmpProfileId;
        _tmpProfileId = _cursor.getLong(_cursorIndexOfProfileId);
        final long _tmpTimestampMs;
        _tmpTimestampMs = _cursor.getLong(_cursorIndexOfTimestampMs);
        final String _tmpRawText;
        if (_cursor.isNull(_cursorIndexOfRawText)) {
          _tmpRawText = null;
        } else {
          _tmpRawText = _cursor.getString(_cursorIndexOfRawText);
        }
        final String _tmpDetectedSubstancesJson;
        if (_cursor.isNull(_cursorIndexOfDetectedSubstancesJson)) {
          _tmpDetectedSubstancesJson = null;
        } else {
          _tmpDetectedSubstancesJson = _cursor.getString(_cursorIndexOfDetectedSubstancesJson);
        }
        final String _tmpRiskLevel;
        if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
          _tmpRiskLevel = null;
        } else {
          _tmpRiskLevel = _cursor.getString(_cursorIndexOfRiskLevel);
        }
        _result = new ScanResult(_tmpProfileId,_tmpTimestampMs,_tmpRawText,_tmpDetectedSubstancesJson,_tmpRiskLevel);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
