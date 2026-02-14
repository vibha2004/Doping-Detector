package com.example.smartfoods.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.smartfoods.data.entities.Profile;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ProfileDao_Impl implements ProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Profile> __insertionAdapterOfProfile;

  private final EntityDeletionOrUpdateAdapter<Profile> __deletionAdapterOfProfile;

  private final EntityDeletionOrUpdateAdapter<Profile> __updateAdapterOfProfile;

  public ProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProfile = new EntityInsertionAdapter<Profile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `profiles` (`id`,`displayName`,`sport`,`competitionStatus`,`nextCompetitionDate`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Profile entity) {
        statement.bindLong(1, entity.id);
        if (entity.displayName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.displayName);
        }
        if (entity.sport == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.sport);
        }
        if (entity.competitionStatus == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.competitionStatus);
        }
        if (entity.nextCompetitionDate == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.nextCompetitionDate);
        }
      }
    };
    this.__deletionAdapterOfProfile = new EntityDeletionOrUpdateAdapter<Profile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `profiles` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Profile entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfProfile = new EntityDeletionOrUpdateAdapter<Profile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `profiles` SET `id` = ?,`displayName` = ?,`sport` = ?,`competitionStatus` = ?,`nextCompetitionDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Profile entity) {
        statement.bindLong(1, entity.id);
        if (entity.displayName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.displayName);
        }
        if (entity.sport == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.sport);
        }
        if (entity.competitionStatus == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.competitionStatus);
        }
        if (entity.nextCompetitionDate == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.nextCompetitionDate);
        }
        statement.bindLong(6, entity.id);
      }
    };
  }

  @Override
  public long insert(final Profile profile) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfProfile.insertAndReturnId(profile);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final Profile profile) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __deletionAdapterOfProfile.handle(profile);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final Profile profile) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfProfile.handle(profile);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Profile getPrimaryProfile() {
    final String _sql = "SELECT * FROM profiles ORDER BY id LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
      final int _cursorIndexOfCompetitionStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "competitionStatus");
      final int _cursorIndexOfNextCompetitionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextCompetitionDate");
      final Profile _result;
      if (_cursor.moveToFirst()) {
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final String _tmpSport;
        if (_cursor.isNull(_cursorIndexOfSport)) {
          _tmpSport = null;
        } else {
          _tmpSport = _cursor.getString(_cursorIndexOfSport);
        }
        final String _tmpCompetitionStatus;
        if (_cursor.isNull(_cursorIndexOfCompetitionStatus)) {
          _tmpCompetitionStatus = null;
        } else {
          _tmpCompetitionStatus = _cursor.getString(_cursorIndexOfCompetitionStatus);
        }
        final String _tmpNextCompetitionDate;
        if (_cursor.isNull(_cursorIndexOfNextCompetitionDate)) {
          _tmpNextCompetitionDate = null;
        } else {
          _tmpNextCompetitionDate = _cursor.getString(_cursorIndexOfNextCompetitionDate);
        }
        _result = new Profile(_tmpDisplayName,_tmpSport,_tmpCompetitionStatus,_tmpNextCompetitionDate);
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

  @Override
  public LiveData<List<Profile>> getAll() {
    final String _sql = "SELECT * FROM profiles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"profiles"}, false, new Callable<List<Profile>>() {
      @Override
      @Nullable
      public List<Profile> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfCompetitionStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "competitionStatus");
          final int _cursorIndexOfNextCompetitionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextCompetitionDate");
          final List<Profile> _result = new ArrayList<Profile>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Profile _item;
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpSport;
            if (_cursor.isNull(_cursorIndexOfSport)) {
              _tmpSport = null;
            } else {
              _tmpSport = _cursor.getString(_cursorIndexOfSport);
            }
            final String _tmpCompetitionStatus;
            if (_cursor.isNull(_cursorIndexOfCompetitionStatus)) {
              _tmpCompetitionStatus = null;
            } else {
              _tmpCompetitionStatus = _cursor.getString(_cursorIndexOfCompetitionStatus);
            }
            final String _tmpNextCompetitionDate;
            if (_cursor.isNull(_cursorIndexOfNextCompetitionDate)) {
              _tmpNextCompetitionDate = null;
            } else {
              _tmpNextCompetitionDate = _cursor.getString(_cursorIndexOfNextCompetitionDate);
            }
            _item = new Profile(_tmpDisplayName,_tmpSport,_tmpCompetitionStatus,_tmpNextCompetitionDate);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<Profile> getAllImmediate() {
    final String _sql = "SELECT * FROM profiles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
      final int _cursorIndexOfCompetitionStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "competitionStatus");
      final int _cursorIndexOfNextCompetitionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextCompetitionDate");
      final List<Profile> _result = new ArrayList<Profile>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Profile _item;
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final String _tmpSport;
        if (_cursor.isNull(_cursorIndexOfSport)) {
          _tmpSport = null;
        } else {
          _tmpSport = _cursor.getString(_cursorIndexOfSport);
        }
        final String _tmpCompetitionStatus;
        if (_cursor.isNull(_cursorIndexOfCompetitionStatus)) {
          _tmpCompetitionStatus = null;
        } else {
          _tmpCompetitionStatus = _cursor.getString(_cursorIndexOfCompetitionStatus);
        }
        final String _tmpNextCompetitionDate;
        if (_cursor.isNull(_cursorIndexOfNextCompetitionDate)) {
          _tmpNextCompetitionDate = null;
        } else {
          _tmpNextCompetitionDate = _cursor.getString(_cursorIndexOfNextCompetitionDate);
        }
        _item = new Profile(_tmpDisplayName,_tmpSport,_tmpCompetitionStatus,_tmpNextCompetitionDate);
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
  public Profile getById(final long id) {
    final String _sql = "SELECT * FROM profiles WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
      final int _cursorIndexOfCompetitionStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "competitionStatus");
      final int _cursorIndexOfNextCompetitionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextCompetitionDate");
      final Profile _result;
      if (_cursor.moveToFirst()) {
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final String _tmpSport;
        if (_cursor.isNull(_cursorIndexOfSport)) {
          _tmpSport = null;
        } else {
          _tmpSport = _cursor.getString(_cursorIndexOfSport);
        }
        final String _tmpCompetitionStatus;
        if (_cursor.isNull(_cursorIndexOfCompetitionStatus)) {
          _tmpCompetitionStatus = null;
        } else {
          _tmpCompetitionStatus = _cursor.getString(_cursorIndexOfCompetitionStatus);
        }
        final String _tmpNextCompetitionDate;
        if (_cursor.isNull(_cursorIndexOfNextCompetitionDate)) {
          _tmpNextCompetitionDate = null;
        } else {
          _tmpNextCompetitionDate = _cursor.getString(_cursorIndexOfNextCompetitionDate);
        }
        _result = new Profile(_tmpDisplayName,_tmpSport,_tmpCompetitionStatus,_tmpNextCompetitionDate);
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
