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
import com.example.smartfoods.data.entities.ConsumptionLog;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ConsumptionLogDao_Impl implements ConsumptionLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ConsumptionLog> __insertionAdapterOfConsumptionLog;

  private final EntityDeletionOrUpdateAdapter<ConsumptionLog> __deletionAdapterOfConsumptionLog;

  private final EntityDeletionOrUpdateAdapter<ConsumptionLog> __updateAdapterOfConsumptionLog;

  public ConsumptionLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfConsumptionLog = new EntityInsertionAdapter<ConsumptionLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `consumption_logs` (`id`,`profileId`,`timestampMs`,`productName`,`substanceName`,`dosageAmount`,`dosageUnit`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ConsumptionLog entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.profileId);
        statement.bindLong(3, entity.timestampMs);
        if (entity.productName == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.productName);
        }
        if (entity.substanceName == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.substanceName);
        }
        statement.bindDouble(6, entity.dosageAmount);
        if (entity.dosageUnit == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.dosageUnit);
        }
      }
    };
    this.__deletionAdapterOfConsumptionLog = new EntityDeletionOrUpdateAdapter<ConsumptionLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `consumption_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ConsumptionLog entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfConsumptionLog = new EntityDeletionOrUpdateAdapter<ConsumptionLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `consumption_logs` SET `id` = ?,`profileId` = ?,`timestampMs` = ?,`productName` = ?,`substanceName` = ?,`dosageAmount` = ?,`dosageUnit` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ConsumptionLog entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.profileId);
        statement.bindLong(3, entity.timestampMs);
        if (entity.productName == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.productName);
        }
        if (entity.substanceName == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.substanceName);
        }
        statement.bindDouble(6, entity.dosageAmount);
        if (entity.dosageUnit == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.dosageUnit);
        }
        statement.bindLong(8, entity.id);
      }
    };
  }

  @Override
  public long insert(final ConsumptionLog log) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfConsumptionLog.insertAndReturnId(log);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final ConsumptionLog log) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __deletionAdapterOfConsumptionLog.handle(log);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final ConsumptionLog log) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfConsumptionLog.handle(log);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ConsumptionLog> getByProfile(final long profileId) {
    final String _sql = "SELECT * FROM consumption_logs WHERE profileId = ? ORDER BY timestampMs DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, profileId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfTimestampMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMs");
      final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
      final int _cursorIndexOfSubstanceName = CursorUtil.getColumnIndexOrThrow(_cursor, "substanceName");
      final int _cursorIndexOfDosageAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageAmount");
      final int _cursorIndexOfDosageUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageUnit");
      final List<ConsumptionLog> _result = new ArrayList<ConsumptionLog>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ConsumptionLog _item;
        final long _tmpProfileId;
        _tmpProfileId = _cursor.getLong(_cursorIndexOfProfileId);
        final long _tmpTimestampMs;
        _tmpTimestampMs = _cursor.getLong(_cursorIndexOfTimestampMs);
        final String _tmpProductName;
        if (_cursor.isNull(_cursorIndexOfProductName)) {
          _tmpProductName = null;
        } else {
          _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
        }
        final String _tmpSubstanceName;
        if (_cursor.isNull(_cursorIndexOfSubstanceName)) {
          _tmpSubstanceName = null;
        } else {
          _tmpSubstanceName = _cursor.getString(_cursorIndexOfSubstanceName);
        }
        final double _tmpDosageAmount;
        _tmpDosageAmount = _cursor.getDouble(_cursorIndexOfDosageAmount);
        final String _tmpDosageUnit;
        if (_cursor.isNull(_cursorIndexOfDosageUnit)) {
          _tmpDosageUnit = null;
        } else {
          _tmpDosageUnit = _cursor.getString(_cursorIndexOfDosageUnit);
        }
        _item = new ConsumptionLog(_tmpProfileId,_tmpTimestampMs,_tmpProductName,_tmpSubstanceName,_tmpDosageAmount,_tmpDosageUnit);
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
  public List<ConsumptionLog> getRecent(final int limit) {
    final String _sql = "SELECT * FROM consumption_logs ORDER BY timestampMs DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfTimestampMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMs");
      final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
      final int _cursorIndexOfSubstanceName = CursorUtil.getColumnIndexOrThrow(_cursor, "substanceName");
      final int _cursorIndexOfDosageAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageAmount");
      final int _cursorIndexOfDosageUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageUnit");
      final List<ConsumptionLog> _result = new ArrayList<ConsumptionLog>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ConsumptionLog _item;
        final long _tmpProfileId;
        _tmpProfileId = _cursor.getLong(_cursorIndexOfProfileId);
        final long _tmpTimestampMs;
        _tmpTimestampMs = _cursor.getLong(_cursorIndexOfTimestampMs);
        final String _tmpProductName;
        if (_cursor.isNull(_cursorIndexOfProductName)) {
          _tmpProductName = null;
        } else {
          _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
        }
        final String _tmpSubstanceName;
        if (_cursor.isNull(_cursorIndexOfSubstanceName)) {
          _tmpSubstanceName = null;
        } else {
          _tmpSubstanceName = _cursor.getString(_cursorIndexOfSubstanceName);
        }
        final double _tmpDosageAmount;
        _tmpDosageAmount = _cursor.getDouble(_cursorIndexOfDosageAmount);
        final String _tmpDosageUnit;
        if (_cursor.isNull(_cursorIndexOfDosageUnit)) {
          _tmpDosageUnit = null;
        } else {
          _tmpDosageUnit = _cursor.getString(_cursorIndexOfDosageUnit);
        }
        _item = new ConsumptionLog(_tmpProfileId,_tmpTimestampMs,_tmpProductName,_tmpSubstanceName,_tmpDosageAmount,_tmpDosageUnit);
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
  public int countByProductSince(final long profileId, final String productName,
      final long sinceMs) {
    final String _sql = "SELECT COUNT(*) FROM consumption_logs WHERE profileId = ? AND productName = ? AND timestampMs >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, profileId);
    _argIndex = 2;
    if (productName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, productName);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, sinceMs);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
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
