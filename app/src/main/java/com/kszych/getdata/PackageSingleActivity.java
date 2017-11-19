package com.kszych.getdata;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kszych.getdata.utils.DatabaseHelper;
import com.kszych.getdata.utils.Package;

public class PackageSingleActivity extends AppCompatActivity {

    public static final String KEY_PACKAGE = "incoming_package";

    private Package mCurrentPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_single);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mCurrentPackage = extras.getParcelable(KEY_PACKAGE);
        }
        else {
            // TODO fail die etc.
        }

        setTitle(getString(R.string.activity_name_single_package, mCurrentPackage.getId()));

        TextView tvMass = findViewById(R.id.tvMass);
        TextView tvDimension = findViewById(R.id.tvDimensions);
        TextView tvBarcode = findViewById(R.id.tvBarcode);
        TextView tvLocation = findViewById(R.id.tvLocation);
        TextView tvAdditional = findViewById(R.id.tvAdditionalInfo);
        ListView lvPackageParts = findViewById(R.id.singlePackagePartsList);
        Button btnModify = findViewById(R.id.singlePackageModify);
        Button btnDelete = findViewById(R.id.singlePackageDelete);

        tvMass.setText(mCurrentPackage.getMass() == DatabaseHelper.DEFAULT_INT
                ? DatabaseHelper.NULL_VAL
                : Integer.toString(mCurrentPackage.getMass()));
        tvDimension.setText(mCurrentPackage.getDimensionsString(
                " x "
                , " [mm]"
                , mCurrentPackage.getDimH()
                , mCurrentPackage.getDimW()
                , mCurrentPackage.getDimD()));
        tvBarcode.setText(mCurrentPackage.getBarcode() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPackage.getBarcode());
        tvLocation.setText(mCurrentPackage.getLocationString(
                mCurrentPackage.getAisle()
                , mCurrentPackage.getRack()
                , mCurrentPackage.getShelf()));
        tvAdditional.setText(mCurrentPackage.getAdditionalText() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPackage.getAdditionalText());

        // TODO list view package parts

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO implement
                Toast.makeText(PackageSingleActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PackageSingleActivity.this);
                builder.setTitle(R.string.dialog_confirm_delete_title)
                        .setMessage(R.string.dialog_confirm_delete_message)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO delete from database and call on back pressed and other scary things
                                Toast.makeText(PackageSingleActivity.this
                                        , "DELETED! Just kidding", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });


    }

    private String getDimensionsString(@NonNull String separator, @Nullable String append, int... args) {
        boolean isNull = true;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length && isNull; i++) {
            if(args[i] != DatabaseHelper.DEFAULT_INT) {
                isNull = false;
            }

            if(i != 0) {
                builder.append(separator);
            }
            builder.append(args[i] == DatabaseHelper.DEFAULT_INT ? "0" : Integer.toString(args[i]));
        }
        if(append != null) {
            builder.append(append);
        }

        if(isNull) {
            return DatabaseHelper.NULL_VAL;
        }

        return builder.toString();
    }

    private String getLocationString(@Nullable String aisle, int rack, int shelf) {
        if(aisle == DatabaseHelper.DEFAULT_STRING
                && rack == DatabaseHelper.DEFAULT_INT
                && shelf == DatabaseHelper.DEFAULT_INT) {
            return DatabaseHelper.NULL_VAL;
        }

        return getString(R.string.label_aisle) + " "
                + (aisle == DatabaseHelper.DEFAULT_STRING ? "?" : aisle) + ": R"
                + (rack == DatabaseHelper.DEFAULT_INT ? "?" : rack) + " P"
                + (shelf == DatabaseHelper.DEFAULT_INT ? "?" : shelf);
    }
}