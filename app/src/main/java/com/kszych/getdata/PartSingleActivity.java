package com.kszych.getdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kszych.getdata.utils.DatabaseHelper;
import com.kszych.getdata.utils.Package;
import com.kszych.getdata.utils.Part;

import java.util.ArrayList;

public class PartSingleActivity extends AppCompatActivity {

    public static final String KEY_PART = "single_part";

    private Part mPart;
    private ArrayList<Package> mPackagesOfPart;
    private DatabaseHelper mDb = DatabaseHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_single);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mPart = extras.getParcelable(KEY_PART);

            if(mPart == null) {
                // TODO die panic etc
            }
            mPackagesOfPart = mDb.getPackagesContainingPart(mPart);
        }
        else {
            // TODO die etc. We require extras
            Toast.makeText(this, "PART IS NULLL WHAAAT TO DOOOOO", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}
