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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);
        setTitle("pms");

        mDb = DatabaseHelper.getInstance(PartListActivity.this);

//        if(mDb.count(DatabaseHelper.TPart.TNAME) < 20) {
//            Random r = new Random();
//            for (int i = 0; i < 20; i++) {
//                mDb.addTestParts("sth" + r.nextInt(5000));
//            }
//        }

        mParts = mDb.getParts();

        ListView listView = findViewById(R.id.lv_parts);
        PartsArrayAdapter adapter = new PartsArrayAdapter(PartListActivity.this, mParts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PartListActivity.this, PartSingleActivity.class);
                intent.putExtra(PartSingleActivity.KEY_PART, mParts.get(position));
                startActivity(intent); // TODO startactivity for result? because the record can be deleted
            }
        });
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
                // TODO implement
//                Toast.makeText(PackageListActivity.this
//                        , R.string.not_implemented, Toast.LENGTH_SHORT).show();
                // DEBUG DELETE_ME
                StringBuilder builder = new StringBuilder();
                ArrayList<Part> parts = mDb.getParts();
                for(Part singlePart : parts) {
                    builder.append(singlePart.getName());
                }

                Toast.makeText(PartListActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
