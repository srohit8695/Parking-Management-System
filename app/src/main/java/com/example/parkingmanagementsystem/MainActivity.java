package com.example.parkingmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.classes.Parking_location;
import com.example.parkingmanagementsystem.classes.database;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button location;
    private database db;
    private String email="";
    TextView mail,ph,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mail = findViewById(R.id.mail);
        ph = findViewById(R.id.ph);
        name = findViewById(R.id.name);

        location=findViewById(R.id.location);
        db=new database(this);

        Cursor c1=db.login_showall();
        if (c1.getCount() != 0){
            if (c1.moveToLast()) {
                email=c1.getString(2);
            }
        }
        c1.close();

        serverCall(1);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(MainActivity.this,Location.class);
                startActivity(i1);
            }
        });


    }


    public void reservedSlot(View view) {
        Intent i1=new Intent(MainActivity.this,BookedSlot.class);
        startActivity(i1);
    }

    public void parkingHistory(View view) {
        Intent i1=new Intent(MainActivity.this,parkingHistory.class);
        startActivity(i1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.signout:
                try {
                    db.login_delete();
                    Intent i2=new Intent(MainActivity.this,Login.class);
                    startActivity(i2);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void support(View view) {
        Intent i1=new Intent(MainActivity.this,Support.class);
        startActivity(i1);
    }



    public void onBackPressed() {
        try{
            AlertDialog.Builder close_dialog = new AlertDialog.Builder(this);
            close_dialog.setTitle("Exit Application");
            close_dialog.setMessage("Wish to exit application?");
            close_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.finishAffinity(MainActivity.this);
                }
            });
            close_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            close_dialog.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void  serverCall(final int nos){

        String[] str=new String[3];
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/Login_process";
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","basic information");
                jsonObject.put("mail",email);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        new Asynch_GET(MainActivity.this, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1) {

                        JSONArray jsonArray=new JSONArray(aVoid);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        name.setText(jsonObject.getString("name"));
                        ph.setText(jsonObject.getString("phone"));
                        mail.setText(jsonObject.getString("mail"));
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void exceptioncall(Exception e) {
                Toast.makeText(MainActivity.this, "Exceptional Issue", Toast.LENGTH_SHORT).show();
            }
        }).execute(str);

    }


}