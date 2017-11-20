package com.kszych.pms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;
import com.kszych.pms.utils.Part;

import java.util.ArrayList;

public class PartSingleActivity extends AppCompatActivity {

    public static final String KEY_PART = "single_part";

    private Part mPart;
    private ArrayList<Package> mPackagesOfPart;
    private DatabaseHelper mDb = DatabaseHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_single);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mPart = extras.getParcelable(KEY_PART);

            if(mPart == null) {
                // TODO die panic etc
            }
            mPackagesOfPart = mDb.getPackagesContainingPart(mPart);

            ListView packagesList = findViewById(R.id.singlePartPackagesList);
            PackagesInPartArrayAdapter adapter = new PackagesInPartArrayAdapter(this, mPackagesOfPart);
            packagesList.setAdapter(adapter);

        }
        else {
            // TODO die etc. We require extras
            Toast.makeText(this, "PART IS NULLL WHAAAT TO DOOOOO", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
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
}
