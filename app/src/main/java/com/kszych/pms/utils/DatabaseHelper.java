package com.kszych.pms.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper{


public static final int DATABASE_VERSION = 13;
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
                + "FOREIGN KEY (" + TPackagePart.PACKAGE_ID + ") "
                + "REFERENCES " + TPackage.TNAME + "(" + TPackage.ID + "), "
                + "FOREIGN KEY (" + TPackagePart.PART_ID + ") "
                + "REFERENCES " + TPart.TNAME + "(" + TPart.ID + ") )";
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

    public int SafeGetIntFromEditText(String val) {
        if(val.equals(NULL_VAL) || val == null){
            return DEFAULT_INT;
        }
        return Integer.valueOf(val);
    }

    public String SafeGetStringFromEditText(String val) {
        if(val.equals(NULL_VAL) || val == null){
            return DEFAULT_STRING;
        }
        return val;
    }
    public double SafeGetDoubleFromEditText(String val) {
        if(val.equals(NULL_VAL) || val == null){
            return DEFAULT_REAL;
        }
        return Double.valueOf(val);
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

        int piId = cursor.getColumnIndex(TPackage.ID);

        int piRFID = cursor.getColumnIndex(TPackage.RFID_TAG);
        int piMass = cursor.getColumnIndex(TPackage.MASS);
        int piDimH = cursor.getColumnIndex(TPackage.DIM_H);
        int piDimW = cursor.getColumnIndex(TPackage.DIM_W);
        int piDimD = cursor.getColumnIndex(TPackage.DIM_D);
        int piAdditional = cursor.getColumnIndex(TPackage.ADDITIONAL_INFO);
        int piBarCode = cursor.getColumnIndex(TPackage.BAR_CODE);
        int piAisle = cursor.getColumnIndex(TPackage.AISLE);
        int piRack = cursor.getColumnIndex(TPackage.RACK);
        int piShelf = cursor.getColumnIndex(TPackage.SHELF);

        while (cursor.moveToNext()){

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
                + TPackage.TNAME + "." + TPackage.ID + ", "
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
                + " LEFT JOIN " + TPackage.TNAME + " ON "
                + TPackagePart.PACKAGE_ID + " = " + TPackage.TNAME + "." + TPackage.ID
                + " WHERE " + TPackagePart.PART_ID + " = ?";

        return getPackagesByQuery(queryString, new String[]{ Integer.toString( part.getId() ) });
    }


    public ArrayList<Part> getPartsInPackage(@NonNull Package singlePackage) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT "
                + TPart.TNAME + "." + TPart.ID + ", "
                + TPart.NAME + ", "
                + TPart.BUY_URL + ", "
                + TPart.PRICE + ", "
                + TPart.PRODUCER_NAME + ", "
                + TPart.ADDITIONAL_INFO
                + " FROM " + TPackagePart.TNAME
                + " LEFT JOIN " + TPart.TNAME + " ON "
                + TPackagePart.PART_ID + " = " + TPart.TNAME + "." + TPart.ID
                + " WHERE " + TPackagePart.PACKAGE_ID + " = ?";

        return getPartsByQuery(queryString, new String[]{ Integer.toString( singlePackage.getId() ) });
    }

    private ArrayList<Part> getPartsByQuery(String query, String[] queryArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, queryArgs);
        ArrayList<Part> resultArrayList = new ArrayList<>();

        int piId = cursor.getColumnIndex(TPart.ID);

        int piName = cursor.getColumnIndex(TPart.NAME);
        int piBuyUrl = cursor.getColumnIndex(TPart.BUY_URL);
        int piPrice = cursor.getColumnIndex(TPart.PRICE);
        int piProducerName = cursor.getColumnIndex(TPart.PRODUCER_NAME);
        int piAdditionalInfo = cursor.getColumnIndex(TPart.ADDITIONAL_INFO);

        while (cursor.moveToNext()) {
            resultArrayList.add(new Part(
                            cursor.getInt(piId)
                            , cursor.getString(piName)
                            , safeGetVal(cursor.isNull(piBuyUrl), cursor.getString(piBuyUrl))
                            , safeGetVal(cursor.isNull(piPrice), cursor.getDouble(piPrice))
                            , safeGetVal(cursor.isNull(piProducerName), cursor.getString(piProducerName))
                            , safeGetVal(cursor.isNull(piAdditionalInfo), cursor.getString(piAdditionalInfo))
                    )
            );
        }

        cursor.close();

        return resultArrayList;
    }

    public ArrayList<Part> getParts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TPart.TNAME, null);

        int piId = cursor.getColumnIndex(TPart.ID);

        int piName = cursor.getColumnIndex(TPart.NAME);
        int piBuyUrl = cursor.getColumnIndex(TPart.BUY_URL);
        int piPrice = cursor.getColumnIndex(TPart.PRICE);
        int piProducerName = cursor.getColumnIndex(TPart.PRODUCER_NAME);
        int piAdditionalInfo = cursor.getColumnIndex(TPart.ADDITIONAL_INFO);

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

    private void testDeleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TPackagePart.TNAME, null, null);
        db.delete(TPackage.TNAME, null, null);
        db.delete(TPart.TNAME, null, null);
    }

    public void createTestDatabase() {
        testDeleteAll();
        Random r = new Random();

        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Long> packagesID = new ArrayList<>();
        ArrayList<Long> partsID = new ArrayList<>();

        for(int i=0; i<20; i++) {

            ContentValues packageVals = new ContentValues();

            packageVals.put(TPackage.RFID_TAG, r.nextInt(10000));
            packageVals.put(TPackage.MASS, r.nextInt(1000));
            packageVals.put(TPackage.DIM_D, r.nextInt(100));
            packageVals.put(TPackage.DIM_H, r.nextInt(80));
            packageVals.put(TPackage.DIM_W, r.nextInt(50));
            packageVals.put(TPackage.ADDITIONAL_INFO, "Additional info nr " + r.nextInt(100));
            packageVals.put(TPackage.BAR_CODE, r.nextInt(20000));
            packageVals.put(TPackage.AISLE, "Aisle nr " + r.nextInt(5));
            packageVals.put(TPackage.RACK, r.nextInt(10));
            packageVals.put(TPackage.SHELF, r.nextInt(10));

            packagesID.add(db.insert(TPackage.TNAME, null, packageVals));

            ContentValues partVals = new ContentValues();

            partVals.put(TPart.NAME, "Part nr " + r.nextInt(500));
            partVals.put(TPart.BUY_URL, "Buy URL nr " + r.nextInt(1000));
            partVals.put(TPart.PRICE, 1000 * r.nextDouble());
            partVals.put(TPart.PRODUCER_NAME, "Producer nr " + r.nextInt(80));

            partsID.add(db.insert(TPart.TNAME, null, partVals));
        }
        for(int i = 0; i < packagesID.size(); i++){
            int partsCount = new Random().nextInt(partsID.size() / 2);
            for(int j = 0; j < partsCount; j++) {
                ContentValues cv = new ContentValues();
                cv.put(TPackagePart.PACKAGE_ID, packagesID.get(i));
                cv.put(TPackagePart.PART_ID, partsID.get(j));
                db.insert(TPackagePart.TNAME, null, cv);
            }
        }

    }

    public void addPackage(String tag_RFID, int mass, int dim_H,
                            int dim_W, int dim_D, int barCode, String aisle, int rack, int shelf, String additionalInfo){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues packageVals = new ContentValues();

        packageVals.put(TPackage.RFID_TAG, tag_RFID);
        packageVals.put(TPackage.MASS, mass);
        packageVals.put(TPackage.DIM_H, dim_H);
        packageVals.put(TPackage.DIM_W, dim_W);
        packageVals.put(TPackage.DIM_D, dim_D);
        packageVals.put(TPackage.ADDITIONAL_INFO, additionalInfo);
        packageVals.put(TPackage.BAR_CODE, barCode);
        packageVals.put(TPackage.AISLE, aisle);
        packageVals.put(TPackage.RACK, rack);
        packageVals.put(TPackage.SHELF, shelf);

        db.insert(TPackage.TNAME, null, packageVals);
    }
    public boolean addPackage(Package newPackage){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();

        vals.put(TPackage.RFID_TAG, newPackage.getRfidTag());
        vals.put(TPackage.MASS, newPackage.getMass());
        vals.put(TPackage.DIM_H, newPackage.getDimH());
        vals.put(TPackage.DIM_W, newPackage.getDimW());
        vals.put(TPackage.DIM_D, newPackage.getDimD());
        vals.put(TPackage.ADDITIONAL_INFO, newPackage.getAdditionalText());
        vals.put(TPackage.BAR_CODE, newPackage.getBarcode());
        vals.put(TPackage.AISLE, newPackage.getAisle());
        vals.put(TPackage.RACK, newPackage.getRack());
        vals.put(TPackage.SHELF, newPackage.getShelf());

        if(db.insert(TPackage.TNAME, null, vals) == -1)
        {
            return false;
        }
        return true;
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