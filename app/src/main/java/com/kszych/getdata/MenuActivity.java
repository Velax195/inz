package com.kszych.getdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kszych.getdata.utils.scanRFID;

public class MenuActivity extends AppCompatActivity {

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
                // TODO implement
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
                // TODO implement
                Intent mIntent = new Intent(MenuActivity.this, scanRFID.class);
                mIntent.putExtra("FROM_ACTIVITY", R.string.menuActivityName);
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

    private void showNotImplemented() {
        Toast.makeText(MenuActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }
}
