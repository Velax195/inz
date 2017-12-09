package com.kszych.pms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.kszych.pms.utils.Part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderExecuteActivity extends AppCompatActivity {

    private static final String TAG = OrderExecuteActivity.class.getSimpleName();


    private static final String REQUEST_TAG = "ScannerRequest";
    Package mScannedPackage;
    String mRequestURL = "http://192.168.0.14/scaner";
    PackagesArrayAdapter mAdapter;
    private ArrayList<Package> mPackagesArray;
    private DatabaseHelper mDb = DatabaseHelper.getInstance(this);
    private RequestQueue mRequestQueue;
    private boolean[] mCheckArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_execute);

        boolean isEnough = true;

        Map<Part, Integer> partsInOrder = new HashMap<>();
        partsInOrder.put(mDb.getPartByName("ołówek"), 20);
        partsInOrder.put(mDb.getPartByName("długopis"), 10);
        partsInOrder.put(mDb.getPartByName("pędzel"), 30);
        partsInOrder.put(mDb.getPartByName("kartka"), 50);
        partsInOrder.put(mDb.getPartByName("linijka"), 10);


        Map<Part, Integer> filteredPartCounts = mDb.getPartsMissingFromDb(partsInOrder);
        for(Map.Entry<Part, Integer> singleEntry : filteredPartCounts.entrySet()) {
            if(singleEntry.getValue() > 0) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                isEnough = false;
            }
        }

        if (isEnough) {
            mPackagesArray = mDb.getPackagesInOrder(partsInOrder);

        } else {
            //TODO show missing parts
            mPackagesArray = new ArrayList<>();

            mPackagesArray.add(mDb.getPackageByRFID(" 85 32 ba 51"));
            mPackagesArray.add(mDb.getPackageByRFID(" e2 2d a1 d5"));
            mPackagesArray.add(mDb.getPackageByRFID(" 04 f1 74 a2 3e 3e 80"));

        }
        TextView tvHowManyLeft = findViewById(R.id.tvHowManyLeft);
        ListView lvPackagesToExecute = findViewById(R.id.lvPackagesToExecute);

        tvHowManyLeft.setText(String.format(Locale.ENGLISH, "%d", mPackagesArray.size()));
        mAdapter = new PackagesArrayAdapter(OrderExecuteActivity.this, mPackagesArray);
        lvPackagesToExecute.setAdapter(mAdapter);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mRequestQueue != null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return request.getTag() != null && request.getTag().equals(REQUEST_TAG);
                }
            });
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
        if (response.equals(ScanRFIDActivity.DEFAULT_RESPOND_MESSAGE)) {
            //loop
        } else if (response.equals(ScanRFIDActivity.NOT_READABLE_RESPOND_MESSAGE)) {

        } else {
            return false;
        }
        return true;
    }

    private void cardIsRead(final String uidHex) {
        mScannedPackage = mDb.getPackageByRFID(uidHex);
        if (mScannedPackage == null) {
            Toast.makeText(OrderExecuteActivity.this, "Not in database", Toast.LENGTH_SHORT).show();
        } else {
            if (mPackagesArray.indexOf(mScannedPackage) == -1) {
                Toast.makeText(OrderExecuteActivity.this, "Not in order", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OrderExecuteActivity.this, "Take this package", Toast.LENGTH_SHORT).show();
                mCheckArray[mPackagesArray.indexOf(mScannedPackage)] = true;
                mAdapter.notifyDataSetChanged();
                isFinished();
                //TODO single toast pls
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onResume();
            }
        }, 1000);
    }

    void isFinished() {
        for (int i = 0; i < mCheckArray.length; i++) {
            if (!mCheckArray[i]) {
                return;
            }
        }
        mAdapter.initBoolArray();
        Toast.makeText(OrderExecuteActivity.this, "Order completed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(OrderExecuteActivity.this, MenuActivity.class);
        startActivity(intent);
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

        public void initBoolArray() {
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
            checkbox.setClickable(false);
            checkbox.setChecked(mCheckArray[position]);
            checkbox.setText(mPackages.get(position).getRfidTag());

            return convertView;
        }
    }

}
