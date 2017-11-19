package com.kszych.getdata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kszych.getdata.utils.DatabaseHelper;
import com.kszych.getdata.utils.Package;

public class ModifyPackageActivity extends AppCompatActivity {

    public static final String KEY_PACKAGE = "incoming_package";

    private Package mCurrentPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_package);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mCurrentPackage = extras.getParcelable(KEY_PACKAGE);
        }
        else {
            // TODO fail die etc.
        }


        EditText etDimensions = findViewById(R.id.etDimensions);
        EditText etMass = findViewById(R.id.etMass);
        EditText etBarcode = findViewById(R.id.etBarcode);
        EditText etLocation = findViewById(R.id.etLocation);
        EditText etAdditionalInfo = findViewById(R.id.etAdditionalInfo);
        ListView lvPackageParts = findViewById(R.id.modifyPackagePartsList);
        Button btnAdd = findViewById(R.id.modifyPackageAdd);
        Button btnDelete = findViewById(R.id.modifyPackageDelete);
        Button btnSave = findViewById(R.id.modifyPackageSave);

        if(mCurrentPackage != null) {
            setTitle(getString(R.string.modifyPackageTitle, Integer.toString(mCurrentPackage.getId())));
        }
        else {
            setTitle(getString(R.string.modifyPackageTitle, "nowa"));
            mCurrentPackage = new Package(DatabaseHelper.DEFAULT_INT, DatabaseHelper.DEFAULT_STRING);
        }

        etMass.setText(mCurrentPackage.getMass() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getMass()));
        etDimensions.setText(mCurrentPackage.getDimensionsString(
                "x "
                , " [mm]"
                , mCurrentPackage.getDimH()
                , mCurrentPackage.getDimW()
                , mCurrentPackage.getDimD()));
        etBarcode.setText(mCurrentPackage.getBarcode() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPackage.getBarcode());
        etLocation.setText(mCurrentPackage.getLocationString(
                mCurrentPackage.getAisle()
                , mCurrentPackage.getRack()
                , mCurrentPackage.getShelf()));
        etAdditionalInfo.setText(mCurrentPackage.getAdditionalText() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPackage.getAdditionalText());

        //TODO list view package parts

        btnAdd.setOnClickListener(new View.OnClickListener() {
            //TODO implement
            @Override
            public void onClick(View view) {
                Toast.makeText(ModifyPackageActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            //TODO implement
            @Override
            public void onClick(View view) {
                Toast.makeText(ModifyPackageActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
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
