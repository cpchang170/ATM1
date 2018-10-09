package com.askey.hahow.atm1;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText eduserid;
    private EditText edpwd;

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
        String userid = getSharedPreferences("atm",MODE_PRIVATE)
                .getString("USERID","");
        eduserid = findViewById(R.id.ed_account);
        edpwd = findViewById(R.id.ed_pwd);
        eduserid.setText(userid);
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
                            getSharedPreferences("atm",MODE_PRIVATE)
                                    .edit()
                                    .putString("USERID",useridstr)
                                    .commit();
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
