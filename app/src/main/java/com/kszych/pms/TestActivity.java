package com.kszych.pms;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kszych.pms.utils.DatabaseHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestActivity extends AppCompatActivity {

    Button btON;
    Button btOFF;
    Button btTimer;
    Button btDatabase;
    Button btOrder;
    TextView tvData;
    String ip = "http://192.168.0.14";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btON = findViewById(R.id.btON);
        btOFF = findViewById(R.id.btOFF);
        btTimer = findViewById(R.id.btTimer);
        tvData = findViewById(R.id.data);
        btDatabase = findViewById(R.id.btDatabase);
        btOrder = findViewById(R.id.btTestExecuteOrder);


        btON.setOnClickListener(myListener);
        btOFF.setOnClickListener(myListener);
        btTimer.setOnClickListener(myListener);
        btDatabase.setOnClickListener(myListener);
        btOrder.setOnClickListener(myListener);
    }


    View.OnClickListener myListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String message;
            if (view == btON) {
                message = "/on";
            } else if (view == btOFF) {
                message = "/off";
            } else if (view == btTimer) {
                message = "/timer";
            } else if (view == btDatabase) {
                message = "";
                DatabaseHelper mDb = DatabaseHelper.getInstance(TestActivity.this);
                mDb.createTestDatabase();
                onBackPressed();
            } else if (view == btOrder) {
                message = "";
                Intent intent = new Intent(TestActivity.this, OrderExecuteActivity.class);
                startActivity(intent);
            } else {
                message = null;
            }
            btON.setEnabled(false);
            btOFF.setEnabled(false);
            btTimer.setEnabled(false);

            ip = "http://192.168.0.14" + message;
            execute();

        }

    };

    void execute() {
        try {

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(ip);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();

            InputStream inputStream = httpEntity.getContent();

            StringBuilder stringBuffer = new StringBuilder();
            String newLine;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            while ((newLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(newLine);
                stringBuffer.append("\n");
            }

            tvData.setText(stringBuffer.toString());

            inputStream.close();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btON.setEnabled(true);
        btOFF.setEnabled(true);
        btTimer.setEnabled(true);
        btDatabase.setEnabled(true);
    }
}
