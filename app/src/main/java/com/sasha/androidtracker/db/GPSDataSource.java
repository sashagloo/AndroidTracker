package com.sasha.androidtracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sasha.androidtracker.model.GPSData;

import java.util.ArrayList;
import java.util.List;

/**
 * GPSDataSource class
 *  to manage local storage on SQLlite
 * @see DBOpenHelper
 * @author Sasha Antipin
 * @version 0.1
 * @since 24-12-2015
 */

public class GPSDataSource {

    public static final String LOGTAG="TRACKER";

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns = {    // contain one string for each column in the table
            DBOpenHelper.COLUMN_ID,
            DBOpenHelper.COLUMN_TIME_STAMP,
            DBOpenHelper.COLUMN_LATITUDE,
            DBOpenHelper.COLUMN_LONGITUDE,
            DBOpenHelper.COLUMN_ACCELEROMETER_X,
            DBOpenHelper.COLUMN_ACCELEROMETER_Y,
            DBOpenHelper.COLUMN_ACCELEROMETER_Z,
    };
    public GPSDataSource(Context context) {
        dbhelper =  new DBOpenHelper(context);
    }

    /**
     * method opens connection to the dataBase
     */
    public void open() {
        Log.i(LOGTAG, "Database opened");

        /**
         * get reference to the connection to the database
         *  use that connection to inserting, retrieving, updating and deleting data.
         *  will trigger the onCreate() method of dataBaseOpenHelper class
         */
        database = dbhelper.getWritableDatabase();
    }

    /**
     *  method closes connection to the database
     */
    public void close() {
        Log.i(LOGTAG, "Database closed");
        dbhelper.close();
    }

    /**
     * methode to create Tour object
     * in order to pass it into the database
     * @param data GPSData object
     * @return GPSData object
     */
    public GPSData create(GPSData data) {

        /** use ContentValues in place of the SQL statement ------------------*/
        ContentValues values = new ContentValues();

        // put items into the map where the key is the name of the column
        // and the value is the value we want to insert
        values.put(DBOpenHelper.COLUMN_TIME_STAMP, data.getTimeStamp());
        values.put(DBOpenHelper.COLUMN_LATITUDE, data.getLatitude());
        values.put(DBOpenHelper.COLUMN_LONGITUDE, data.getLongitude());
        values.put(DBOpenHelper.COLUMN_ACCELEROMETER_X, data.getAccelerometerX());
        values.put(DBOpenHelper.COLUMN_ACCELEROMETER_Y, data.getAccelerometerY());
        values.put(DBOpenHelper.COLUMN_ACCELEROMETER_Z, data.getAccelerometerZ());

        /** insert a row into the database table ------------
         * receive back automatically assigned primary key value */
        long insertId = database.insert(DBOpenHelper.TABLE_TRACKER,  // name of the table
                null,                           // name of the column
                values);                       //content values object
        data.setId(insertId);

        return data;
    }

    /** method to retrive all rows and all columns ot the same table
     *  @return a list of tour objects
     */
    public List<GPSData> findAll() {

        /**  querying the single table
            return a reference to the data that's returned from the query */
        Cursor cursor = database.query(
                DBOpenHelper.TABLE_TRACKER, // table
                allColumns,                     // columns
                null,                           // WHERE selection
                null,                           // selectionArgs
                null,                           // groupBy
                null,                           // having
                null                            // orderBy
        );
        Log.i(LOGTAG, "returnded " + cursor.getCount() + " rows");

        List<GPSData> data = cursorToList(cursor);
        return data;
    }

    private List<GPSData> cursorToList(Cursor cursor) {
        List<GPSData> dataList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                GPSData data = new GPSData();
                data.setId(
                        cursor.getLong(
                                cursor.getColumnIndex(
                                        DBOpenHelper.COLUMN_ID)));
                data.setTimeStamp(
                        cursor.getString(
                                cursor.getColumnIndex(
                                        DBOpenHelper.COLUMN_TIME_STAMP)));
                data.setLatitude(
                        cursor.getDouble(
                                cursor.getColumnIndex(
                                        DBOpenHelper.COLUMN_LATITUDE)));
                data.setLongitude(
                        cursor.getDouble(
                                cursor.getColumnIndex(
                                        DBOpenHelper.COLUMN_LONGITUDE)));
                data.setAccelerometerX(
                        cursor.getFloat(
                                cursor.getColumnIndex(
                                        DBOpenHelper.COLUMN_ACCELEROMETER_X)));
                data.setAccelerometerY(
                        cursor.getFloat(
                                cursor.getColumnIndex(
                                        DBOpenHelper.COLUMN_ACCELEROMETER_Y)));
                data.setAccelerometerZ(
                        cursor.getFloat(
                                cursor.getColumnIndex(
                                        DBOpenHelper.COLUMN_ACCELEROMETER_Z)));

                dataList.add(data);
            }
        }
        return dataList;
    }

    /**
     * Quickly deletes all records from a table
     *
     * @param
     */
    public void clearData() {

        database.execSQL("DELETE FROM " + DBOpenHelper.TABLE_TRACKER );

        Log.i(LOGTAG, "All records has been deleted from the table");
    }

}
