package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(10) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `testPlanEntry` (`ID` TEXT NOT NULL, `Favorite` INTEGER NOT NULL, `FirstExaminer` TEXT, `SecondExaminer` TEXT, `Date` TEXT, `ExamForm` TEXT, `Semester` TEXT, `Module` TEXT, `course` TEXT, `termin` TEXT, `room` TEXT, `Status` TEXT, `Hint` TEXT, `TimeStamp` TEXT, PRIMARY KEY(`ID`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Uuid` (`uuid` TEXT NOT NULL, PRIMARY KEY(`uuid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Course` (`cId` TEXT NOT NULL, `courseName` TEXT NOT NULL, `facultyId` TEXT NOT NULL, `chosen` INTEGER NOT NULL, PRIMARY KEY(`cId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Faculty` (`fbid` TEXT NOT NULL, `facName` TEXT NOT NULL, `facShortName` TEXT NOT NULL, PRIMARY KEY(`fbid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fbdbf0c116692c7175a8b25f466317eb')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `testPlanEntry`");
        _db.execSQL("DROP TABLE IF EXISTS `Uuid`");
        _db.execSQL("DROP TABLE IF EXISTS `Course`");
        _db.execSQL("DROP TABLE IF EXISTS `Faculty`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTestPlanEntry = new HashMap<String, TableInfo.Column>(14);
        _columnsTestPlanEntry.put("ID", new TableInfo.Column("ID", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("Favorite", new TableInfo.Column("Favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("FirstExaminer", new TableInfo.Column("FirstExaminer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("SecondExaminer", new TableInfo.Column("SecondExaminer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("Date", new TableInfo.Column("Date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("ExamForm", new TableInfo.Column("ExamForm", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("Semester", new TableInfo.Column("Semester", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("Module", new TableInfo.Column("Module", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("course", new TableInfo.Column("course", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("termin", new TableInfo.Column("termin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("room", new TableInfo.Column("room", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("Status", new TableInfo.Column("Status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("Hint", new TableInfo.Column("Hint", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTestPlanEntry.put("TimeStamp", new TableInfo.Column("TimeStamp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTestPlanEntry = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTestPlanEntry = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTestPlanEntry = new TableInfo("testPlanEntry", _columnsTestPlanEntry, _foreignKeysTestPlanEntry, _indicesTestPlanEntry);
        final TableInfo _existingTestPlanEntry = TableInfo.read(_db, "testPlanEntry");
        if (! _infoTestPlanEntry.equals(_existingTestPlanEntry)) {
          return new RoomOpenHelper.ValidationResult(false, "testPlanEntry(com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry).\n"
                  + " Expected:\n" + _infoTestPlanEntry + "\n"
                  + " Found:\n" + _existingTestPlanEntry);
        }
        final HashMap<String, TableInfo.Column> _columnsUuid = new HashMap<String, TableInfo.Column>(1);
        _columnsUuid.put("uuid", new TableInfo.Column("uuid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUuid = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUuid = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUuid = new TableInfo("Uuid", _columnsUuid, _foreignKeysUuid, _indicesUuid);
        final TableInfo _existingUuid = TableInfo.read(_db, "Uuid");
        if (! _infoUuid.equals(_existingUuid)) {
          return new RoomOpenHelper.ValidationResult(false, "Uuid(com.fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid).\n"
                  + " Expected:\n" + _infoUuid + "\n"
                  + " Found:\n" + _existingUuid);
        }
        final HashMap<String, TableInfo.Column> _columnsCourse = new HashMap<String, TableInfo.Column>(4);
        _columnsCourse.put("cId", new TableInfo.Column("cId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourse.put("courseName", new TableInfo.Column("courseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourse.put("facultyId", new TableInfo.Column("facultyId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourse.put("chosen", new TableInfo.Column("chosen", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCourse = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCourse = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCourse = new TableInfo("Course", _columnsCourse, _foreignKeysCourse, _indicesCourse);
        final TableInfo _existingCourse = TableInfo.read(_db, "Course");
        if (! _infoCourse.equals(_existingCourse)) {
          return new RoomOpenHelper.ValidationResult(false, "Course(com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course).\n"
                  + " Expected:\n" + _infoCourse + "\n"
                  + " Found:\n" + _existingCourse);
        }
        final HashMap<String, TableInfo.Column> _columnsFaculty = new HashMap<String, TableInfo.Column>(3);
        _columnsFaculty.put("fbid", new TableInfo.Column("fbid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFaculty.put("facName", new TableInfo.Column("facName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFaculty.put("facShortName", new TableInfo.Column("facShortName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFaculty = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFaculty = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFaculty = new TableInfo("Faculty", _columnsFaculty, _foreignKeysFaculty, _indicesFaculty);
        final TableInfo _existingFaculty = TableInfo.read(_db, "Faculty");
        if (! _infoFaculty.equals(_existingFaculty)) {
          return new RoomOpenHelper.ValidationResult(false, "Faculty(com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty).\n"
                  + " Expected:\n" + _infoFaculty + "\n"
                  + " Found:\n" + _existingFaculty);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "fbdbf0c116692c7175a8b25f466317eb", "d4cc9ff258aeb418a171ca703ab1db57");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "testPlanEntry","Uuid","Course","Faculty");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `testPlanEntry`");
      _db.execSQL("DELETE FROM `Uuid`");
      _db.execSQL("DELETE FROM `Course`");
      _db.execSQL("DELETE FROM `Faculty`");
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
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }
}
