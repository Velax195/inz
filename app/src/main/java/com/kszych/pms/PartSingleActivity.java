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

public class PartSingleActivity extends AppCompatActivity {

    public static final String KEY_PART = "single_part";

    private Part mCurrentPart;
    private ArrayList<Package> mPackagesOfPart;
    private DatabaseHelper mDb = DatabaseHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_single);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mCurrentPart = extras.getParcelable(KEY_PART);

            if(mCurrentPart == null) {
                // TODO die panic etc
            }
            mPackagesOfPart = mDb.getPackagesContainingPart(mCurrentPart);

            Button btnModify = findViewById(R.id.singlePartModify);
            Button btnDelete = findViewById(R.id.btnDelete);

            setText();

            btnModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PartSingleActivity.this, PartAddActivity.class);
                    intent.putExtra(PartAddActivity.KEY_PART, mCurrentPart);
                    startActivity(intent);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDb.deletePart(mCurrentPart.getName());
                    onBackPressed();
                }
            });

            ListView packagesList = findViewById(R.id.singlePartPackagesList);
            PackagesInPartArrayAdapter adapter = new PackagesInPartArrayAdapter(this, mPackagesOfPart);
            packagesList.setAdapter(adapter);

            packagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(PartSingleActivity.this, PackageSingleActivity.class);
                    intent.putExtra(PackageSingleActivity.KEY_PACKAGE, mPackagesOfPart.get(i));
                    startActivity(intent);
                }
            });

        }
        else {
            // TODO die etc. We require extras
            Toast.makeText(this, "PART IS NULLL WHAAAT TO DOOOOO", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentPart = mDb.getPartById(mCurrentPart.getId());
        setText();
    }

    class PackagesInPartArrayAdapter extends ArrayAdapter<Package> {

        private Context mContext;
        private ArrayList<Package> mPackages;

        public PackagesInPartArrayAdapter(@NonNull Context context, ArrayList<Package> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mPackages = objects;
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
            listItemText.setText(mPackages.get(position).getRfidTag());

            return convertView;
        }
    }

    void setText(){

        setTitle(mCurrentPart.getName());

        TextView tvName = findViewById(R.id.tvName);
        TextView tvBuyURL = findViewById(R.id.tvBuyURL);
        TextView tvProducerName = findViewById(R.id.tvProducer);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo);

        tvName.setText(mCurrentPart.getName() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : (mCurrentPart.getName()));
        tvBuyURL.setText(mCurrentPart.getBuyUrl() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPart.getBuyUrl());
        tvProducerName.setText(mCurrentPart.getProducerName() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPart.getProducerName());
        tvPrice.setText(mCurrentPart.getPrice() == DatabaseHelper.DEFAULT_REAL
                ? DatabaseHelper.NULL_VAL
                : Double.toString(mCurrentPart.getPrice()));
        tvAdditionalInfo.setText(mCurrentPart.getAdditionalInfo() == DatabaseHelper.DEFAULT_STRING
                ? DatabaseHelper.NULL_VAL
                : mCurrentPart.getAdditionalInfo());
    }
}
