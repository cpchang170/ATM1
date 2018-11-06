package com.askey.hahow.atm1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText eduserid;
    private EditText edpwd;
    private CheckBox cb_account_remerber;
    private Intent helloService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSharedPreferences("atm",MODE_PRIVATE)
//                .edit()
//                .putInt("LEVEL",5)
//                .putString("NAME","CP")
//                .commit();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contain_news,NewsFragment.getinstance());
        fragmentTransaction.commit();
        helloService = new Intent(this,HelloService.class);
        //Service will queue in the same thread
        helloService.putExtra("NAME","T1");
        startService(helloService);
        helloService.putExtra("NAME","T2");
        startService(helloService);
        helloService.putExtra("NAME","T3");
        startService(helloService);
        int level = getSharedPreferences("atm",MODE_PRIVATE)
                .getInt("LEVEL",0);
        Log.d(TAG, "onCreate: "+ level);

        eduserid = findViewById(R.id.ed_account);
        edpwd = findViewById(R.id.ed_pwd);
        cb_account_remerber = findViewById(R.id.cb_account_rem_id);
        Boolean account_check =  getSharedPreferences("atm",MODE_PRIVATE)
                .getBoolean("REMEMBER_ACCOUNT",false);
        if (account_check){
            String userid = getSharedPreferences("atm",MODE_PRIVATE)
                    .getString("USERID","");
                    eduserid.setText(userid);
        }
        cb_account_remerber.setChecked(account_check);
        cb_account_remerber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean account_check =  getSharedPreferences("atm",MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_ACCOUNT",isChecked)
                        .commit();
            }
        });

        //TestTask test = new TestTask();
        //test.execute("http://tw.yahoo.com");
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Hello" +intent.getAction());
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver,new IntentFilter(HelloService.ACTION_HELLO_DONE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(helloService);
    }

    public class TestTask extends AsyncTask<String, Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            int data = 0;
            try {
                URL url = new URL(strings[0]);
                data = url.openStream().read();
                publishProgress();
                Log.d(TAG, "onCreate: tw.yahoo.com" + data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d(TAG, "onPostExecute: "+integer);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    public void login(View v) {
        final String useridstr = eduserid.getText().toString();
        final String pwdstr = edpwd.getText().toString();
            FirebaseDatabase.getInstance().getReference("usr").child(useridstr).child("passwd")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String pw = (String) dataSnapshot.getValue();
                        if (pw.equals(pwdstr)) {
                            setResult(RESULT_OK);
                            Boolean account_check = getSharedPreferences("atm",MODE_PRIVATE)
                                    .getBoolean("REMEMBER_ACCOUNT",false);
                            if (account_check) {
                                getSharedPreferences("atm", MODE_PRIVATE)
                                        .edit()
                                        .putString("USERID", useridstr)
                                        .commit();

                            }
                            else {
                                getSharedPreferences("atm", MODE_PRIVATE)
                                        .edit()
                                        .putString("USERID", null)
                                        .commit();
                            }
                            finish();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("login result")
                                    .setMessage("login fail")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //if ("jack".equals(useridstr) && "1234".equals(pwdstr)){
        //    setResult(RESULT_OK);
        //    finish();
        //}

    }
}
