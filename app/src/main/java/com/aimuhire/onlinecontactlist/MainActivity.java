package com.aimuhire.onlinecontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

public class MainActivity extends AppCompatActivity {
    private List<Contact> cList = new ArrayList<>() ;
    private List<Contact> copyList = new ArrayList<>() ;

    ContactsRVAdapter adapter = new ContactsRVAdapter(this, (ArrayList<Contact>) cList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
initContacts();

RecyclerView rv = findViewById(R.id.rvcontacts);
  rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem search=menu.findItem(R.id.btnappSearch);

        //this 2 lines
        SearchView searchView= (SearchView) search.getActionView();
        search(searchView);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnAddContact:
                addContact();
                return true;
            case R.id.btnappSearch:
                searchContact();
                return true;
            case R.id.btnRefresh:
                initContacts();
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("@###### q "+cList.size());
                newText=newText.toLowerCase();
                ArrayList<Contact> newList=new ArrayList<>();
                for (Contact ct : cList){
                    String name=ct.getName().toLowerCase();
                    String phone=ct.getPhone().toLowerCase();
                    if (name.contains(newText)||phone.contains(newText)){
                        newList.add(ct);
                     }
                }

                 if(newList.size()==0){


                     Toast.makeText(getApplicationContext(),"No contact found",Toast.LENGTH_SHORT).show();
                     cList.clear();
                             cList.addAll(copyList);
                     adapter.setFilter(copyList);




                }else{
                    adapter.setFilter(newList);
                }

                return true;
            }
        });
    }

    private void addContact() {
        Intent intent = new Intent(getApplicationContext(),AddContact.class);
        startActivity(intent);
    }

    private void searchContact() {
    }

    public void initContacts(){


final ArrayList cList = new ArrayList<>();


        RequestQueue queue = Volley.newRequestQueue( this);
        String url ="http://192.168.43.193:3000/contacts";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


// Gets the data repository in write mode

                        JSONArray mJsonArray = null;
                        try {
                            mJsonArray = new JSONArray(response);

                            final JSONArray finalMJsonArray = mJsonArray;
                                         DBHelper dbHelper = new DBHelper(getApplicationContext());
                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                         try{
                             db.execSQL("DROP TABLE " + DBContract.ContactList.TABLE_NAME);
                             dbHelper.onCreate(db);
                         }catch (Exception e) {
                             System.out.print("#### "+e.toString());

                         }
copyList.clear();

                                            for(int i = 0; i< finalMJsonArray.length(); i++){
                                                JSONObject jsObject = null;
                                                try {
                                                    jsObject = finalMJsonArray.getJSONObject(i);


                                                Contact ct = new Contact(jsObject.getString("name"),jsObject.getString("phone"),jsObject.getInt("id"));
                                               saveContactToDb(ct);
                                                //         Toast.makeText(getApplicationContext(),"Db name "+ct.getName(),Toast.LENGTH_SHORT).show();
                                                copyList.add(ct);
                                                } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            }
                                            DisplayLocalContacts();
                                            return;




                        } catch (JSONException e) {
                            e.printStackTrace();
                            DisplayLocalContacts();

                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
DisplayLocalContacts();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    private void DisplayLocalContacts() {

        List<Contact> mList = new ArrayList<>() ;

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from contacts",null);

        cList.clear();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                int cid = Integer.parseInt(cursor.getString(0));

                Contact ct = new Contact(name,phone,cid);

                mList.add(ct);

                cursor.moveToNext();
            }
        }

       adapter.setFilter(mList);

    }

    private long saveContactToDb(Contact ct) {


}
