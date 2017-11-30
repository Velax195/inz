package com.kszych.pms.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "DatabaseX.db";

    // default db values below
    public static final String DEFAULT_STRING = null;
    public static final int DEFAULT_INT = -1;
    public static final double DEFAULT_REAL = -1.0;
    public static final String NULL_VAL = "N/A";
    public static final boolean DEFAULT_BOOLEAN = false;
    private static DatabaseHelper _instance = null;

    public static class TPackage {
        public final static String TNAME = "Package";

        final static String ID = "ID";
        final static String RFID_TAG = "rfidTag";
        final static String MASS = "mass";
        final static String DIM_H = "dimH";
        final static String DIM_W = "dimW";
        final static String DIM_D = "dimD";
        final static String ADDITIONAL_INFO = "additionalInfo";
        final static String BAR_CODE = "barCode";
        final static String AISLE = "aisle";
        final static String RACK = "rack";
        final static String SHELF = "shelf";
    }

    public static class TPart {
        public final static String TNAME = "Part";

        final static String ID = "ID";
        final static String NAME = "name";
        final static String BUY_URL = "buyUrl";
        final static String PRICE = "price";
        final static String PRODUCER_NAME = "producerName";
        final static String ADDITIONAL_INFO = "additionalInfo";
    }

    public static class TPackagePart {
        public final static String TNAME = "PackagePart";

        final static String ID = "ID";
        final static String PACKAGE_ID = "fk_package_id";
        final static String PART_ID = "fk_part_id";
        final static String QUANTITY = "quantity";
    }

    public static class TClient{
        public final static String TNAME = "Client";

        final static String ID = "ID";
        final static String NAME = "name";
        final static String STREET = "street";
        final static String NUMBER = "number";
        final static String CITY = "city";
        final static String PHONE = "phone";
        final static String ADDITIONAL_INFO = "additionalInfo";
    }

    public static class TOrder{
        public final static String TNAME = "OrderTable";

        final static String ID = "ID";
        final static String IS_EXECUTED = "isExecuted";
        final static String CLIENT_ID = "fk_client_id";
        final static String NUMBER = "number";
        final static String DATE = "date";
        final static String ADDITIONAL_INFO = "additionalInfo";
    }

    public static class TPartInOrder{
        public final static String TNAME = "PartInOrder";

        final static String ID = "ID";
        final static String ORDER_ID = "fk_order_id";
        final static String PART_ID = "fk_part_id";
        final static String QUANTITY = "quantity";
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (_instance == null) {
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
                + TPackagePart.QUANTITY + " INTEGER, "
                + "FOREIGN KEY (" + TPackagePart.PACKAGE_ID + ") "
                + "REFERENCES " + TPackage.TNAME + "(" + TPackage.ID + "), "
                + "FOREIGN KEY (" + TPackagePart.PART_ID + ") "
                + "REFERENCES " + TPart.TNAME + "(" + TPart.ID + ") )";
        sqLiteDatabase.execSQL(createPackagePart);

        String createClient = "CREATE TABLE " + TClient.TNAME + " ("
                + TClient.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TClient.NAME + "  TEXT NOT NULL, "
                + TClient.STREET + " TEXT, "
                + TClient.NUMBER + " INT, "
                + TClient.CITY + " TEXT, "
                + TClient.PHONE + " INT, "
                + TClient.ADDITIONAL_INFO + "TEXT)";
        sqLiteDatabase.execSQL(createClient);

        String createOrder = "CREATE TABLE " + TOrder.TNAME + " ("
                + TOrder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TOrder.IS_EXECUTED + " INTEGER, "
                + TOrder.CLIENT_ID + " INTEGER, "
                + TOrder.NUMBER + " INTEGER, "
                + TOrder.DATE + " TEXT, "
                + TOrder.ADDITIONAL_INFO + " TEXT, "
                + "FOREIGN KEY (" + TOrder.CLIENT_ID + ") "
                + "REFERENCES " + TClient.TNAME + "(" + TClient.ID + ") ) ";
        sqLiteDatabase.execSQL(createOrder);

        String createPartInOrder = "CREATE TABLE " + TPartInOrder.TNAME + " ("
                + TPartInOrder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TPartInOrder.ORDER_ID + " INTEGER, "
                + TPartInOrder.PART_ID + " INTEGER, "
                + TPartInOrder.QUANTITY + " INTEGER, "
                + "FOREIGN KEY (" + TPartInOrder.ORDER_ID + ") "
                + "REFERENCES " + TOrder.TNAME + "(" + TOrder.ID + "), "
                + "FOREIGN KEY (" + TPartInOrder.PART_ID + ") "
                + "REFERENCES " + TPart.TNAME + "(" + TPart.ID + ") )";
        sqLiteDatabase.execSQL(createPartInOrder);

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
        if (pack.getId() == DEFAULT_INT) {
            // TODO insert
        } else {
            // TODO update
        }
    }

    public void save(Part part) {
        if (part.getId() == DEFAULT_INT) {
            //TODO insert
        } else {
            //TODO update
        }
    }

    public void save(PackagePart packagePart) {
        if (packagePart.getId() == DEFAULT_INT) {
            //TODO insert
        } else {
            //TODO update
        }
    }

    private int safeGetVal(boolean isNull, int val) {
        if (isNull) {
            return DEFAULT_INT;
        }
        return val;
    }

    private String safeGetVal(boolean isNull, String val) {
        if (isNull) {
            return DEFAULT_STRING;
        }
        return val;
    }

    private double safeGetVal(boolean isNull, double val) {
        if (isNull) {
            return DEFAULT_REAL;
        }
        return val;
    }

    public int SafeGetIntFromEditText(String val) {
        if (val.equals(NULL_VAL) || val.matches("")) {
            return DEFAULT_INT;
        }
        return Integer.valueOf(val);
    }

    public String SafeGetStringFromEditText(String val) {
        if (val.equals(NULL_VAL) || val.matches("")) {
            return DEFAULT_STRING;
        }
        return val;
    }

    public double SafeGetDoubleFromEditText(String val) {
        if (val.equals(NULL_VAL) || val.matches("")) {
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


    public ArrayList<Part> getParts() {
        return getPartsByQuery("SELECT * FROM " + TPart.TNAME, null);
    }

    public ArrayList<Part> searchParts(Part filteredPart) {
        String querry = "SELECT * FROM " + TPart.TNAME + " WHERE ";
        String temp = null;
        temp = appendQuerryFromString(temp ,TPart.NAME, filteredPart.getName());
        querry += temp;
        temp = appendQuerryFromString(temp ,TPart.BUY_URL, filteredPart.getBuyUrl());
        querry += temp;
        temp = appendQuerryFromDouble(temp ,TPart.PRICE, filteredPart.getPrice());
        querry += temp;
        temp = appendQuerryFromString(temp ,TPart.PRODUCER_NAME, filteredPart.getProducerName());
        querry += temp;
        temp = appendQuerryFromString(temp ,TPart.ADDITIONAL_INFO, filteredPart.getAdditionalInfo());
        querry += temp;

        return getPartsByQuery(querry, null);
    }

    private String appendQuerryFromString(String previous ,String name, String value){
        String string = "";
        if (value == DEFAULT_STRING || value.matches("")){
            return string;
        } else {
            if(previous != null)
            {
                string += " OR ";
            }
            string += name + " LIKE " + "'%" + value + "%' ";
        }
        return string;
    }

    private String appendQuerryFromDouble(String previous ,String name, Double value){
        String string = "";
        if (value == DEFAULT_REAL){
            return string;
        } else {
            if(previous == null)
            {
                string += " OR ";
            }
            string += name + " LIKE " + Double.toString(value) + " ";
        }
        return string;
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

    public Package getPackageByRFID(String scannedRFID) {

        String queryString = "SELECT * FROM " + TPackage.TNAME
                + " WHERE " + TPackage.RFID_TAG + " = ?";
        ArrayList<Package> foundPackages = getPackagesByQuery(queryString, new String[]{scannedRFID});

        return foundPackages.size() == 0 ? null : foundPackages.get(0);
    }

    public Part getPartByName(String name) {

        String queryString = "SELECT * FROM " + TPart.TNAME
                + " WHERE " + TPart.NAME + " = ?";
        ArrayList<Part> foundParts = getPartsByQuery(queryString, new String[]{name});

        return foundParts.size() == 0 ? null : foundParts.get(0);
    }

    public Part getPartById(int Id) {

        String queryString = "SELECT * FROM " + TPart.TNAME
                + " WHERE " + TPart.ID + " = ?";
        ArrayList<Part> foundParts = getPartsByQuery(queryString, new String[]{Integer.toString(Id)});

        return foundParts.size() == 0 ? null : foundParts.get(0);
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

        return getPackagesByQuery(queryString, new String[]{Integer.toString(part.getId())});
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

        return getPartsByQuery(queryString, new String[]{Integer.toString(singlePackage.getId())});
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

        for (int i = 0; i < 20; i++) {

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
        for (int i = 0; i < packagesID.size(); i++) {
            int partsCount = new Random().nextInt(partsID.size() / 2);
            for (int j = 0; j < partsCount; j++) {
                ContentValues cv = new ContentValues();
                cv.put(TPackagePart.PACKAGE_ID, packagesID.get(i));
                cv.put(TPackagePart.PART_ID, partsID.get(j));
                cv.put(TPackagePart.QUANTITY, 1);
                db.insert(TPackagePart.TNAME, null, cv);
            }
        }

    }

    public boolean addPackage(Package newPackage) {

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

        if (db.insert(TPackage.TNAME, null, vals) == -1) {
            return false;
        }
        return true;
    }

    public boolean addPart(Part newPart) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();

        vals.put(TPart.NAME, newPart.getName());
        vals.put(TPart.BUY_URL, newPart.getBuyUrl());
        vals.put(TPart.PRICE, newPart.getPrice());
        vals.put(TPart.PRODUCER_NAME, newPart.getProducerName());
        vals.put(TPart.ADDITIONAL_INFO, newPart.getAdditionalInfo());

        if (db.insert(TPart.TNAME, null, vals) == -1) {
            return false;
        }
        return true;
    }

    public boolean deletePackage(String tagRFID) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete(TPackage.TNAME, TPackage.RFID_TAG + " = ?", new String[]{tagRFID}) == 1) {
            return true;
        }
        return false;
    }

    public boolean deletePart(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete(TPart.TNAME, TPart.NAME + " = ?", new String[]{name}) == 1) {
            return true;
        }
        return false;
    }

    public boolean updatePackage(Package updatedPackage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();

        vals.put(TPackage.RFID_TAG, updatedPackage.getRfidTag());
        vals.put(TPackage.MASS, updatedPackage.getMass());
        vals.put(TPackage.DIM_H, updatedPackage.getDimH());
        vals.put(TPackage.DIM_W, updatedPackage.getDimW());
        vals.put(TPackage.DIM_D, updatedPackage.getDimD());
        vals.put(TPackage.ADDITIONAL_INFO, updatedPackage.getAdditionalText());
        vals.put(TPackage.BAR_CODE, updatedPackage.getBarcode());
        vals.put(TPackage.AISLE, updatedPackage.getAisle());
        vals.put(TPackage.RACK, updatedPackage.getRack());
        vals.put(TPackage.SHELF, updatedPackage.getShelf());

        if (db.update(TPackage.TNAME, vals, TPackage.ID + " = ?",
                new String[]{Integer.toString(updatedPackage.getId())}) == 0) {
            return false;
        }
        return true;
    }

    public boolean updatePart(Part updatedPart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();

        vals.put(TPart.NAME, updatedPart.getName());
        vals.put(TPart.BUY_URL, updatedPart.getBuyUrl());
        vals.put(TPart.PRICE, updatedPart.getPrice());
        vals.put(TPart.PRODUCER_NAME, updatedPart.getProducerName());
        vals.put(TPart.ADDITIONAL_INFO, updatedPart.getAdditionalInfo());

        if (db.update(TPart.TNAME, vals, TPart.ID + " = ?",
                new String[]{Integer.toString(updatedPart.getId())}) == 0) {
            return false;
        }
        return true;
    }

    public void deletePackageParts(@NonNull Package fromPackage, @Nullable List<Part> parts) {
        if(parts != null && parts.size() > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            for (Part singlePart : parts) {
                builder.append(singlePart.getId()).append(",");
            }
            builder.setLength(builder.length() - 1);

            db.delete(TPackagePart.TNAME
                    , TPackagePart.PACKAGE_ID + "=? " +
                            "AND " + TPackagePart.PART_ID + " IN (" + builder.toString() + ")"
                    , new String[]{ String.valueOf(fromPackage.getId()) } );
        }
        // else do nothing
    }

    public void addPackageParts(@NonNull Package fromPackage, @Nullable List<Part> parts) {
        if(parts != null && parts.size() > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            for(Part singlePart : parts) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TPackagePart.PACKAGE_ID, fromPackage.getId());
                contentValues.put(TPackagePart.PART_ID, singlePart.getId());
                contentValues.put(TPackagePart.QUANTITY, 1);
                db.insert(TPackagePart.TNAME, null, contentValues);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public int countParts(int partId){
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT "
                + TPackagePart.QUANTITY
                + " FROM " + TPackagePart.TNAME
                + " WHERE " + TPackagePart.PART_ID + " = ? ";

        Cursor cursor = db.rawQuery(queryString, new String[]{Integer.toString(partId)});
        int piQuantity = cursor.getColumnIndex(TPackagePart.QUANTITY);
        while(cursor.moveToNext()){
            count += cursor.getInt(piQuantity);
        }
        return count;
    }
}