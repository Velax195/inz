package com.kszych.getdata.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.kszych.getdata.PackageSingleActivity;
import com.kszych.getdata.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class scanRFID extends AppCompatActivity {

    String ip = "http://192.168.0.14/timer";
    String UidHex = null;
    String UidDec = null;
    boolean doneFlag = false;

    //TODO check line under
    DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_rfid);

        //TODO different actions from different activities

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");

        while(doneFlag == false) {
            execute();
        }
        if(previousActivity == getResources().getString(R.string.menuActivityName)) {
            Intent sIntent = new Intent(scanRFID.this, PackageSingleActivity.class);
            sIntent.putExtra("SCANNED_PACKAGE_RFID", UidDec);
            startActivity(sIntent);
        } else if (previousActivity == getResources().getString(R.string.packageListActivityName)) {
            if(mDb.isInDatabase(UidDec)==true) {
                //TODO dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(scanRFID.this);
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.overwritePackage)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO delete package
                                Toast.makeText(scanRFID.this
                                        , "DELETED! Just kidding", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.showInfo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent sIntent = new Intent(scanRFID.this, ModifyPackageActivity.class);
                                sIntent.putExtra("SCANNED_PACKAGE_RFID", UidDec);
                                startActivity(sIntent);
                            }
                        })
                        .show();
            } else {
                Intent sIntent = new Intent(scanRFID.this, ModifyPackageActivity.class);
                sIntent.putExtra("SCANNED_PACKAGE_RFID", UidDec);
                startActivity(sIntent);
            }
        }
        doneFlag = false;
    }

    void execute() {
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ip);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            //StringBuilder stringBuffer = new StringBuilder();
            String newLine;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            newLine = bufferedReader.readLine();
            if(newLine == "STOP ASKING") {
            doneFlag = true;
            inputStream.close();
            return;
            } else {
                UidDec = newLine;
                newLine = bufferedReader.readLine();
                UidHex = newLine;
            }
            inputStream.close();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
