package com.askey.hahow.atm1;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransActivity extends AppCompatActivity {
    private static final String TAG = TransActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        recyclerView = findViewById(R.id.transrecycler);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //new TransAsyncTask().execute("http://atm201605.appspot.com/h");
        OkHttpClient client = new OkHttpClient();
        //RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder()
                .url("http://atm201605.appspot.com/h")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string().toString();////final String json = response.body().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJSON(json);
                        //parseGSON(json);
                        Log.d(TAG, "onResponse: " + json);
                   }
                });

            }
        });

    }

    private void parseJSON(String json) {
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                jsonObjectList.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        recyclerView.setAdapter(new FunctionAdapter(this));
        recyclerView.setAdapter(new TransAdapter(jsonObjectList));
    }

    public class TransAsyncTask extends AsyncTask<String, Void, String>{

        private final String TAG = TransActivity.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

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
    public class  TransAdapter extends RecyclerView.Adapter<TransAdapter.TransHolder>{
        private final List<JSONObject> jsonObjectList;

        public TransAdapter(List<JSONObject> jsonObjectList) {
            this.jsonObjectList = jsonObjectList;
        }

        @NonNull
        @Override
        public TransHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_trans,viewGroup,false);
            return new TransHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransHolder transHolder, int i) {
            try {
                transHolder.item_date.setText(jsonObjectList.get(i).getString("date"));
                transHolder.item_amount.setText(String.valueOf(jsonObjectList.get(i).getInt("amount")));
                transHolder.item_type.setText(String.valueOf(jsonObjectList.get(i).getInt("type")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonObjectList.size();
        }

        public class TransHolder extends RecyclerView.ViewHolder{
            //TextView item_account;
            TextView item_date;
            TextView item_amount;
            TextView item_type;
            public TransHolder(@NonNull View itemView) {
                super(itemView);
                item_type = itemView.findViewById(R.id.item_type);
                item_date = itemView.findViewById(R.id.item_date);
                item_amount = itemView.findViewById(R.id.item_type);

            }
        }
    }
}
