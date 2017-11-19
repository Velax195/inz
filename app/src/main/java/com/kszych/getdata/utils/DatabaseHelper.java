package com.kszych.getdata.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Database.db";

    // default db values below
    public static final String DEFAULT_STRING = null;
    public static final int DEFAULT_INT = -1;
    public static final double DEFAULT_REAL = -1.0;
    public static final String NULL_VAL = "N/A";
    private static DatabaseHelper _instance = null;

    public static class TPackage {
        public final static String TNAME = "Package";

        final static String ID                  = TPackage.TNAME + ".ID";
        final static String RFID_TAG            = TPackage.TNAME + ".rfidTag";
        final static String MASS                = TPackage.TNAME + ".mass";
        final static String DIM_H               = TPackage.TNAME + ".dimH";
        final static String DIM_W               = TPackage.TNAME + ".dimW";
        final static String DIM_D               = TPackage.TNAME + ".dimD";
        final static String ADDITIONAL_INFO     = TPackage.TNAME + ".additionalInfo";
        final static String BAR_CODE            = TPackage.TNAME + ".barCode";
        final static String AISLE               = TPackage.TNAME + ".aisle";
        final static String RACK                = TPackage.TNAME + ".rack";
        final static String SHELF               = TPackage.TNAME + ".shelf";
    }

    public static class TPart {
        public final static String TNAME = "Part";

        final static String ID                  = TPart.TNAME + ".ID";
        final static String NAME                = TPart.TNAME + ".name";
        final static String BUY_URL             = TPart.TNAME + ".buyUrl";
        final static String PRICE               = TPart.TNAME + ".price";
        final static String PRODUCER_NAME       = TPart.TNAME + ".producerName";
        final static String ADDITIONAL_INFO     = TPart.TNAME + ".additionalInfo";
    }

    public static class TPackagePart {
        public final static String TNAME = "PackagePart";

        final static String ID                  = TPackagePart.TNAME + ".ID";
        final static String PACKAGE_ID          = TPackagePart.TNAME + ".fk_package_id";
        final static String PART_ID             = TPackagePart.TNAME + ".fk_part_id";
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
        return getPackagesByQuery("SELECT * FROM " + TPackage.TNAME, null);
    }

    private ArrayList<Package> getPackagesByQuery(String query, String[] queryArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, queryArgs);
        ArrayList<Package> resultArrayList = new ArrayList<>();

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

        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            resultArrayList.add(new Package(
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
                    )
            );
        }

        cursor.close();

        return resultArrayList;
    }

    public Package getPackageByRFID(String scannedRFID){
        //TODO get uid, find package in database, return it

        String queryString = "SELECT * FROM " + TPackage.TNAME
                + " WHERE " + TPackage.RFID_TAG + " = ?";
        ArrayList<Package> foundPackages = getPackagesByQuery(queryString, new String[]{scannedRFID});

        return foundPackages.size() == 0 ? null : foundPackages.get(0);
    }

    public ArrayList<Package> getPackagesContainingPart(@NonNull Part part) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT "
                + TPackage.ID + ", "
                + TPackage.RFID_TAG + ", "
                + TPackage.MASS + ", "
                + TPackage.DIM_H + ", "
                + TPackage.DIM_W + ", "
                + TPackage.DIM_D + ", "
                + TPackage.ADDITIONAL_INFO + ", "
                + TPackage.BAR_CODE + ", "
                + TPackage.AISLE + ", "
                + TPackage.RACK + ", "
                + TPackage.SHELF
                + " FROM " + TPackagePart.TNAME
                + " LEFT JOIN " + TPackage.TNAME + " ON " + TPackagePart.PACKAGE_ID + " = " + TPackage.ID
                + " WHERE " + TPackagePart.PART_ID + " = ?";

        return getPackagesByQuery(queryString, new String[]{ Integer.toString( part.getId() ) });
    }

    public ArrayList<Part> getParts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TPart.TNAME, null);

        int piId = cursor.getColumnIndexOrThrow(TPart.ID);

        int piName = cursor.getColumnIndexOrThrow(TPart.NAME);
        int piBuyUrl = cursor.getColumnIndexOrThrow(TPart.BUY_URL);
        int piPrice = cursor.getColumnIndexOrThrow(TPart.PRICE);
        int piProducerName = cursor.getColumnIndexOrThrow(TPart.PRODUCER_NAME);
        int piAdditionalInfo = cursor.getColumnIndexOrThrow(TPart.ADDITIONAL_INFO);

        ArrayList<Part> partList = new ArrayList<>();
        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            Part newPart = new Part(
                    cursor.getInt(piId)
                    , cursor.getString(piName)
                    , cursor.getString(piBuyUrl)
                    , safeGetVal(cursor.isNull(piPrice), cursor.getDouble(piPrice))
                    , cursor.getString(piProducerName)
                    , cursor.getString(piAdditionalInfo)
            );

            partList.add(newPart);
        }

        cursor.close();

        return partList;
    }

    public long addTestPackage(String dummyRfidTag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(TPackage.RFID_TAG, dummyRfidTag);

        return db.insert(TPackage.TNAME, null, vals);
    }

    public long addTestParts(String dummyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(TPart.NAME, dummyName);

        return db.insert(TPart.TNAME, null, vals);
    }

    public Package checkRFID(String Uid){
        //TODO implement
        return new Package(-1, null);
    }

    public boolean isInDatabase(String Uid){
        //TODO implement
        return true;
    }
}
