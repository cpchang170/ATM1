package com.askey.hahow.atm1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_CODEREAD_CONTACTS = 30;
    private static final String TAG = ContactActivity.class.getSimpleName();
    private List<Contacts> contactsList;
    private ContactAdapter contactAdapter;

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
        contactAdapter = new ContactAdapter(contactsList);
        RecyclerView recyclerView = findViewById(R.id.contactview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        recyclerView.setAdapter(new FunctionAdapter(this));
        recyclerView.setAdapter(contactAdapter);
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
        contactsList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int hasphonenumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            Contacts contact = new Contacts(id,name);
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
                    contact.getPhone().add(phone);
                }
            }

            contactsList.add(contact);

        }

    }

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{

        private final List<Contacts> contacts;

        public ContactAdapter(List<Contacts> contactsList) {
            contacts = contactsList;
        }

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2,parent,false);
            return new ContactHolder(view);
            //return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            Contacts contact= contacts.get(position);
            holder.item_name.setText(contact.getName());
            StringBuilder sb = new StringBuilder();
            for (String s : contact.getPhone()) {
                sb.append(s);
                sb.append(" ");
            }
            holder.item_phone.setText(sb.toString());
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public class ContactHolder extends RecyclerView.ViewHolder {
            TextView item_name;
            TextView item_phone;

            public ContactHolder(View itemView) {
                super(itemView);
                item_name = itemView.findViewById(android.R.id.text1);
                item_phone = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
