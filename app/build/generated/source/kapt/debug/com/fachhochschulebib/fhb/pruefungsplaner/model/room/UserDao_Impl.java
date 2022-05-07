package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TestPlanEntry> __insertionAdapterOfTestPlanEntry;

  private final EntityInsertionAdapter<Course> __insertionAdapterOfCourse;

  private final EntityInsertionAdapter<Faculty> __insertionAdapterOfFaculty;

  private final EntityDeletionOrUpdateAdapter<TestPlanEntry> __deletionAdapterOfTestPlanEntry;

  private final SharedSQLiteStatement __preparedStmtOfInsertUuid;

  private final SharedSQLiteStatement __preparedStmtOfUpdate;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCourse;

  private final SharedSQLiteStatement __preparedStmtOfUnselectAllFavorites;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllEntries;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTestPlanEntry = new EntityInsertionAdapter<TestPlanEntry>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `testPlanEntry` (`ID`,`Favorite`,`FirstExaminer`,`SecondExaminer`,`Date`,`ExamForm`,`Semester`,`Module`,`course`,`termin`,`room`,`Status`,`Hint`,`TimeStamp`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TestPlanEntry value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        final int _tmp = value.getFavorite() ? 1 : 0;
        stmt.bindLong(2, _tmp);
        if (value.getFirstExaminer() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFirstExaminer());
        }
        if (value.getSecondExaminer() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSecondExaminer());
        }
        if (value.getDate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDate());
        }
        if (value.getExamForm() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getExamForm());
        }
        if (value.getSemester() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSemester());
        }
        if (value.getModule() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getModule());
        }
        if (value.getCourse() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getCourse());
        }
        if (value.getTermin() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getTermin());
        }
        if (value.getRoom() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getRoom());
        }
        if (value.getStatus() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getStatus());
        }
        if (value.getHint() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getHint());
        }
        if (value.getTimeStamp() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getTimeStamp());
        }
      }
    };
    this.__insertionAdapterOfCourse = new EntityInsertionAdapter<Course>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `Course` (`cId`,`courseName`,`facultyId`,`chosen`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Course value) {
        if (value.getSgid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getSgid());
        }
        if (value.getCourseName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getCourseName());
        }
        if (value.getFacultyId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFacultyId());
        }
        final int _tmp = value.getChosen() ? 1 : 0;
        stmt.bindLong(4, _tmp);
      }
    };
    this.__insertionAdapterOfFaculty = new EntityInsertionAdapter<Faculty>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `Faculty` (`fbid`,`facName`,`facShortName`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Faculty value) {
        if (value.getFbid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getFbid());
        }
        if (value.getFacultyName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getFacultyName());
        }
        if (value.getFacultyShortname() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFacultyShortname());
        }
      }
    };
    this.__deletionAdapterOfTestPlanEntry = new EntityDeletionOrUpdateAdapter<TestPlanEntry>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `testPlanEntry` WHERE `ID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TestPlanEntry value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
    this.__preparedStmtOfInsertUuid = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "INSERT INTO Uuid VALUES (?)";
        return _query;
      }
    };
    this.__preparedStmtOfUpdate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE TestPlanEntry SET favorite = ? WHERE ID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCourse = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE Course SET chosen = ? WHERE courseName = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUnselectAllFavorites = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE testPlanEntry SET favorite = 0";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllEntries = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM TestPlanEntry ";
        return _query;
      }
    };
  }

  @Override
  public Object insertEntry(final TestPlanEntry testPlanEntry,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTestPlanEntry.insert(testPlanEntry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertCourses(final List<Course> courses,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCourse.insert(courses);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertFaculty(final Faculty faculty,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFaculty.insert(faculty);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntries(final List<TestPlanEntry> entries,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTestPlanEntry.handleMultiple(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntry(final TestPlanEntry entry,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTestPlanEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertUuid(final String uuid, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfInsertUuid.acquire();
        int _argIndex = 1;
        if (uuid == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, uuid);
        }
        __db.beginTransaction();
        try {
          _stmt.executeInsert();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfInsertUuid.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final boolean favorite, final String id,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdate.acquire();
        int _argIndex = 1;
        final int _tmp = favorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdate.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateCourse(final String courseName, final boolean chosen,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCourse.acquire();
        int _argIndex = 1;
        final int _tmp = chosen ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (courseName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, courseName);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateCourse.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object unselectAllFavorites(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUnselectAllFavorites.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUnselectAllFavorites.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAllEntries(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllEntries.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllEntries.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object getSingleEntryById(final String id,
      final Continuation<? super TestPlanEntry> continuation) {
    final String _sql = "SELECT * FROM TestPlanEntry WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TestPlanEntry>() {
      @Override
      public TestPlanEntry call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final TestPlanEntry _result;
          if(_cursor.moveToFirst()) {
            _result = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _result.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _result.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _result.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _result.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _result.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _result.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _result.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _result.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _result.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _result.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _result.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _result.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _result.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _result.setTimeStamp(_tmpTimeStamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public LiveData<List<TestPlanEntry>> getAllEntriesByDateLiveData() {
    final String _sql = "SELECT * FROM TestPlanEntry ORDER BY date, termin, module";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"TestPlanEntry"}, false, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
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
  public LiveData<List<TestPlanEntry>> getAllEntriesForChosenCoursesByDateLiveData() {
    final String _sql = "SELECT * FROM TestPlanEntry as t INNER JOIN Course as c ON c.courseName LIKE t.course WHERE c.chosen = 1 ORDER BY date, termin, module";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"TestPlanEntry","Course"}, false, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
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
  public LiveData<List<Course>> getAllChosenCoursesLiveData() {
    final String _sql = "SELECT * FROM Course WHERE chosen = 1 ORDER BY courseName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Course"}, false, new Callable<List<Course>>() {
      @Override
      public List<Course> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSgid = CursorUtil.getColumnIndexOrThrow(_cursor, "cId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfFacultyId = CursorUtil.getColumnIndexOrThrow(_cursor, "facultyId");
          final int _cursorIndexOfChosen = CursorUtil.getColumnIndexOrThrow(_cursor, "chosen");
          final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Course _item;
            _item = new Course();
            final String _tmpSgid;
            if (_cursor.isNull(_cursorIndexOfSgid)) {
              _tmpSgid = null;
            } else {
              _tmpSgid = _cursor.getString(_cursorIndexOfSgid);
            }
            _item.setSgid(_tmpSgid);
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            _item.setCourseName(_tmpCourseName);
            final String _tmpFacultyId;
            if (_cursor.isNull(_cursorIndexOfFacultyId)) {
              _tmpFacultyId = null;
            } else {
              _tmpFacultyId = _cursor.getString(_cursorIndexOfFacultyId);
            }
            _item.setFacultyId(_tmpFacultyId);
            final boolean _tmpChosen;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfChosen);
            _tmpChosen = _tmp != 0;
            _item.setChosen(_tmpChosen);
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
  public LiveData<List<TestPlanEntry>> getAllFavoritesLiveData() {
    final String _sql = "SELECT * FROM TestPlanEntry WHERE Favorite = 1 ORDER BY Date, termin, Module";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"TestPlanEntry"}, false, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
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
  public LiveData<List<Faculty>> getAllFacultiesLiveData() {
    final String _sql = "SELECT * FROM Faculty ORDER BY facName LIMIT 100 OFFSET 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Faculty"}, false, new Callable<List<Faculty>>() {
      @Override
      public List<Faculty> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFbid = CursorUtil.getColumnIndexOrThrow(_cursor, "fbid");
          final int _cursorIndexOfFacultyName = CursorUtil.getColumnIndexOrThrow(_cursor, "facName");
          final int _cursorIndexOfFacultyShortname = CursorUtil.getColumnIndexOrThrow(_cursor, "facShortName");
          final List<Faculty> _result = new ArrayList<Faculty>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Faculty _item;
            _item = new Faculty();
            final String _tmpFbid;
            if (_cursor.isNull(_cursorIndexOfFbid)) {
              _tmpFbid = null;
            } else {
              _tmpFbid = _cursor.getString(_cursorIndexOfFbid);
            }
            _item.setFbid(_tmpFbid);
            final String _tmpFacultyName;
            if (_cursor.isNull(_cursorIndexOfFacultyName)) {
              _tmpFacultyName = null;
            } else {
              _tmpFacultyName = _cursor.getString(_cursorIndexOfFacultyName);
            }
            _item.setFacultyName(_tmpFacultyName);
            final String _tmpFacultyShortname;
            if (_cursor.isNull(_cursorIndexOfFacultyShortname)) {
              _tmpFacultyShortname = null;
            } else {
              _tmpFacultyShortname = _cursor.getString(_cursorIndexOfFacultyShortname);
            }
            _item.setFacultyShortname(_tmpFacultyShortname);
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
  public LiveData<List<String>> getFirstExaminerNamesLiveData() {
    final String _sql = "SELECT DISTINCT firstExaminer FROM testPlanEntry ORDER BY firstExaminer";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"testPlanEntry"}, false, new Callable<List<String>>() {
      @Override
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
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
  public Object getAllEntries(final Continuation<? super List<TestPlanEntry>> continuation) {
    final String _sql = "SELECT * FROM TestPlanEntry ORDER BY date, termin, module";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getCourseById(final String id, final Continuation<? super Course> continuation) {
    final String _sql = "SELECT * FROM Course WHERE cId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Course>() {
      @Override
      public Course call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSgid = CursorUtil.getColumnIndexOrThrow(_cursor, "cId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfFacultyId = CursorUtil.getColumnIndexOrThrow(_cursor, "facultyId");
          final int _cursorIndexOfChosen = CursorUtil.getColumnIndexOrThrow(_cursor, "chosen");
          final Course _result;
          if(_cursor.moveToFirst()) {
            _result = new Course();
            final String _tmpSgid;
            if (_cursor.isNull(_cursorIndexOfSgid)) {
              _tmpSgid = null;
            } else {
              _tmpSgid = _cursor.getString(_cursorIndexOfSgid);
            }
            _result.setSgid(_tmpSgid);
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            _result.setCourseName(_tmpCourseName);
            final String _tmpFacultyId;
            if (_cursor.isNull(_cursorIndexOfFacultyId)) {
              _tmpFacultyId = null;
            } else {
              _tmpFacultyId = _cursor.getString(_cursorIndexOfFacultyId);
            }
            _result.setFacultyId(_tmpFacultyId);
            final boolean _tmpChosen;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfChosen);
            _tmpChosen = _tmp != 0;
            _result.setChosen(_tmpChosen);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getAllCourses(final Continuation<? super List<Course>> continuation) {
    final String _sql = "SELECT * FROM Course ORDER BY courseName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Course>>() {
      @Override
      public List<Course> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSgid = CursorUtil.getColumnIndexOrThrow(_cursor, "cId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfFacultyId = CursorUtil.getColumnIndexOrThrow(_cursor, "facultyId");
          final int _cursorIndexOfChosen = CursorUtil.getColumnIndexOrThrow(_cursor, "chosen");
          final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Course _item;
            _item = new Course();
            final String _tmpSgid;
            if (_cursor.isNull(_cursorIndexOfSgid)) {
              _tmpSgid = null;
            } else {
              _tmpSgid = _cursor.getString(_cursorIndexOfSgid);
            }
            _item.setSgid(_tmpSgid);
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            _item.setCourseName(_tmpCourseName);
            final String _tmpFacultyId;
            if (_cursor.isNull(_cursorIndexOfFacultyId)) {
              _tmpFacultyId = null;
            } else {
              _tmpFacultyId = _cursor.getString(_cursorIndexOfFacultyId);
            }
            _item.setFacultyId(_tmpFacultyId);
            final boolean _tmpChosen;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfChosen);
            _tmpChosen = _tmp != 0;
            _item.setChosen(_tmpChosen);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getFavorites(final boolean favorite,
      final Continuation<? super List<TestPlanEntry>> continuation) {
    final String _sql = "SELECT * FROM TestPlanEntry WHERE favorite = ? ORDER BY date, termin, module";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final int _tmp = favorite ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp_1 != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getEntriesByCourseName(final String course, final boolean favorite,
      final Continuation<? super List<TestPlanEntry>> continuation) {
    final String _sql = "SELECT * FROM TestPlanEntry WHERE course = ? AND favorite = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (course == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, course);
    }
    _argIndex = 2;
    final int _tmp = favorite ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp_1 != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getEntriesByCourseName(final String course,
      final Continuation<? super List<TestPlanEntry>> continuation) {
    final String _sql = "SELECT * FROM TestPlanEntry WHERE course LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (course == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, course);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getChosenCourseIds(final Continuation<? super List<String>> continuation) {
    final String _sql = "SELECT cId FROM Course WHERE chosen = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getAllCoursesByFacultyId(final String facultyId,
      final Continuation<? super List<Course>> continuation) {
    final String _sql = "SELECT * FROM Course WHERE facultyId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (facultyId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, facultyId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Course>>() {
      @Override
      public List<Course> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSgid = CursorUtil.getColumnIndexOrThrow(_cursor, "cId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfFacultyId = CursorUtil.getColumnIndexOrThrow(_cursor, "facultyId");
          final int _cursorIndexOfChosen = CursorUtil.getColumnIndexOrThrow(_cursor, "chosen");
          final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Course _item;
            _item = new Course();
            final String _tmpSgid;
            if (_cursor.isNull(_cursorIndexOfSgid)) {
              _tmpSgid = null;
            } else {
              _tmpSgid = _cursor.getString(_cursorIndexOfSgid);
            }
            _item.setSgid(_tmpSgid);
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            _item.setCourseName(_tmpCourseName);
            final String _tmpFacultyId;
            if (_cursor.isNull(_cursorIndexOfFacultyId)) {
              _tmpFacultyId = null;
            } else {
              _tmpFacultyId = _cursor.getString(_cursorIndexOfFacultyId);
            }
            _item.setFacultyId(_tmpFacultyId);
            final boolean _tmpChosen;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfChosen);
            _tmpChosen = _tmp != 0;
            _item.setChosen(_tmpChosen);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getEntryById(final String id,
      final Continuation<? super TestPlanEntry> continuation) {
    final String _sql = "SELECT * FROM TestPlanEntry WHERE ID = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TestPlanEntry>() {
      @Override
      public TestPlanEntry call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final TestPlanEntry _result;
          if(_cursor.moveToFirst()) {
            _result = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _result.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _result.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _result.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _result.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _result.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _result.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _result.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _result.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _result.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _result.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _result.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _result.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _result.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _result.setTimeStamp(_tmpTimeStamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getUuid(final Continuation<? super Uuid> continuation) {
    final String _sql = "SELECT * FROM Uuid";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Uuid>() {
      @Override
      public Uuid call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
          final Uuid _result;
          if(_cursor.moveToFirst()) {
            _result = new Uuid();
            final String _tmpUuid;
            if (_cursor.isNull(_cursorIndexOfUuid)) {
              _tmpUuid = null;
            } else {
              _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
            }
            _result.setUuid(_tmpUuid);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getFavoritesForCourse(final String courseName,
      final Continuation<? super List<TestPlanEntry>> continuation) {
    final String _sql = "SELECT * FROM TestPlanEntry WHERE course = ? AND favorite = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (courseName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, courseName);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TestPlanEntry>>() {
      @Override
      public List<TestPlanEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "Favorite");
          final int _cursorIndexOfFirstExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstExaminer");
          final int _cursorIndexOfSecondExaminer = CursorUtil.getColumnIndexOrThrow(_cursor, "SecondExaminer");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
          final int _cursorIndexOfExamForm = CursorUtil.getColumnIndexOrThrow(_cursor, "ExamForm");
          final int _cursorIndexOfSemester = CursorUtil.getColumnIndexOrThrow(_cursor, "Semester");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "Module");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfTermin = CursorUtil.getColumnIndexOrThrow(_cursor, "termin");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "Status");
          final int _cursorIndexOfHint = CursorUtil.getColumnIndexOrThrow(_cursor, "Hint");
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
          final List<TestPlanEntry> _result = new ArrayList<TestPlanEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TestPlanEntry _item;
            _item = new TestPlanEntry();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item.setFavorite(_tmpFavorite);
            final String _tmpFirstExaminer;
            if (_cursor.isNull(_cursorIndexOfFirstExaminer)) {
              _tmpFirstExaminer = null;
            } else {
              _tmpFirstExaminer = _cursor.getString(_cursorIndexOfFirstExaminer);
            }
            _item.setFirstExaminer(_tmpFirstExaminer);
            final String _tmpSecondExaminer;
            if (_cursor.isNull(_cursorIndexOfSecondExaminer)) {
              _tmpSecondExaminer = null;
            } else {
              _tmpSecondExaminer = _cursor.getString(_cursorIndexOfSecondExaminer);
            }
            _item.setSecondExaminer(_tmpSecondExaminer);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpExamForm;
            if (_cursor.isNull(_cursorIndexOfExamForm)) {
              _tmpExamForm = null;
            } else {
              _tmpExamForm = _cursor.getString(_cursorIndexOfExamForm);
            }
            _item.setExamForm(_tmpExamForm);
            final String _tmpSemester;
            if (_cursor.isNull(_cursorIndexOfSemester)) {
              _tmpSemester = null;
            } else {
              _tmpSemester = _cursor.getString(_cursorIndexOfSemester);
            }
            _item.setSemester(_tmpSemester);
            final String _tmpModule;
            if (_cursor.isNull(_cursorIndexOfModule)) {
              _tmpModule = null;
            } else {
              _tmpModule = _cursor.getString(_cursorIndexOfModule);
            }
            _item.setModule(_tmpModule);
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            _item.setCourse(_tmpCourse);
            final String _tmpTermin;
            if (_cursor.isNull(_cursorIndexOfTermin)) {
              _tmpTermin = null;
            } else {
              _tmpTermin = _cursor.getString(_cursorIndexOfTermin);
            }
            _item.setTermin(_tmpTermin);
            final String _tmpRoom;
            if (_cursor.isNull(_cursorIndexOfRoom)) {
              _tmpRoom = null;
            } else {
              _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            }
            _item.setRoom(_tmpRoom);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpHint;
            if (_cursor.isNull(_cursorIndexOfHint)) {
              _tmpHint = null;
            } else {
              _tmpHint = _cursor.getString(_cursorIndexOfHint);
            }
            _item.setHint(_tmpHint);
            final String _tmpTimeStamp;
            if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
              _tmpTimeStamp = null;
            } else {
              _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
            }
            _item.setTimeStamp(_tmpTimeStamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getCourseByName(final String name,
      final Continuation<? super Course> continuation) {
    final String _sql = "SELECT * FROM Course WHERE courseName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Course>() {
      @Override
      public Course call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSgid = CursorUtil.getColumnIndexOrThrow(_cursor, "cId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfFacultyId = CursorUtil.getColumnIndexOrThrow(_cursor, "facultyId");
          final int _cursorIndexOfChosen = CursorUtil.getColumnIndexOrThrow(_cursor, "chosen");
          final Course _result;
          if(_cursor.moveToFirst()) {
            _result = new Course();
            final String _tmpSgid;
            if (_cursor.isNull(_cursorIndexOfSgid)) {
              _tmpSgid = null;
            } else {
              _tmpSgid = _cursor.getString(_cursorIndexOfSgid);
            }
            _result.setSgid(_tmpSgid);
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            _result.setCourseName(_tmpCourseName);
            final String _tmpFacultyId;
            if (_cursor.isNull(_cursorIndexOfFacultyId)) {
              _tmpFacultyId = null;
            } else {
              _tmpFacultyId = _cursor.getString(_cursorIndexOfFacultyId);
            }
            _result.setFacultyId(_tmpFacultyId);
            final boolean _tmpChosen;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfChosen);
            _tmpChosen = _tmp != 0;
            _result.setChosen(_tmpChosen);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getFacultyById(final String id, final Continuation<? super Faculty> continuation) {
    final String _sql = "SELECT * FROM Faculty WHERE fbid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Faculty>() {
      @Override
      public Faculty call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFbid = CursorUtil.getColumnIndexOrThrow(_cursor, "fbid");
          final int _cursorIndexOfFacultyName = CursorUtil.getColumnIndexOrThrow(_cursor, "facName");
          final int _cursorIndexOfFacultyShortname = CursorUtil.getColumnIndexOrThrow(_cursor, "facShortName");
          final Faculty _result;
          if(_cursor.moveToFirst()) {
            _result = new Faculty();
            final String _tmpFbid;
            if (_cursor.isNull(_cursorIndexOfFbid)) {
              _tmpFbid = null;
            } else {
              _tmpFbid = _cursor.getString(_cursorIndexOfFbid);
            }
            _result.setFbid(_tmpFbid);
            final String _tmpFacultyName;
            if (_cursor.isNull(_cursorIndexOfFacultyName)) {
              _tmpFacultyName = null;
            } else {
              _tmpFacultyName = _cursor.getString(_cursorIndexOfFacultyName);
            }
            _result.setFacultyName(_tmpFacultyName);
            final String _tmpFacultyShortname;
            if (_cursor.isNull(_cursorIndexOfFacultyShortname)) {
              _tmpFacultyShortname = null;
            } else {
              _tmpFacultyShortname = _cursor.getString(_cursorIndexOfFacultyShortname);
            }
            _result.setFacultyShortname(_tmpFacultyShortname);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
