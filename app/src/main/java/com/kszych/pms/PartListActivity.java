package com.kszych.pms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;
import com.kszych.pms.utils.Part;

import java.util.ArrayList;
import java.util.Locale;

public class PartListActivity extends AppCompatActivity {

    private DatabaseHelper mDb;
    private ArrayList<Part> mParts;
    private ArrayAdapter mCurrentListAdapter;
    final int sendRequestCode = 1;
    boolean shouldResume = true;
    private Flow mCurrentFlow;
    private Package mCurrentPackage = null;

    public static final String KEY_ACTIVITY = "previousActivity";
    public static final String KEY_SELECTED_PARTS = "selectedParts";
    public static final String KEY_PARTS_QUANTITY = "partsQuantity";
    public static final String KEY_INCOMING_PACKAGE = "incomingPackage";

    enum Flow {
        STANDARD, CHECKABLE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);
        setTitle(R.string.tittle_activity_list_parts);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mCurrentFlow = Flow.valueOf(extras.getString(KEY_ACTIVITY, Flow.STANDARD.name()));
        }
        else {
            mCurrentFlow = Flow.STANDARD;
        }
        if(mCurrentFlow == Flow.CHECKABLE) {
            mCurrentPackage = extras.getParcelable(KEY_INCOMING_PACKAGE);
        }

        mDb = DatabaseHelper.getInstance(PartListActivity.this);
        mParts = mDb.getParts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldResume) {
            mParts = mDb.getParts();
            ListView listView = findViewById(R.id.lv_parts);
            if(mCurrentFlow == Flow.CHECKABLE) {
                mCurrentListAdapter = new PartsAddArrayAdapter(PartListActivity.this, mParts, mCurrentPackage);
            }
            else {
                mCurrentListAdapter = new PartsArrayAdapter(PartListActivity.this, mParts);
            }

            listView.setAdapter(mCurrentListAdapter);

            if(mCurrentFlow == Flow.STANDARD) {
                listView.setOnItemClickListener(constructListenerForStandardFlow());
            }
            else if(mCurrentFlow == Flow.CHECKABLE) {
                listView.setOnItemClickListener(constructListenerForCheckable());
            }
        }
        shouldResume = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mCurrentFlow == Flow.CHECKABLE) {
            getMenuInflater().inflate(R.menu.list_actions_part_list_checkable, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.list_actions_part_list, menu);
        }
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
            case R.id.action_done:
                Intent resultIntent = new Intent();
                ArrayList<Part> selectedParts
                    = ((PartsAddArrayAdapter) mCurrentListAdapter).getSelectedPartsList();
                ArrayList<Integer> selectedPartsQuantity
                        = ((PartsAddArrayAdapter) mCurrentListAdapter).getSelectedPartsQuantity();
                resultIntent.putParcelableArrayListExtra(KEY_SELECTED_PARTS, selectedParts);
                resultIntent.putExtra(KEY_PARTS_QUANTITY, selectedPartsQuantity);
                setResult(RESULT_OK, resultIntent);
                finish();
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

                // TODO check if below code is necessary
                ListView listView = findViewById(R.id.lv_parts);
                PartsArrayAdapter adapter = new PartsArrayAdapter(PartListActivity.this, mParts);
                listView.setAdapter(adapter);

                if(mCurrentFlow == Flow.STANDARD) {
                    listView.setOnItemClickListener(constructListenerForStandardFlow());
                }
                else if(mCurrentFlow == Flow.CHECKABLE) {
                    listView.setOnItemClickListener(constructListenerForCheckable());
                }
            } else {
                onResume();
            }
        }
    }

    private AdapterView.OnItemClickListener constructListenerForStandardFlow() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PartListActivity.this, PartSingleActivity.class);
                intent.putExtra(PartSingleActivity.KEY_PART, mParts.get(position));
                startActivity(intent);
            }
        };
    }

    private AdapterView.OnItemClickListener constructListenerForCheckable() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                LayoutInflater inflater = PartListActivity.this.getLayoutInflater();
                final PartsAddArrayAdapter adapter = (PartsAddArrayAdapter) adapterView.getAdapter();
                View dialogContent = inflater.inflate(R.layout.dialog_content_part_quantity, null);
                final EditText editText = dialogContent.findViewById(R.id.etDialogPartQuantity);
                editText.setText(String.format(Locale.ENGLISH, "%d", adapter.getQuantityForItem(position)));
                AlertDialog.Builder builder = new AlertDialog.Builder(PartListActivity.this);
                builder.setMessage(R.string.dialog_tittle_how_many_parts)
                        .setView(dialogContent)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.setQuantityForItem(position, Integer.valueOf(editText.getText().toString()));
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        };
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

    class PartsAddArrayAdapter extends ArrayAdapter<Part> {

        private Context mContext;
        private Package mPackage;
        private ArrayList<Part> mParts;
        private int[] mQuantityArray;

        public PartsAddArrayAdapter(Context context, ArrayList<Part> objects, Package currentPackage) {
            super(context, R.layout.simple_checkbox_list_item_1, objects);
            mParts = objects;
            mContext = context;
            mPackage = currentPackage;
            mQuantityArray = new int[mParts.size()];
            initIntArray();
        }

        private void initIntArray() {
            ArrayList<Part> partsToSetText = getIntent().getParcelableArrayListExtra(PackageModifyActivity.KEY_CHECK_PARTS);
            if(partsToSetText == null) {
                for (int i = 0; i < mQuantityArray.length; i++){
                    mQuantityArray[i] = 0;
                }
            } else {
                for(int i = 0; i < mQuantityArray.length; i++){
                    if(partsToSetText.contains(mParts.get(i))) {
                        mQuantityArray[i] = mDb.countPartsInPackage(mPackage, mParts.get(i));
                    } else {
                        mQuantityArray[i] = 0;
                    }
                }
            }
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_edit_text_list_item_1, null);

            EditText editText = convertView.findViewById(R.id.etPartQuantity);
            TextView textView = convertView.findViewById(R.id.partName);

            editText.setText(String.format(Locale.ENGLISH, "%d", mQuantityArray[position]));
            textView.setText((mParts.get(position).getName()));

            return convertView;
        }


        public ArrayList<Part> getSelectedPartsList() {
            ArrayList<Part> selectedPartsList = new ArrayList<>();
            for(int i = 0; i < mQuantityArray.length; i++){
                if(mQuantityArray[i] > 0) {
                    selectedPartsList.add(mParts.get(i));
                }
            }
            return selectedPartsList;
        }

        public ArrayList<Integer> getSelectedPartsQuantity() {
            ArrayList<Integer> selectedPartsQuantity = new ArrayList<>();
            for(int i = 0; i < mQuantityArray.length; i++){
                if(mQuantityArray[i] > 0) {
                    selectedPartsQuantity.add(mQuantityArray[i]);
                }
            }
            return selectedPartsQuantity;
        }

        void setQuantityForItem(int position, int quantity) {
            mQuantityArray[position] = quantity;
        }

        int getQuantityForItem(int position) {
            return mQuantityArray[position];
        }
    }
}

