package com.kszych.pms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Part;

import java.util.ArrayList;
import java.util.Random;

public class PartListActivity extends AppCompatActivity {

    private DatabaseHelper mDb;
    private ArrayList<Part> mParts;
    final int sendRequestCode = 1;
    boolean shouldResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);
        setTitle("PMS");

        mDb = DatabaseHelper.getInstance(PartListActivity.this);
        mParts = mDb.getParts();

        ListView listView = findViewById(R.id.lv_parts);
        PartsArrayAdapter adapter = new PartsArrayAdapter(PartListActivity.this, mParts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PartListActivity.this, PartSingleActivity.class);
                intent.putExtra(PartSingleActivity.KEY_PART, mParts.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldResume) {
            mParts = mDb.getParts();
            ListView listView = findViewById(R.id.lv_parts);
            PartsArrayAdapter adapter = new PartsArrayAdapter(PartListActivity.this, mParts);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PartListActivity.this, PartSingleActivity.class);
                    intent.putExtra(PartSingleActivity.KEY_PART, mParts.get(position));
                    startActivity(intent);
                }
            });
        }
        shouldResume = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_actions_part_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent sIntent = new Intent(PartListActivity.this, PartAddActivity.class);
                startActivity(sIntent);
                return true;
            case R.id.action_search:
                Intent sendIntent = new Intent(PartListActivity.this, PartSearchActivity.class);
                startActivityForResult(sendIntent, sendRequestCode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == sendRequestCode) {
            if (resultCode == RESULT_OK) {
                shouldResume = false;

                Part mFilteredPart = data.getParcelableExtra(PartSearchActivity.SEARCH_PACKAGE);
                mParts = mDb.searchParts(mFilteredPart);

                ListView listView = findViewById(R.id.lv_parts);
                PartsArrayAdapter adapter = new PartsArrayAdapter(PartListActivity.this, mParts);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(PartListActivity.this, PartSingleActivity.class);
                        intent.putExtra(PartSingleActivity.KEY_PART, mParts.get(position));
                        startActivity(intent);
                    }
                });
            } else {
                onResume();
            }
        }
    }

    class PartsArrayAdapter extends ArrayAdapter<Part> {

        private Context mContext;
        private ArrayList<Part> mParts;

        public PartsArrayAdapter(@NonNull Context context, ArrayList<Part> objects) {
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
