package com.example.parkingmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.parkingmanagementsystem.adapters.BookedListAdapter;
import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.classes.database;
import com.example.parkingmanagementsystem.databinding.ActivityBookedSlotBinding;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;
import com.example.parkingmanagementsystem.interfaces.ItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class BookedSlot extends AppCompatActivity  {

    private database db;
    private String email="";
    private BookedListAdapter bookedListAdapter;
    private List<Location.BookedList> list;

    ActivityBookedSlotBinding activityBookedSlotBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_booked_slot);

        setTitle("Reserved Slots");

        activityBookedSlotBinding=ActivityBookedSlotBinding.inflate(getLayoutInflater());
        setContentView(activityBookedSlotBinding.getRoot());





        db=new database(this);

        Cursor c1=db.login_showall();
        if (c1.getCount() != 0){
            if (c1.moveToLast()) {
                 email=c1.getString(2);
            }
        }
        c1.close();

        serverCall(1);
    }

    public void  serverCall(final int nos){

        String[] str=new String[3];
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/booking_process";
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","booked list");
                jsonObject.put("mail",email);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        new Asynch_GET(BookedSlot.this, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1) {
                        Gson gson=new Gson();
                        Type token=new TypeToken<List<Location.BookedList>>(){}.getType();
                         list=gson.fromJson(aVoid,token);
                        bookedListAdapter=new BookedListAdapter(list,email,BookedSlot.this);
                        activityBookedSlotBinding.list.setAdapter(bookedListAdapter);
//                        bookedListAdapter.setClickListener(BookedSlot.this);
                                Log.d("Test",list.toString());
                        if(list.size()==0){
                            Toast.makeText(BookedSlot.this, "No reserved slots found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void exceptioncall(Exception e) {
                Toast.makeText(BookedSlot.this, "Exceptional Issue", Toast.LENGTH_SHORT).show();
            }
        }).execute(str);

    }




}