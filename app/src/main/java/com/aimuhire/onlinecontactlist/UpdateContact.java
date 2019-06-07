package com.aimuhire.onlinecontactlist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class UpdateContact extends AppCompatActivity {


    EditText etPhone;
    EditText etNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_update_contact);
        try{
            DBHelper dbHelper = new DBHelper(this);

            SQLiteDatabase db = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
            String[] projection = {
                    BaseColumns._ID,
                    DBContract.ContactList.COLUMN_NAME_NAMES,
                    DBContract.ContactList.COLUMN_NAME_PHONE,
            };

// Filter results WHERE "title" = 'My Title'
            String selection =    DBContract.ContactList._ID + " = ?";
            String[] selectionArgs = { String.valueOf( intent.getIntExtra("cid",-1))};



            Cursor cursor = db.query(
                    DBContract.ContactList.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            cursor.moveToFirst();

            String names= cursor.getString(1);
            System.out.println("#### names"+names);
            String phone = cursor.getString(2);
            System.out.println("#### phone"+phone);
            etNames = findViewById(R.id.etNamesU);

            etPhone = findViewById(R.id.etPhoneU);


            etNames.setText(names);
            etPhone.setText(phone);
        }catch(Exception ex){

            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_SHORT).show();

        }

    }

    public void updateContact(View view){
        Intent intent = getIntent();
        String cid =String.valueOf( intent.getIntExtra("cid",-1));

        if(!localUpdateContact(cid)){

            Toast.makeText(getApplicationContext(),"Local update failed. Update action aborted.",Toast.LENGTH_SHORT).show();

            return;
        }


Contact ct = new Contact(etNames.getText().toString(),etPhone.getText().toString(),Integer.parseInt(cid));

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.43.193:3000/update";
        System.out.println("ct.getName() "+ct.getName());
        HashMap data = new HashMap();
        data.put("name",ct.getName()) ;
        data.put("phone",ct.getPhone());
        data.put("id",ct.getId());


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Contact updated online.",Toast.LENGTH_SHORT).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error updating online",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);



                    }
                }
        ){
            //here I want to post data to sever
        };
        queue.add(jsonobj);
    }


    public boolean localUpdateContact(String cid) {
        DBHelper dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
etPhone = findViewById(R.id.etPhoneU);
etNames =findViewById(R.id.etNamesU);

// New value for one column
        String title = "MyNewTitle";
        ContentValues values = new ContentValues();
        values.put(DBContract.ContactList.COLUMN_NAME_NAMES,etNames.getText().toString());
        values.put(DBContract.ContactList.COLUMN_NAME_PHONE, etPhone.getText().toString());
// Which row to update, based on the title
        String selection = (DBContract.ContactList._ID + " LIKE ?");
        String[] selectionArgs = { cid };

        int count = db.update(
                DBContract.ContactList.TABLE_NAME,
                values,
                selection,
                selectionArgs);

     if (count>0)
         return true;

return false;
    }



}
