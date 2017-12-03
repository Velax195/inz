package com.kszych.pms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PackageModifyActivity extends AppCompatActivity {

    public static final String KEY_PACKAGE = "incoming_package";
    public static final String KEY_ACTIVITY = "previous_activity";
    public static final String KEY_RFID_TAG = "scanned_rfid_tag";
    public static final String KEY_CHECK_PARTS = "parts_to_check";

    private static final int REQUEST_CODE_ADD_PARTS = 101;

    private Package mCurrentPackage = null;
    private ArrayList<Part> mCurrentParts;

    DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    String scannedID;
    String previousActivity = null;
    boolean shit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_package);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            previousActivity = extras.getString(KEY_ACTIVITY);
            scannedID = extras.getString(KEY_RFID_TAG);
            if (previousActivity.equals(getResources().getString(R.string.packageListActivityName))) {
                shit = true;
            } else {
            mCurrentPackage = extras.getParcelable(KEY_PACKAGE);
            mCurrentParts = mDb.getPartsInPackage(mCurrentPackage);
            }
        } else {
            // TODO fail die etc.
            Toast.makeText(PackageModifyActivity.this, "error", Toast.LENGTH_SHORT).show();
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

        Button btnAdd = findViewById(R.id.modifyPackageAdd);
        Button btnDelete = findViewById(R.id.modifyPackageDelete);
        Button btnSave = findViewById(R.id.modifyPackageSave);


        if (mCurrentPackage != null) {
            setTitle(getString(R.string.modifyPackageTitle, Integer.toString(mCurrentPackage.getId())));
        } else {
            setTitle(getString(R.string.modifyPackageTitle, "nowa"));
            //mCurrentPackage = new Package(DatabaseHelper.DEFAULT_INT, DatabaseHelper.DEFAULT_STRING);
        }

        if (mCurrentPackage!=null) {
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
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO changes here
                Intent intent = new Intent(PackageModifyActivity.this, PartListActivity.class);
                intent.putExtra(PartListActivity.KEY_ACTIVITY, PartListActivity.Flow.CHECKABLE.name());
                intent.putParcelableArrayListExtra(KEY_CHECK_PARTS, mCurrentParts);
                if (previousActivity.equals(getResources().getString(R.string.packageListActivityName))) {
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
                    mDb.addPackage(mNewPackage);
                    mCurrentPackage = mDb.getPackageByRFID(scannedID);
                }
                intent.putExtra(PartListActivity.KEY_INCOMING_PACKAGE, mCurrentPackage);
                startActivityForResult(intent, REQUEST_CODE_ADD_PARTS);
            }
        });

        if (previousActivity.equals(getResources().getString(R.string.packageListActivityName))) {
            btnDelete.setText("Cofnij");
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDb.deletePackage(mCurrentPackage.getRfidTag());
                    onBackPressed();
                }
            });
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentPackage!=null) {
                    Package mNewPackage = new Package(
                            mCurrentPackage.getId(),
                            mCurrentPackage.getRfidTag(),
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
                    mDb.updatePackage(mNewPackage);
                    //TODO deal with activityStack, onBack shows previous values
                    onBackPressed();

                } else {
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
                    mDb.addPackage(mNewPackage);
                    Intent intent = new Intent(PackageModifyActivity.this, PackageSingleActivity.class);
                    intent.putExtra(PackageSingleActivity.KEY_PACKAGE, mDb.getPackageByRFID(scannedID));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentPackage==null) {
            shit = true;
        } else {
            mCurrentPackage = mDb.getPackageByRFID(mCurrentPackage.getRfidTag());
            ListView lvPackageParts = findViewById(R.id.modifyPackagePartsList);
            PartsInPackageArrayAdapter adapter = new PartsInPackageArrayAdapter(this, mCurrentParts);
            lvPackageParts.setAdapter(adapter);
        }
    }

//    private void updatePartsPackageDatabase(ArrayList<Part> oldList, ArrayList<Part> newList) {
//        ArrayList<Part> partsToRemove = new ArrayList<>();
//        if(oldList == null) {
//            mDb.addPackageParts(mCurrentPackage, newList, 1);
//        } else {
//            for (Part singleOldPart : oldList) {
//                if (newList.contains(singleOldPart)) {
//                    partsToRemove.add(singleOldPart);
//                }
//            }
//
//            for (Part singlePartToRemove : partsToRemove) {
//                oldList.remove(singlePartToRemove);
//                newList.remove(singlePartToRemove);
//            }
//
//            mDb.deletePackageParts(mCurrentPackage, oldList);
//            mDb.addPackageParts(mCurrentPackage, newList, 1);
//        }
//    }

    private void updatePartsPackageDatabase(ArrayList<Part> oldList, ArrayList<Part> newList, ArrayList<Integer> partsQuantity) {
        ArrayList<Part> partsToRemove = new ArrayList<>();
        if(oldList == null) {
            mDb.addPackageParts(mCurrentPackage, newList, partsQuantity);
        } else {
            for (Part singleOldPart : oldList) {
                if (newList.contains(singleOldPart)) {
                    partsToRemove.add(singleOldPart);
                }
            }

            for (Part singlePartToRemove : partsToRemove) {
                oldList.remove(singlePartToRemove);
                newList.remove(singlePartToRemove);
            }

            mDb.deletePackageParts(mCurrentPackage, oldList);
            mDb.addPackageParts(mCurrentPackage, newList, partsQuantity);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_PARTS) {
            if (resultCode == RESULT_OK) {
                if (mCurrentPackage==null) {
                }
                else {
                    ArrayList<Part> newParts = data.getParcelableArrayListExtra(PartListActivity.KEY_SELECTED_PARTS);
                    ArrayList<Integer> partsQuantity = data.getExtras().getIntegerArrayList(PartListActivity.KEY_PARTS_QUANTITY);
                    updatePartsPackageDatabase(mCurrentParts, newParts, partsQuantity);
                    mCurrentParts = mDb.getPartsInPackage(mCurrentPackage);
                }

            }
        }
    }

    class PartsInPackageArrayAdapter extends ArrayAdapter<Part> {

        private Context mContext;
        private ArrayList<Part> mParts;

        public PartsInPackageArrayAdapter(@NonNull Context context, ArrayList<Part> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mParts = objects;
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            }
            TextView listItemText = convertView.findViewById(android.R.id.text1);
            listItemText.setText(mParts.get(position).getName());

            return convertView;
        }
    }
}
