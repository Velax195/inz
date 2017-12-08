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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kszych.pms.utils.DatabaseHelper;
import com.kszych.pms.utils.Package;

import java.util.ArrayList;
import java.util.Locale;

public class OrderExecuteActivity extends AppCompatActivity {

    private ArrayList<Package> mPackagesArray = new ArrayList<>();
    private DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    Package mScannedPackage;
    public static final String DEFAULT_RESPOND_MESSAGE = "CARD_NOT_PRESENT";
    public static final String NOT_READABLE_RESPOND_MESSAGE = "CARD_NOT_READABLE";
    private static final String REQUEST_TAG = "ScannerRequest";
    String mRequestURL = "http://192.168.0.14/scaner";
    private RequestQueue mRequestQueue;
    private boolean[] mCheckArray;
    PackagesArrayAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_execute);

        mPackagesArray.add(mDb.getPackageByRFID(" 85 32 ba 51"));
        mPackagesArray.add(mDb.getPackageByRFID(" e2 2d a1 d5"));
        mPackagesArray.add(mDb.getPackageByRFID(" 04 f1 74 a2 3e 3e 80"));

        TextView tvHowManyLeft = findViewById(R.id.tvHowManyLeft);
        ListView lvPackagesToExecute = findViewById(R.id.lvPackagesToExecute);

        tvHowManyLeft.setText(String.format(Locale.ENGLISH, "%d", mPackagesArray.size()));
        mAdapter = new PackagesArrayAdapter(OrderExecuteActivity.this, mPackagesArray);
        lvPackagesToExecute.setAdapter(mAdapter);

        mRequestQueue = Volley.newRequestQueue(this);
        android.os.SystemClock.sleep(100);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    class PackagesArrayAdapter extends ArrayAdapter<Package> {

        private Context mContext;
        private ArrayList<Package> mPackages;

        public PackagesArrayAdapter(@NonNull Context context, ArrayList<Package> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mContext = context;
            mPackages = objects;
            initBoolArray();
        }

        void initBoolArray() {
            mCheckArray = new boolean[mPackages.size()];
            for (int i = 0; i < mPackages.size(); i++) {
                mCheckArray[i] = false;
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.simple_checkbox_list_item_1, null);
            }
            CheckBox checkbox = convertView.findViewById(R.id.checkbox);
            checkbox.setChecked(mCheckArray[position]);
            checkbox.setText(mPackages.get(position).getRfidTag());

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            for (int i = 0; i < mPackages.size(); i++){
                CheckBox checkbox = findViewById(R.id.checkbox);
                checkbox.setChecked(mCheckArray[i]);
                checkbox.setText(mPackages.get(i).getRfidTag());
            }
        }
    }

    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, mRequestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (isDefaultMessage(response)) {
                    sendRequest();
                } else {
                    String[] uids = response.split("\n");
                    cardIsRead(uids[0]);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendRequest();
            }
        });
        request.setShouldCache(false).setTag(REQUEST_TAG);
        mRequestQueue.add(request);
    }

    public boolean isDefaultMessage(String response) {
        if(response.equals(ScanRFIDActivity.DEFAULT_RESPOND_MESSAGE)) {
            //loop
        } else if(response.equals(ScanRFIDActivity.NOT_READABLE_RESPOND_MESSAGE)) {

        } else {
            return false;
        }
        return true;
    }

    private  void cardIsRead(final String uidHex) {
        mScannedPackage = mDb.getPackageByRFID(uidHex);
        if(mScannedPackage == null) {
            Toast.makeText(OrderExecuteActivity.this, "Not in database", Toast.LENGTH_SHORT).show();
            onResume();
        } else {
            if (mPackagesArray.indexOf(mScannedPackage) == -1) {
                Toast.makeText(OrderExecuteActivity.this, "Not in order", Toast.LENGTH_SHORT).show();
                onResume();
            } else {
                Toast.makeText(OrderExecuteActivity.this, "Take this package", Toast.LENGTH_SHORT).show();
                mCheckArray[mPackagesArray.indexOf(mScannedPackage)] = true;
                mAdapter.notifyDataSetChanged();
                isFinished();
                onResume();
                //TODO change checkViev
                //TODO single toast pls
            }
        }
    }

    void isFinished() {
        for(int i = 0; i < mCheckArray.length; i++) {
            if (!mCheckArray[i]) {
                return;
            }
        }
            Toast.makeText(OrderExecuteActivity.this, "Order completed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OrderExecuteActivity.this, MenuActivity.class);
            startActivity(intent);
    }

}
