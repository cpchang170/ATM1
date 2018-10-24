package com.askey.hahow.atm1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_CODEREAD_CONTACTS = 30;
    private static final String TAG = ContactActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED){
            readContacts();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CODEREAD_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODEREAD_CONTACTS){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                readContacts();
            }
        }
    }
    private void readContacts() {
        //getContentResolver extend from Context , so it can be call directory without new a object
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null,null,null,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int hasphonenumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            Log.d(TAG, "readContacts:" + name);
            if (hasphonenumber==1){
                Cursor sub_cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                         null);
                while (sub_cursor.moveToNext()){
                    String phone = sub_cursor.getString(sub_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d(TAG, "readContacts: \t" + phone);
                }
            }

        }

    }
}