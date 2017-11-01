package com.kszych.getdata.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";

    // default db values below
    public static final String DEFAULT_STRING = null;
    public static final int DEFAULT_INT = -1;
    public static final double DEFAULT_REAL = -1.0;

    private static DatabaseHelper _instance = null;

    private static class TPackage {
        final static String TNAME = "Package";

        final static String ID                  = "ID";
        final static String RFID_TAG            = "rfidTag";
        final static String MASS                = "mass";
        final static String DIM_H               = "dimH";
        final static String DIM_W               = "dimW";
        final static String DIM_D               = "dimD";
        final static String ADDITIONAL_INFO     = "additionalInfo";
        final static String BAR_CODE            = "barCode";
        final static String AISLE = "aisle";
        final static String RACK = "rack";
        final static String SHELF = "shelf";
    }

    private static class TPart {
        final static String TNAME = "Part";

        final static String ID                  = "ID";
        final static String NAME                = "name";
        final static String BUY_URL             = "buyUrl";
        final static String PRICE               = "price";
        final static String PRODUCER_NAME       = "producerName";
        final static String ADDITIONAL_INFO     = "additionalInfo";
    }

    private static class TPackagePart {
        final static String TNAME = "PackagePart";

        final static String ID                  = "ID";
        final static String PACKAGE_ID          = "fk_package_id";
        final static String PART_ID             = "fk_part_id";
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(_instance == null) {
            _instance = new DatabaseHelper(context);
        }
        return _instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createPackage = "CREATE TABLE " + TPackage.TNAME + " ("
                + TPackage.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TPackage.RFID_TAG + " TEXT NOT NULL, "
                + TPackage.MASS + " INT, "
                + TPackage.DIM_H + " INT, "
                + TPackage.DIM_W + " INT, "
                + TPackage.DIM_D + " INT, "
                + TPackage.BAR_CODE + " TEXT, "
                + TPackage.AISLE + " TEXT, "
                + TPackage.RACK + " INT, "
                + TPackage.SHELF + " INT, "
                + TPackage.ADDITIONAL_INFO + " TEXT )";
        sqLiteDatabase.execSQL(createPackage);

        String createPart = "CREATE TABLE " + TPart.TNAME + " ("
                + TPart.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TPart.NAME + " TEXT NOT NULL, "
                + TPart.BUY_URL + " TEXT, "
                + TPart.PRICE + " REAL, "
                + TPart.PRODUCER_NAME + " TEXT, "
                + TPart.ADDITIONAL_INFO + " TEXT)";
        sqLiteDatabase.execSQL(createPart);

        String createPackagePart = "CREATE TABLE " + TPackagePart.TNAME + " ("
                + TPackagePart.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TPackagePart.PACKAGE_ID + " INTEGER, "
                + TPackagePart.PART_ID + " INTEGER, "
                + "FOREIGN KEY (" + TPackagePart.PACKAGE_ID + ") REFERENCES Package(" + TPackagePart.ID + "), "
                + "FOREIGN KEY (" + TPackagePart.PART_ID + ") REFERENCES Package(" + TPackagePart.ID + ") )";
        sqLiteDatabase.execSQL(createPackagePart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // TODO ignore?
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void save(Package pack) {
        if(pack.getId() == DEFAULT_INT) {
            // TODO insert
        }
        else {
            // TODO update
        }
    }

    public void save(Part part) {
        if(part.getId() == DEFAULT_INT) {
            //TODO insert
        } else {
            //TODO update
        }
    }

    public void save(PackagePart packagePart) {
        if(packagePart.getId() == DEFAULT_INT) {
            //TODO insert
        } else {
            //TODO update
        }
    }

    public ArrayList<Package> getPackages() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TPackage.TNAME, null);

        ArrayList<Package> packageList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Package newPackage = new Package(
                    cursor.getInt(cursor.getColumnIndexOrThrow(TPackage.ID))
            , cursor.getString(cursor.getColumnIndexOrThrow(TPackage.RFID_TAG)));
            packageList.add(newPackage);
        }

        cursor.close();

        return packageList;
    }

    public long addTestPackage(String dummyRfidTag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(TPackage.RFID_TAG, dummyRfidTag);

        return db.insert(TPackage.TNAME, null, vals);
    }
}
