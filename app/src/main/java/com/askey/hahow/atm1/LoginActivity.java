package com.askey.hahow.atm1;

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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText eduserid;
    private EditText edpwd;
    private CheckBox cb_account_remerber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSharedPreferences("atm",MODE_PRIVATE)
//                .edit()
//                .putInt("LEVEL",5)
//                .putString("NAME","CP")
//                .commit();
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
