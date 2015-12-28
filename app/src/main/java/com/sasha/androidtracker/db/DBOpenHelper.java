package com.sasha.androidtracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DBOpenHelper class
 *  to instantiate local storage
 * @see SQLiteOpenHelper, SQLite
 * @author Sasha Antipin
 * @version 0.1
 * @since 24-12-2015
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "TRACKER";

    private static final String DATABASE_NAME = "tracker.db";
    private static final int DATABASE_VERSION = 1;

    //	constants for field references
    public static final String TABLE_TRACKER = "tracker";
    public static final String COLUMN_ID = "trackerId";
    public static final String COLUMN_TIME_STAMP = "timeStamp";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ACCELEROMETER_X = "accelerometerX";
    public static final String COLUMN_ACCELEROMETER_Y = "accelerometerY";
    public static final String COLUMN_ACCELEROMETER_Z = "accelerometerZ";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TRACKER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TIME_STAMP + " TEXT, " +
                    COLUMN_LATITUDE + " NUMERIC, " +
                    COLUMN_LONGITUDE + " NUMERIC, " +
                    COLUMN_ACCELEROMETER_X + " NUMERIC, " +
                    COLUMN_ACCELEROMETER_Y + " NUMERIC, " +
                    COLUMN_ACCELEROMETER_Z + " NUMERIC " +
                    ")";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DBOpenHelper(Context context) {
        //super(context, name, factory, version);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

        Log.i(LOGTAG, "Table has been created");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKER);
        onCreate(db);

        Log.i(LOGTAG, "Database has been upgraded from " +
                oldVersion + " to " + newVersion);
    }

}
