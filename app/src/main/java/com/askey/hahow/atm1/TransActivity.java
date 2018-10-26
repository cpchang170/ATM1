package com.askey.hahow.atm1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransActivity extends AppCompatActivity {
    private static final String TAG = TransActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        new TransAsyncTask().execute("http://atm201605.appspot.com/h");
        OkHttpClient client = new OkHttpClient();
        try {
            final Request request = new Request.Builder()
                    .url("http://atm201605.appspot.com/h")
                    .build();
            client.newCall(request).enqueue(new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public class TransAsyncTask extends AsyncTask<String, Void, String>{

        private final String TAG = TransActivity.class.getSimpleName();

        @Override
        protected String doInBackground(String... strings) {
            //String data;
            StringBuilder sb = null;
            try {
                URL url = new URL(strings[0]);
                InputStream in = url.openStream();
                BufferedReader inbuffer = new BufferedReader(new InputStreamReader(in));
                String inline = inbuffer.readLine();
                sb = new StringBuilder();
                while (inline != null) {
                    sb.append(inline);
                    inline = inbuffer.readLine();
                }
                Log.d(TAG, "onCreate:" + sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
}
