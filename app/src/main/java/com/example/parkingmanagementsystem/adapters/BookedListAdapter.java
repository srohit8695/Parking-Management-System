package com.example.parkingmanagementsystem.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.adapters.ToolbarBindingAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.example.parkingmanagementsystem.BookedSlot;
import com.example.parkingmanagementsystem.Location;
import com.example.parkingmanagementsystem.Login;
import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.databinding.BookedSlotTempletBinding;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;
import com.example.parkingmanagementsystem.interfaces.ItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class BookedListAdapter extends RecyclerView.Adapter<BookedListAdapter.MyViewHolder> {

    List<Location.BookedList> data;
    private String mailid="",rcd_id="";
    Context context;


    public BookedListAdapter(List<Location.BookedList> list,String mailid,Context context){
        this.data=list;
        this.mailid=mailid;
        this.context=context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        BookedSlotTempletBinding bookedSlotTempletBinding;
        public MyViewHolder(BookedSlotTempletBinding bookedSlotTempletBinding) {
            super(bookedSlotTempletBinding.getRoot());
                this.bookedSlotTempletBinding=bookedSlotTempletBinding;

                bookedSlotTempletBinding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("click",mailid+" "+bookedSlotTempletBinding.rcdid.getText());
                        rcd_id=bookedSlotTempletBinding.rcdid.getText().toString();


                        AlertDialog.Builder close_dialog = new AlertDialog.Builder(context);
                        close_dialog.setMessage("Wish to cancel booked slot?");
                        close_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                serverCall(1);
                            }
                        });
                        close_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        close_dialog.show();
                    }
                });

            bookedSlotTempletBinding.gatein.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rcd_id=bookedSlotTempletBinding.rcdid.getText().toString();
//                    serverCall(2);

                    AlertDialog.Builder close_dialog = new AlertDialog.Builder(context);
                    close_dialog.setMessage("Wish to open gate?");
                    close_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            serverCall(2);
                        }
                    });
                    close_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    close_dialog.show();

                }
            });

            bookedSlotTempletBinding.gateout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rcd_id=bookedSlotTempletBinding.rcdid.getText().toString();
//                    serverCall(3);

                    AlertDialog.Builder close_dialog = new AlertDialog.Builder(context);
                    close_dialog.setMessage("Wish to close gate?");
                    close_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            serverCall(3);
                        }
                    });
                    close_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    close_dialog.show();

                }
            });

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        BookedSlotTempletBinding bookedSlotTempletBinding=BookedSlotTempletBinding.inflate(layoutInflater,parent,false);
        return new MyViewHolder(bookedSlotTempletBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Location.BookedList bookedList=data.get(position);
        holder.bookedSlotTempletBinding.setBookeddata(bookedList);
        holder.bookedSlotTempletBinding.executePendingBindings();
        if(bookedList.getOpen_btn().trim().equals("yes")){
            holder.bookedSlotTempletBinding.gatein.setEnabled(true);
        }else{
            holder.bookedSlotTempletBinding.gatein.setEnabled(false);
        }

        if(bookedList.getClose_btn().trim().equals("yes")){
            holder.bookedSlotTempletBinding.gateout.setEnabled(true);
        }else{
            holder.bookedSlotTempletBinding.gateout.setEnabled(false);
        }

        if(bookedList.getOpen_btn().trim().equals("no")&&bookedList.getClose_btn().trim().equals("yes")){
            holder.bookedSlotTempletBinding.cancel.setEnabled(false);
        }else{
            holder.bookedSlotTempletBinding.cancel.setEnabled(true);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void  serverCall(final int nos){

        String[] str=new String[3];
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/booking_process";
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","cancel the slot");
                jsonObject.put("mail",mailid);
                jsonObject.put("rcd_id",rcd_id);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(nos==2){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","gate open");
                jsonObject.put("mail",mailid);
                jsonObject.put("rcd_id",rcd_id);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(nos==3){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","gate close");
                jsonObject.put("mail",mailid);
                jsonObject.put("rcd_id",rcd_id);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        new Asynch_GET(context, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1||nos==2||nos==3) {

                        Toast.makeText(context, aVoid, Toast.LENGTH_SHORT).show();
                        if(aVoid.trim().equals("cancelled")||aVoid.trim().equals("success")){
                            ((Activity)context).finish();
                        }

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void exceptioncall(Exception e) {
                Toast.makeText(context, "Exceptional Issue", Toast.LENGTH_SHORT).show();
            }
        }).execute(str);

    }

}
