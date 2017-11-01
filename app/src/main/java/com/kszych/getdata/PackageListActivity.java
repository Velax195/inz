package com.kszych.getdata;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kszych.getdata.utils.DatabaseHelper;
import com.kszych.getdata.utils.Package;

import java.util.ArrayList;
import java.util.List;

public class PackageListActivity extends AppCompatActivity {

    private DatabaseHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        setTitle(R.string.activity_name_list_packages);

        mDb = DatabaseHelper.getInstance(PackageListActivity.this);

        mDb.addTestPackage("sth1");
        mDb.addTestPackage("sth2");
        mDb.addTestPackage("sth3");
        mDb.addTestPackage("sth4");

        ListView listView = findViewById(R.id.lv_packages);
        PackagesArrayAdapter adapter = new PackagesArrayAdapter(PackageListActivity.this, mDb.getPackages());
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // TODO implement
//                Toast.makeText(PackageListActivity.this
//                        , R.string.not_implemented, Toast.LENGTH_SHORT).show();
                // DEBUG DELETE_ME
                StringBuilder builder = new StringBuilder();
                ArrayList<Package> packages = mDb.getPackages();
                for(Package singlePackage : packages) {
                    builder.append(singlePackage.getRfidTag());
                }

                Toast.makeText(PackageListActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
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
