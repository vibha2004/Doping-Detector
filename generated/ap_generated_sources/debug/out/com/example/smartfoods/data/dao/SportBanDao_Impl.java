package com.example.smartfoods.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.smartfoods.data.entities.SportBan;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SportBanDao_Impl implements SportBanDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SportBan> __insertionAdapterOfSportBan;

  public SportBanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSportBan = new EntityInsertionAdapter<SportBan>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `sport_bans` (`id`,`category`,`sportLower`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final SportBan entity) {
        statement.bindLong(1, entity.id);
        if (entity.category == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.category);
        }
        if (entity.sportLower == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.sportLower);
        }
      }
    };
  }

  @Override
  public long insert(final SportBan ban) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfSportBan.insertAndReturnId(ban);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<SportBan> getByCategory(final String category) {
    final String _sql = "SELECT * FROM sport_bans WHERE category = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfSportLower = CursorUtil.getColumnIndexOrThrow(_cursor, "sportLower");
      final List<SportBan> _result = new ArrayList<SportBan>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SportBan _item;
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        final String _tmpSportLower;
        if (_cursor.isNull(_cursorIndexOfSportLower)) {
          _tmpSportLower = null;
        } else {
          _tmpSportLower = _cursor.getString(_cursorIndexOfSportLower);
        }
        _item = new SportBan(_tmpCategory,_tmpSportLower);
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
  public int count() {
    final String _sql = "SELECT COUNT(*) FROM sport_bans";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
