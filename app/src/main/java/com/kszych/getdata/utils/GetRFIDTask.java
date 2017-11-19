package com.kszych.getdata.utils;

import android.os.AsyncTask;

import com.kszych.getdata.ScanRFIDActivity;

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

public class GetRFIDTask extends AsyncTask<String, Void, String[]> {

    String server;
    private GetRFIDTaskCompleteListener mResponseListener;

    GetRFIDTask(String server, GetRFIDTaskCompleteListener listener){
        this.server=server;
        this.mResponseListener = listener;
    }

    @Override
    protected String[] doInBackground(String... params) {
        boolean found = false;
        String uidHex = "";
        String uidDec = "";
        while (!found) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(server);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                //StringBuilder stringBuffer = new StringBuilder();
                String newLine;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                newLine = bufferedReader.readLine();
//                if ("STOP ASKING".equals(newLine)) {
//                    inputStream.close();
//                    doneFlag = true;
//                    return null;
//                } else {
//                    UidHex = newLine;
//                    newLine = bufferedReader.readLine();
//                    UidDec = newLine;
//                }
                if (newLine.equals(ScanRFIDActivity.DEFAULT_RESPOND_MESSAGE)) {
                    // loop TODO delete this if
                } else if (newLine.equals(ScanRFIDActivity.NOT_READABLE_RESPOND_MESSAGE)) {
                    // TODO toast here - card not compatible
                } else {
                    // stop
                    found = true;
                    uidHex = newLine;
                    uidDec = bufferedReader.readLine();
                }
                inputStream.close();

            } catch(ClientProtocolException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        return new String[]{uidHex, uidDec};
    }

    @Override
    protected void onPostExecute(String[] result) {
        mResponseListener.cardIsRead(result[0], result[1]);
    }

}
