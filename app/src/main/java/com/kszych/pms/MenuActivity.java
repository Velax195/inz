package com.kszych.pms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = MenuActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnManageParts = findViewById(R.id.btnManageParts);
        Button btnManagePackages = findViewById(R.id.btnManagePackages);
        final Button btnScanRFID = findViewById(R.id.btnScanRFID);
        Button btnTestActivity = findViewById(R.id.btnTestActivity);

        btnManageParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, PartListActivity.class));
            }
        });

        btnManagePackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, PackageListActivity.class));
            }
        });

        btnScanRFID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MenuActivity.this, ScanRFIDActivity.class);
                mIntent.putExtra(ScanRFIDActivity.FROM_ACTIVITY, ACTIVITY_NAME);
                startActivity(mIntent);
            }
        });

        btnTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, TestActivity.class));
            }
        });
    }
}
