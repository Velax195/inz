package com.kszych.getdata.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";

    // default db values below
    public static final String DEFAULT_STRING = null;
    public static final int DEFAULT_INT = -1;
    public static final double DEFAULT_REAL = -1.0;
    public static final String NULL_VAL = "N/A";
    private static DatabaseHelper _instance = null;

    public static class TPackage {
        public final static String TNAME = "Package";

        final static String ID                  = "ID";
        final static String RFID_TAG            = "rfidTag";
        final static String MASS                = "mass";
        final static String DIM_H               = "dimH";
        final static String DIM_W               = "dimW";
        final static String DIM_D               = "dimD";
        final static String ADDITIONAL_INFO     = "additionalInfo";
        final static String BAR_CODE            = "barCode";
        final static String AISLE               = "aisle";
        final static String RACK                = "rack";
        final static String SHELF               = "shelf";
    }

    public static class TPart {
        public final static String TNAME = "Part";

        final static String ID                  = "ID";
        final static String NAME                = "name";
        final static String BUY_URL             = "buyUrl";
        final static String PRICE               = "price";
        final static String PRODUCER_NAME       = "producerName";
        final static String ADDITIONAL_INFO     = "additionalInfo";
    }

    public static class TPackagePart {
        public final static String TNAME = "PackagePart";

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
                + TPackage.RFID_TAG + " TEXT UNIQUE NOT NULL, "
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

    private int safeGetVal(boolean isNull, int val) {
        if(isNull) {
            return DEFAULT_INT;
        }
        return val;
    }

    private String safeGetVal(boolean isNull, String val) {
        if(isNull) {
            return DEFAULT_STRING;
        }
        return val;
    }

    private double safeGetVal(boolean isNull, double val) {
        if(isNull) {
            return DEFAULT_REAL;
        }
        return val;
    }

    public int count(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public ArrayList<Package> getPackages() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TPackage.TNAME, null);

        int piId = cursor.getColumnIndexOrThrow(TPackage.ID);

        int piRFID = cursor.getColumnIndexOrThrow(TPackage.RFID_TAG);
        int piMass = cursor.getColumnIndexOrThrow(TPackage.MASS);
        int piDimH = cursor.getColumnIndexOrThrow(TPackage.DIM_H);
        int piDimW = cursor.getColumnIndexOrThrow(TPackage.DIM_W);
        int piDimD = cursor.getColumnIndexOrThrow(TPackage.DIM_D);
        int piAdditional = cursor.getColumnIndexOrThrow(TPackage.ADDITIONAL_INFO);
        int piBarCode = cursor.getColumnIndexOrThrow(TPackage.BAR_CODE);
        int piAisle = cursor.getColumnIndexOrThrow(TPackage.AISLE);
        int piRack = cursor.getColumnIndexOrThrow(TPackage.RACK);
        int piShelf = cursor.getColumnIndexOrThrow(TPackage.SHELF);

        ArrayList<Package> packageList = new ArrayList<>();
        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            Package newPackage = new Package(
                cursor.getInt(piId)
                , cursor.getString(piRFID)
                , safeGetVal(cursor.isNull(piMass), cursor.getInt(piMass))
                , safeGetVal(cursor.isNull(piDimH), cursor.getInt(piDimH))
                , safeGetVal(cursor.isNull(piDimW), cursor.getInt(piDimW))
                , safeGetVal(cursor.isNull(piDimD), cursor.getInt(piDimD))
                , safeGetVal(cursor.isNull(piAdditional), cursor.getString(piAdditional))
                , safeGetVal(cursor.isNull(piBarCode), cursor.getString(piBarCode))
                , safeGetVal(cursor.isNull(piAisle), cursor.getString(piAisle))
                , safeGetVal(cursor.isNull(piRack), cursor.getInt(piRack))
                , safeGetVal(cursor.isNull(piShelf), cursor.getInt(piShelf))
            );

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
