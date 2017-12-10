package com.kszych.pms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;

import java.util.ArrayList;

public class PackageListActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = PackageListActivity.class.getName();

    private DatabaseHelper mDb;
    private ArrayList<Package> mPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        setTitle(R.string.tittle_activity_list_packages);

        mDb = DatabaseHelper.getInstance(PackageListActivity.this);
        mPackages = mDb.getPackages();

        ListView listView = findViewById(R.id.lv_packages);
        PackagesArrayAdapter adapter = new PackagesArrayAdapter(PackageListActivity.this, mPackages);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PackageListActivity.this, PackageSingleActivity.class);
                intent.putExtra(PackageSingleActivity.KEY_PACKAGE, mPackages.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPackages = mDb.getPackages();

        ListView listView = findViewById(R.id.lv_packages);
        PackagesArrayAdapter adapter = new PackagesArrayAdapter(PackageListActivity.this, mPackages);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PackageListActivity.this, PackageSingleActivity.class);
                intent.putExtra(PackageSingleActivity.KEY_PACKAGE, mPackages.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_actions_packages_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent mIntent = new Intent(PackageListActivity.this, ScanRFIDActivity.class);
                mIntent.putExtra(ScanRFIDActivity.FROM_ACTIVITY, ACTIVITY_NAME);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class PackagesArrayAdapter extends ArrayAdapter<Package> {

        private Context mContext;
        private ArrayList<Package> mPackages;

        public PackagesArrayAdapter(@NonNull Context context, ArrayList<Package> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mPackages = objects;
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
            listItemText.setText(mPackages.get(position).getRfidTag());

            return convertView;
        }
    }
}
