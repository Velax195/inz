package com.kszych.pms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Part;

public class PartSearchActivity extends AppCompatActivity {

    private DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    public static final String SEARCH_PACKAGE = "search_package";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_search);

        final EditText etName = findViewById(R.id.etName);
        final EditText etBuyURL = findViewById(R.id.etBuyURL);
        final EditText etPrice = findViewById(R.id.etPrice);
        final EditText etProducer = findViewById(R.id.etProducer);
        final EditText etAdditionalInfo = findViewById(R.id.etAdditionalInfo);
        Button btnSearch = findViewById(R.id.btnSearch);

//        if(mCurrentPart != null){
//            etName.setText(mCurrentPart.getName() == DatabaseHelper.DEFAULT_STRING
//                    ? DatabaseHelper.NULL_VAL
//                    : mCurrentPart.getName());
//            etBuyURL.setText(mCurrentPart.getBuyUrl() == DatabaseHelper.DEFAULT_STRING
//                    ? DatabaseHelper.NULL_VAL
//                    : mCurrentPart.getBuyUrl());
//            etPrice.setText(mCurrentPart.getPrice() == DatabaseHelper.DEFAULT_REAL
//                    ? DatabaseHelper.NULL_VAL
//                    : Double.toString(mCurrentPart.getPrice()));
//            etProducer.setText(mCurrentPart.getProducerName() == DatabaseHelper.DEFAULT_STRING
//                    ? DatabaseHelper.NULL_VAL
//                    : mCurrentPart.getProducerName());
//            etAdditionalInfo.setText(mCurrentPart.getAdditionalInfo() == DatabaseHelper.DEFAULT_STRING
//                    ? DatabaseHelper.NULL_VAL
//                    : mCurrentPart.getAdditionalInfo());
//        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();

                Part mNewPart = new Part(
                        mDb.SafeGetStringFromEditText(etName.getText().toString()),
                        mDb.SafeGetStringFromEditText(etBuyURL.getText().toString()),
                        mDb.SafeGetDoubleFromEditText(etPrice.getText().toString()),
                        mDb.SafeGetStringFromEditText(etProducer.getText().toString()),
                        mDb.SafeGetStringFromEditText(etAdditionalInfo.getText().toString())
                );
                returnIntent.putExtra(SEARCH_PACKAGE, mNewPart);
                setResult(PartSearchActivity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
