package com.example.parkingmanagementsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.parkingmanagementsystem.adapters.Parking_status_adapter;
import com.example.parkingmanagementsystem.adapters.RankingAdapter;
import com.example.parkingmanagementsystem.classes.Parking_status;
import com.example.parkingmanagementsystem.classes.database;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class seats_in_location extends AppCompatActivity {
    RecyclerView list;
    Gson gson;
    Parking_status_adapter parking_status_adapter;
    String area_id="",from="",to="",email="";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats_in_location);

        setTitle(getIntent().getStringExtra("area"));

        try {
            list=findViewById(R.id.list);
            gson=new Gson();

            database db=new database(this);

            Cursor c1=db.login_showall();
            if (c1.getCount() != 0){
                if (c1.moveToLast()) {
                    email=c1.getString(2);
                }
            }
            c1.close();

            area_id=getIntent().getStringExtra("area_id");
            from=getIntent().getStringExtra("from");
            to=getIntent().getStringExtra("to");

            Type listtype=new TypeToken<List<Parking_status>>(){}.getType();
            ArrayList<Parking_status> parkinglist=gson.fromJson(String.valueOf(getIntent().getStringExtra("data")),listtype);
            parking_status_adapter=new Parking_status_adapter(parkinglist,this,area_id,from,to,email);
            list.setAdapter(parking_status_adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}