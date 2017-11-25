package com.kszych.pms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Part;

public class PartAddActivity extends AppCompatActivity {

    DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    private Part mCurrentPart;
    public static final String KEY_PART = "incoming_part";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_add);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentPart = extras.getParcelable(KEY_PART);
        } else {
            //TODO tink of it
            Toast.makeText(PartAddActivity.this, "error", Toast.LENGTH_SHORT).show();
        }

        final EditText etName = findViewById(R.id.etName);
        final EditText etBuyURL = findViewById(R.id.etBuyURL);
        final EditText etPrice = findViewById(R.id.etPrice);
        final EditText etProducer = findViewById(R.id.etProducer);
        final EditText etAdditionalInfo = findViewById(R.id.etAdditionalInfo);
        Button btAdd = findViewById(R.id.btnAdd);

        if(mCurrentPart != null) {
            setTitle(getString(R.string.modifyPartTitle, mCurrentPart.getName()));
        } else {
            setTitle(getString(R.string.modifyPartTitle, "nowa"));
        }

        if(mCurrentPart != null){
            etName.setText(mCurrentPart.getName() == DatabaseHelper.DEFAULT_STRING
                    ? DatabaseHelper.NULL_VAL
                    : mCurrentPart.getName());
            etBuyURL.setText(mCurrentPart.getBuyUrl() == DatabaseHelper.DEFAULT_STRING
                    ? DatabaseHelper.NULL_VAL
                    : mCurrentPart.getBuyUrl());
            etPrice.setText(mCurrentPart.getPrice() == DatabaseHelper.DEFAULT_REAL
                    ? DatabaseHelper.NULL_VAL
                    : Double.toString(mCurrentPart.getPrice()));
            etProducer.setText(mCurrentPart.getProducerName() == DatabaseHelper.DEFAULT_STRING
                    ? DatabaseHelper.NULL_VAL
                    : mCurrentPart.getProducerName());
            etAdditionalInfo.setText(mCurrentPart.getAdditionalInfo() == DatabaseHelper.DEFAULT_STRING
                    ? DatabaseHelper.NULL_VAL
                    : mCurrentPart.getAdditionalInfo());
        }

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString() == DatabaseHelper.DEFAULT_STRING
                        || etName.getText().toString() == DatabaseHelper.NULL_VAL){
                    Toast.makeText(PartAddActivity.this, "Wprowadź nazwę", Toast.LENGTH_SHORT).show();
                } else {
                    Part mNewPart = new Part(
                            mDb.SafeGetStringFromEditText(etName.getText().toString()),
                            mDb.SafeGetStringFromEditText(etBuyURL.getText().toString()),
                            mDb.SafeGetDoubleFromEditText(etPrice.getText().toString()),
                            mDb.SafeGetStringFromEditText(etProducer.getText().toString()),
                            mDb.SafeGetStringFromEditText(etAdditionalInfo.getText().toString())
                    );

                    boolean success = mDb.addPart(mNewPart);
                    if (success) {
                        Toast.makeText(PartAddActivity.this, "succes", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PartAddActivity.this, PartSingleActivity.class);
                        intent.putExtra(PackageSingleActivity.KEY_PACKAGE, mNewPart);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PartAddActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
