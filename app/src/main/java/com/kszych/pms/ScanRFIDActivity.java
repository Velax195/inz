package com.kszych.pms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;

public class ScanRFIDActivity extends AppCompatActivity {


    public static final String DEFAULT_RESPOND_MESSAGE = "CARD_NOT_PRESENT";
    public static final String NOT_READABLE_RESPOND_MESSAGE = "CARD_NOT_READABLE";
    private static final String REQUEST_TAG = "ScannerRequest";
    public static final String FROM_ACTIVITY = "FromActivity";

    String mRequestURL = "http://192.168.0.14/scaner";
    Package mPackage;
    DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_rfid);

        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mRequestQueue != null) {
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }

    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, mRequestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(isDefaultMessage(response)) {
                    sendRequest();
                }
                else {
                    String[] uids = response.split("\n");
                    cardIsRead(uids[0], uids[1]);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO ignore, resend?
                sendRequest();
            }
        });
        request.setShouldCache(false).setTag(REQUEST_TAG);
        mRequestQueue.add(request);
    }

    public boolean isDefaultMessage(String response) {
        if (response.equals(ScanRFIDActivity.DEFAULT_RESPOND_MESSAGE)) {
            // loop TODO delete this if
        } else if (response.equals(ScanRFIDActivity.NOT_READABLE_RESPOND_MESSAGE)) {
            // TODO toast here - card not compatible
        } else {
            // stop
            return false;
        }
        return true;
    }

    private void cardIsRead(final String uidHex, String uidDec) {
        Toast.makeText(ScanRFIDActivity.this, "Read " + uidHex + " " + uidDec, Toast.LENGTH_SHORT).show();
        //onBackPressed();
        mPackage = mDb.getPackageByRFID(uidHex);
        String previousActivity = getIntent().getStringExtra(FROM_ACTIVITY);
        if (previousActivity.equals(getResources().getString(R.string.menuActivityName))) {
            if(mPackage == null) {
                // TODO toast and back
                Toast.makeText(ScanRFIDActivity.this, "Not in database", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            else {
                Intent sendIntent = new Intent(ScanRFIDActivity.this, PackageSingleActivity.class);
                sendIntent.putExtra(PackageSingleActivity.KEY_PACKAGE, mPackage);
                startActivity(sendIntent);
            }
        }
        else if (previousActivity.equals(getResources().getString(R.string.packageListActivityName))) {
            if(mPackage == null) {
                Intent sendIntent = new Intent(ScanRFIDActivity.this, ModifyPackageActivity.class);
                sendIntent.putExtra(ModifyPackageActivity.KEY_PACKAGE, mPackage);
                sendIntent.putExtra(ModifyPackageActivity.KEY_RFID_TAG, uidHex);
                sendIntent.putExtra(ModifyPackageActivity.KEY_ACTIVITY, previousActivity);
                startActivity(sendIntent);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanRFIDActivity.this);
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.overwritePackage)
                        .setPositiveButton(R.string.overwrite, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDb.deletePackage(uidHex);
                                Intent sIntent = new Intent(ScanRFIDActivity.this, ModifyPackageActivity.class);
                                sIntent.putExtra(ModifyPackageActivity.KEY_ACTIVITY, "ScanRFID");
                                startActivity(sIntent);
                            }
                        })
                        .setNegativeButton(R.string.showInfo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent sIntent = new Intent(ScanRFIDActivity.this, PackageSingleActivity.class);
                                sIntent.putExtra(ModifyPackageActivity.KEY_PACKAGE, mDb.getPackageByRFID(uidHex));
                                sIntent.putExtra(ModifyPackageActivity.KEY_ACTIVITY, "ScanRFID");
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