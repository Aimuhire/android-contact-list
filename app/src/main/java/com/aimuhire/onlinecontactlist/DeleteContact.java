package com.aimuhire.onlinecontactlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class DeleteContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Intent in = getIntent();
     final int cid =  in.getIntExtra("cid",0);



        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteContact(cid);
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

    }


    public  void deleteContact(int cid){
       if(!(localDeleteContact())){

           Toast.makeText(getApplicationContext(),"Local delete failed. Delete action aborted.",Toast.LENGTH_SHORT).show();
       return;
       }

try{


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.43.193:3000/delete";
        HashMap data = new HashMap();

        data.put("id",cid);


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                       startActivity(intent);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Online delete failed.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);


                    }
                }
        ){
            //here I want to post data to sever
        };
        queue.add(jsonobj);

}catch (Exception ex){
    System.out.println("##### "+ex);
}
    }

    private boolean localDeleteContact() {
        Intent intent = getIntent();
        DBHelper dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define 'where' part of query.
        String selection = DBContract.ContactList._ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {  String.valueOf( intent.getIntExtra("cid",-1))  };
// Issue SQL statement.
        int deletedRows = db.delete(DBContract.ContactList.TABLE_NAME, selection, selectionArgs);

        if(deletedRows>0)
            return true;



        return false;
    }
}
