package com.example.arp.start.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.arp.start.data.PatContract.PatEntry;


/**
 * Manages a local database for weather data.
 */
public class PatDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "pat.db";

    public PatDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude

        // TBD

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + PatEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                PatEntry._ID + " INTEGER PRIMARY KEY ," +
                PatEntry.COLUMN_SERIAL_NUMBER + " TEXT NOT NULL, " +
                // the ID of the location entry associated with this weather data
                PatEntry.COLUMN_COMPANY_NAME + " TEXT NOT NULL, " +
                PatEntry.COLUMN_DAT + " TEXT NOT NULL, " +
                PatEntry.COLUMN_ELIGIBILITY_CRITERIA + " TEXT NOT NULL, " +
                PatEntry.COLUMN_BRANCH + " TEXT NOT NULL," +

                PatEntry.COLUMN_SALARY + " TEXT NOT NULL, " +
                PatEntry.COLUMN_DEADLINE + " TEXT NOT NULL, " +

                PatEntry.COLUMN_OTHER_INFO + " TEXT NOT NULL, ON CONFLICT REPLACE);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
sqLiteDatabase.execSQL(("DROP TABLE IF EXISTS"+ PatEntry.TABLE_NAME));
        onCreate(sqLiteDatabase);
    }
}