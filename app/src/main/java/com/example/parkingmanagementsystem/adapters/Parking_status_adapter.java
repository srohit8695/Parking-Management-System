package com.example.parkingmanagementsystem.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagementsystem.Location;
import com.example.parkingmanagementsystem.MainActivity;
import com.example.parkingmanagementsystem.R;
import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.classes.Parking_location;
import com.example.parkingmanagementsystem.classes.Parking_status;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;
import com.example.parkingmanagementsystem.seats_in_location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public  class Parking_status_adapter extends RecyclerView.Adapter<Parking_status_adapter.MyViewHolder> {

    private ArrayList<Parking_status> dataSet;
    private Context context;
    private String area_id="",from="",to="",positionid="",email="",vehiclenos="";


    public Parking_status_adapter(ArrayList<Parking_status> dataSet, Context context,String area_id,String from,String to,String email) {
        this.dataSet = dataSet;
        this.context=context;
        this.area_id=area_id;
        this.from=from;
        this.to=to;
        this.email=email;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView slot_id,status;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.slot_id =  itemView.findViewById(R.id.slot_id);
            this.status = itemView.findViewById(R.id.status);
            this.img =  itemView.findViewById(R.id.symbol);

        }

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_templet, parent, false);
            myViewHolder = new MyViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            Parking_status parking_status= dataSet.get(position);

            holder.slot_id.setText(parking_status.getSlot_id());

            if(parking_status.getStatus().trim().equals("No")){
                holder.status.setTextColor(Color.rgb(0, 179, 0));
                holder.status.setText("Available");
                holder.img.setImageResource(R.drawable.ic_baseline_check_24);
            }else{
                holder.status.setTextColor(Color.rgb(179, 0, 0));
                holder.status.setText("Not Available");
                holder.img.setImageResource(R.drawable.ic_baseline_close_24);
            }

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.status.getText().toString().trim().equals("Available")) {
                        positionid=""+(position+1);
                        showDialog();
                    }else{
                        Toast.makeText(context, "Slot Booked Already", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void  serverCall(final int nos){

        String[] str=new String[3];
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/booking_process";
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","book slot");
                jsonObject.put("area_id",area_id);
                jsonObject.put("start",from);
                jsonObject.put("end",to);
                jsonObject.put("slot_id","S"+positionid);
                jsonObject.put("mail",email);
                jsonObject.put("reg_no",vehiclenos);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        new Asynch_GET(context, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1) {

                        if(aVoid.trim().equals("success")){
                            Toast.makeText(context, "Booked Successfully", Toast.LENGTH_SHORT).show();
//                            ((Activity)context).finish();
                            Intent i1=new Intent(context, MainActivity.class);
                            context.startActivity(i1);
                        }
                        else if(aVoid.trim().equals("error")){
                            Toast.makeText(context, "Server try after some time", Toast.LENGTH_SHORT).show();
                        }
                        else if(aVoid.trim().equals("slot not available")){
                            Toast.makeText(context, aVoid, Toast.LENGTH_SHORT).show();
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

    public void showDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.vehicle_number);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText vehicleno =dialog.findViewById(R.id.vehicleno);
        Button submit =dialog.findViewById(R.id.button);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!vehicleno.getText().toString().trim().equals("")){
                    vehiclenos=vehicleno.getText().toString();
                    serverCall(1);
                }else{
                    Toast.makeText(context, "Enter Vehicle Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();

    }


}
