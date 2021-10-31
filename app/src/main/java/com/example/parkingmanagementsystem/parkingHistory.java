package com.example.parkingmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.parkingmanagementsystem.adapters.BookedListAdapter;
import com.example.parkingmanagementsystem.adapters.HistoryAdapter;
import com.example.parkingmanagementsystem.adapters.Parking_status_adapter;
import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.classes.database;
import com.example.parkingmanagementsystem.databinding.ActivityBookedSlotBinding;
import com.example.parkingmanagementsystem.databinding.ActivityParkingHistoryBinding;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;
import com.example.parkingmanagementsystem.model.History;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class parkingHistory extends AppCompatActivity {

    private database db;
    private String email="";
    private List<History> list;
    ActivityParkingHistoryBinding activityParkingHistoryBinding;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Parking History");

        activityParkingHistoryBinding= ActivityParkingHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityParkingHistoryBinding.getRoot());

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
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/booking_process";//https://xcmg-api.schwingcloud.com/XCMG.svc///https://xcmg-api.schwingcloud.com/XCMG.svc
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","history");
                jsonObject.put("mail",email);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        new Asynch_GET(parkingHistory.this, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1) {
                        Gson gson=new Gson();
                        Type token=new TypeToken<List<History>>(){}.getType();
                        list=gson.fromJson(aVoid,token);
                        Log.d("Test",list.toString());
                        historyAdapter=new HistoryAdapter(list);
                        activityParkingHistoryBinding.list.setAdapter(historyAdapter);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void exceptioncall(Exception e) {
                Toast.makeText(parkingHistory.this, "Exceptional Issue", Toast.LENGTH_SHORT).show();
            }
        }).execute(str);

    }

}