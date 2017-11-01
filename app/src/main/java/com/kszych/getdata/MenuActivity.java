package com.kszych.getdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnManageParts = findViewById(R.id.btnManageParts);
        Button btnManagePackages = findViewById(R.id.btnManagePackages);
        Button btnScanRFID = findViewById(R.id.btnScanRFID);
        Button btnTestActivity = findViewById(R.id.btnTestActivity);

        btnTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, TestActivity.class));
            }
        });
    }
}
