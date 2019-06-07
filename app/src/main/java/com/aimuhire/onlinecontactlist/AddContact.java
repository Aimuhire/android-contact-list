package com.aimuhire.onlinecontactlist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddContact extends AppCompatActivity {
   private String name,phone;
   private int id;
    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);



    }


    public boolean postContact(View view){

        EditText etName =findViewById(R.id.etnames);
        EditText etPhone =  findViewById(R.id.etnumber);


        name = etName.getText().toString();
        phone = etPhone.getText().toString();
        id= randomWithRange(2000,50000);

        Contact ct = new Contact(name,phone,id);
int cidLocal=(int)saveContactToDb(ct);
ct.setId(cidLocal);
       if(cidLocal!=-1){
     //    Toast.makeText(this,"Contact saved Locally.",Toast.LENGTH_SHORT).show();
       }else{
           Toast.makeText(this,"Error saving Locally.",Toast.LENGTH_SHORT).show();

       }
        RequestQueue queue = Volley.newRequestQueue(this);
       // do not use LOCALHOST use the computer's IP address. YOu should connect your phone/emulator to the same network(NO DATA REQUIRED)
        String url ="http://192.168.43.193:3000/contact";
System.out.println("ct.getName() "+ct.getName());
        HashMap data = new HashMap();
        data.put("name",ct.getName().toString()) ;
        data.put("phone",ct.getPhone());
        data.put("id",ct.getId());


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Changes added online.",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error adding online",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);


                    }
                }
        ){
            //here I want to post data to sever
        };
        queue.add(jsonobj);

        return false;
    }

    private long saveContactToDb(Contact ct) {

        DBHelper dbHelper = new DBHelper(this);

// Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.ContactList.COLUMN_NAME_NAMES, ct.getName());
        values.put(DBContract.ContactList.COLUMN_NAME_PHONE, ct.getPhone());
        values.put(DBContract.ContactList.COLUMN_NAME_IMAGE, ct.getImage());
// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBContract.ContactList.TABLE_NAME, null, values);

        if(newRowId>0){
            return newRowId;
        }
        return -1;
    }

}
