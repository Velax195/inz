package com.kszych.pms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;

public class ModifyPackageActivity extends AppCompatActivity {

    public static final String KEY_PACKAGE = "incoming_package";

    private Package mCurrentPackage;

    DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    String scannedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_package);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentPackage = extras.getParcelable(KEY_PACKAGE);
            scannedID = extras.getString("SCANNED_PACKAGE");
        } else {
            // TODO fail die etc.
            Toast.makeText(ModifyPackageActivity.this, "error", Toast.LENGTH_SHORT).show();
        }


        final EditText etMass = findViewById(R.id.etMass);
        final EditText etHeight = findViewById(R.id.etHeight);
        final EditText etWidth = findViewById(R.id.etWidth);
        final EditText etDepth = findViewById(R.id.etDepth);
        final EditText etAisle = findViewById(R.id.etAisle);
        final EditText etRack = findViewById(R.id.etRack);
        final EditText etShelf = findViewById(R.id.etShelf);
        final EditText etBarcode = findViewById(R.id.etBarcode);
        final EditText etAdditionalInfo = findViewById(R.id.etAdditionalInfo);
        ListView lvPackageParts = findViewById(R.id.modifyPackagePartsList);
        Button btnAdd = findViewById(R.id.modifyPackageAdd);
        Button btnDelete = findViewById(R.id.modifyPackageDelete);
        Button btnSave = findViewById(R.id.modifyPackageSave);

        if (mCurrentPackage != null) {
            setTitle(getString(R.string.modifyPackageTitle, Integer.toString(mCurrentPackage.getId())));
        } else {
            setTitle(getString(R.string.modifyPackageTitle, "nowa"));
            mCurrentPackage = new Package(DatabaseHelper.DEFAULT_INT, DatabaseHelper.DEFAULT_STRING);
        }

        etMass.setText(mCurrentPackage.getMass() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getMass()));
        etHeight.setText(mCurrentPackage.getDimH() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getDimH()));
        etWidth.setText(mCurrentPackage.getDimW() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getDimW()));
        etDepth.setText(mCurrentPackage.getDimD() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getDimD()));
        etBarcode.setText(mCurrentPackage.getBarcode() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPackage.getBarcode());
        etAisle.setText(mCurrentPackage.getAisle() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPackage.getAisle());
        etRack.setText(mCurrentPackage.getRack() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getRack()));
        etShelf.setText(mCurrentPackage.getShelf() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getShelf()));
        etAdditionalInfo.setText(mCurrentPackage.getAdditionalText() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPackage.getAdditionalText());

        //TODO list view package parts

        btnAdd.setOnClickListener(new View.OnClickListener() {
            //TODO implement
            @Override
            public void onClick(View view) {
                //Toast.makeText(ModifyPackageActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();


                Package mNewPackage = new Package(
                        scannedID,
                        mDb.SafeGetIntFromEditText(etMass.getText().toString()),
                        mDb.SafeGetIntFromEditText(etHeight.getText().toString()),
                        mDb.SafeGetIntFromEditText(etWidth.getText().toString()),
                        mDb.SafeGetIntFromEditText(etDepth.getText().toString()),
                        mDb.SafeGetStringFromEditText(etAdditionalInfo.getText().toString()),
                        mDb.SafeGetStringFromEditText(etBarcode.getText().toString()),
                        mDb.SafeGetStringFromEditText(etAisle.getText().toString()),
                        mDb.SafeGetIntFromEditText(etRack.getText().toString()),
                        mDb.SafeGetIntFromEditText(etShelf.getText().toString())
                        );

                boolean success = mDb.addPackage(mNewPackage);
                if(success) {
                    Toast.makeText(ModifyPackageActivity.this, "partially succes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ModifyPackageActivity.this, PackageSingleActivity.class);
                    intent.putExtra(PackageSingleActivity.KEY_PACKAGE, mDb.getPackageByRFID(scannedID));
                    startActivity(intent);
                } else {
                    Toast.makeText(ModifyPackageActivity.this, mNewPackage.getRfidTag(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            //TODO implement
            @Override
            public void onClick(View view) {
                Toast.makeText(ModifyPackageActivity.this, mDb.getPackageByRFID("9729").getMass(), Toast.LENGTH_SHORT).show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            //TODO implement
            @Override
            public void onClick(View view) {
                Toast.makeText(ModifyPackageActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
