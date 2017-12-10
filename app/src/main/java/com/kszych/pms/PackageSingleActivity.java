package com.kszych.pms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;
import com.kszych.pms.utils.Part;

import java.util.ArrayList;

//TODO show part in package quantity next to name

public class PackageSingleActivity extends AppCompatActivity {

    public static final String KEY_PACKAGE = "incoming_package";
    public static final String ACTIVITY_NAME = "package_single";

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

        setTitle(getString(R.string.tittle_activity_single_package, mCurrentPackage.getId()));

        Button btnModify = findViewById(R.id.singlePackageModify);
        Button btnDelete = findViewById(R.id.singlePackageDelete);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PackageSingleActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PackageSingleActivity.this, PackageModifyActivity.class);
                intent.putExtra(PackageModifyActivity.KEY_PACKAGE, mCurrentPackage);
                intent.putExtra(PackageModifyActivity.KEY_ACTIVITY, ACTIVITY_NAME);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDb.deletePackage(mCurrentPackage.getRfidTag());
                onBackPressed();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentPackage = mDb.getPackageByRFID(mCurrentPackage.getRfidTag());
        mPartsInPackage = mDb.getPartsInPackage(mCurrentPackage);
        setText();
        ListView lvPackageParts = findViewById(R.id.singlePackagePartsList);
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
            String builder = String.valueOf(mDb.countPartsInPackage(mCurrentPackage, mParts.get(position))) +
                    " x " + mParts.get(position).getName();
            listItemText.setText(builder);

            return convertView;
        }
    }

    void setText(){
        TextView tvMass = findViewById(R.id.tvMass);
        TextView tvDimension = findViewById(R.id.tvDimensions);
        TextView tvBarcode = findViewById(R.id.tvBarcode);
        TextView tvLocation = findViewById(R.id.tvLocation);
        TextView tvAdditional = findViewById(R.id.tvAdditionalInfo);
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
    }
}
