package com.kszych.getdata.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kszych.getdata.ModifyPackageActivity;
import com.kszych.getdata.PackageSingleActivity;
import com.kszych.getdata.R;

public class ScanRFIDActivity extends AppCompatActivity implements GetRFIDTaskCompleteListener {


    protected static final String DEFAULT_RESPOND_MESSAGE = "CARD_NOT_PRESENT";
    protected static final String NOT_READABLE_RESPOND_MESSAGE = "CARD_NOT_READABLE";

    String ip = "http://192.168.0.14/scaner";
    Package mPackage;
    DatabaseHelper mDb = DatabaseHelper.getInstance(this);

    private GetRFIDTask mGetRFIDTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_rfid);

        //TODO different actions from different activities
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);


//        mPackage = mDb.getPackageFromID(UidDec);
//        if (previousActivity == getResources().getString(R.string.menuActivityName)) {
//            Intent sIntent = new Intent(ScanRFIDActivity.this, PackageSingleActivity.class);
//            sIntent.putExtra("SCANNED_PACKAGE", mPackage);
//            startActivity(sIntent);
//        } else if (previousActivity == getResources().getString(R.string.packageListActivityName)) {
//            if (mDb.isInDatabase(UidDec) == true) {
//                //TODO dialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(ScanRFIDActivity.this);
//                builder.setTitle(R.string.warning)
//                        .setMessage(R.string.overwritePackage)
//                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //TODO delete package, go to empty ModifyPackageActivity
//                                Toast.makeText(ScanRFIDActivity.this
//                                        , "DELETED! Just kidding", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton(R.string.showInfo, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent sIntent = new Intent(ScanRFIDActivity.this, PackageSingleActivity.class);
//                                sIntent.putExtra("SCANNED_PACKAGE", mPackage);
//                                startActivity(sIntent);
//                            }
//                        })
//                        .show();
//            } else {
//                Intent sIntent = new Intent(ScanRFIDActivity.this, ModifyPackageActivity.class);
//                sIntent.putExtra("SCANNED_PACKAGE", mPackage);
//                startActivity(sIntent);
//            }
//        }
//        doneFlag = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGetRFIDTask = new GetRFIDTask(ip, ScanRFIDActivity.this);
        mGetRFIDTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGetRFIDTask.cancel(true);
    }

    @Override
    public void cardIsRead(String uidHex, String uidDec) {
        Toast.makeText(ScanRFIDActivity.this, "Read " + uidHex + " " + uidDec, Toast.LENGTH_SHORT).show();
        onBackPressed();
        mPackage = mDb.getPackageByRFID(uidDec);
        String previousActivity = getIntent().getStringExtra("FROM_ACTIVITY");
        if (previousActivity.equals(getResources().getString(R.string.menuActivityName))) {
            if(mPackage == null) {
                // TODO toast and back
                Toast.makeText(ScanRFIDActivity.this, "Not in database", Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder newDialog = new AlertDialog.Builder(ScanRFIDActivity.this);
//                newDialog.setMessage("No package is associated with this RFID tag")
//                        .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ScanRFIDActivity.this.onBackPressed();
//                            }
//                        })
//                        .show();
            }
            else {
                Intent sendIntent = new Intent(ScanRFIDActivity.this, PackageSingleActivity.class);
                sendIntent.putExtra("SCANNED_PACKAGE", mPackage);
                startActivity(sendIntent);
            }
        }
        else if (previousActivity.equals(getResources().getString(R.string.packageListActivityName))) {
            if(mPackage == null) {
                Intent sendIntent = new Intent(ScanRFIDActivity.this, ModifyPackageActivity.class);
                sendIntent.putExtra("SCANNED_PACKAGE", mPackage);
                startActivity(sendIntent);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanRFIDActivity.this);
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.overwritePackage)
                        .setPositiveButton(R.string.overwrite, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO delete package, go to empty ModifyPackageActivity
                                Toast.makeText(ScanRFIDActivity.this
                                        , "DELETED! Just kidding", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.showInfo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent sIntent = new Intent(ScanRFIDActivity.this, PackageSingleActivity.class);
                                sIntent.putExtra("SCANNED_PACKAGE", mPackage);
                                startActivity(sIntent);
                            }
                        })
                        .show();
            }
        }
        else {
            // TODO handle case - unknown previous activity
        }
    }
}