package com.kszych.pms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;
import com.kszych.pms.utils.Part;

import java.util.ArrayList;

public class PackageSingleActivity extends AppCompatActivity {

    public static final String KEY_PACKAGE = "incoming_package";

    private ArrayList<Part> mPartsInPackage;
    private DatabaseHelper mDb = DatabaseHelper.getInstance(this);
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

        if(mCurrentPackage != null) {
            mPartsInPackage = mDb.getPartsInPackage(mCurrentPackage);
        }
        // TODO edit layout - three separate fields for dimensions

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

        PartsInPackageArrayAdapter adapter = new PartsInPackageArrayAdapter(this, mPartsInPackage);
        lvPackageParts.setAdapter(adapter);

        lvPackageParts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (PackageSingleActivity.this, PartSingleActivity.class);
                intent.putExtra(PartSingleActivity.KEY_PART, mPartsInPackage.get(i));
                startActivity(intent);
            }
        });

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

    private String readStringValueFromField(EditText field) {
        String val = field.getText().toString().trim();
        if(val.equals(DatabaseHelper.NULL_VAL) || val.length() == 0) {
            return DatabaseHelper.DEFAULT_STRING;
        }
        return val;
    }

    private int readIntValueFromField(EditText field) {
        String val = field.getText().toString().trim();
        if(val.equals(DatabaseHelper.NULL_VAL) || val.length() == 0) {
            return DatabaseHelper.DEFAULT_INT;
        }
        return Integer.valueOf(val);
    }

    private double readDoubleValueFromField(EditText field) {
        String val = field.getText().toString().trim();
        if(val.equals(DatabaseHelper.NULL_VAL) || val.length() == 0) {
            return DatabaseHelper.DEFAULT_INT;
        }
        return Double.valueOf(val);
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

    class PartsInPackageArrayAdapter extends ArrayAdapter<Part> {

        private Context mContext;
        private ArrayList<Part> mParts;

        public PartsInPackageArrayAdapter(@NonNull Context context, ArrayList<Part> objects ) {
            super(context, android.R.layout.simple_list_item_1, objects);
                    mParts = objects;
                    mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            }
            TextView listItemText = convertView.findViewById(android.R.id.text1);
            listItemText.setText(mParts.get(position).getName());

            return convertView;
        }
    }
}
