package com.aimuhire.onlinecontactlist;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder>{
    private static final String TAG = "ContactsRVAdapter";
private List<Contact> cList = new ArrayList();

private Context mContext;
    public ContactsRVAdapter(Context ccontext, ArrayList<Contact> contactsList) {
  this.cList=contactsList;
  this.mContext=ccontext;
    }


    @Override
    public ContactsRVAdapter.ViewHolder onCreateViewHolder( ViewGroup parentView, int position) {
View view = LayoutInflater.from(parentView.getContext()).inflate(R.layout.contact_listitem,parentView,false);
ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
    //    holder.tvImageName.setText("Holla ma mathr "+ i);



//holder.ivImage.setImageResource(mContext.getResources().getIdentifier("com.aimuhire.onlinecontactlist:drawable/i1",null,null));


        holder.tvImageName.setText(cList.get(position).getName());

         holder.tvImageNumber.setText(cList.get(position).getPhone());

         holder.tvImageNumber.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(Intent.ACTION_CALL);

                 intent.setData(Uri.parse("tel:" + cList.get(position).getPhone()));

    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(mContext,"Permission denied!",Toast.LENGTH_SHORT).show();
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
        } else {
            // Permission has already been granted
            mContext.startActivity(intent);

        }
    }


             }
         });
    //   holder.ivImage.setImageResource();

         System.out.println(holder.getAdapterPosition());
        holder.btndelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeleteContact.class);
                intent.putExtra("cid", cList.get(position).id);
                mContext.startActivity(intent);


            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UpdateContact.class);
                intent.putExtra("cid", cList.get(position).id);

                mContext.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {

        return cList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImage;
        TextView tvImageName;
        TextView tvImageNumber;
        Button btndelete;
        Button btnEdit;
        ConstraintLayout imageParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                    ivImage = itemView.findViewById(R.id.cimage);
                    tvImageName = itemView.findViewById(R.id.cname);
                    tvImageNumber = itemView.findViewById(R.id.cnumber);
                    imageParentLayout = itemView.findViewById(R.id.cparentlayout);
            btndelete = itemView.findViewById(R.id.btndelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);



        }
    }

    public void setFilter(List<Contact> newList){
        Log.d(TAG, "setFilter: called");
        cList.clear();
        cList.addAll(newList);
        notifyDataSetChanged();
    }

}
