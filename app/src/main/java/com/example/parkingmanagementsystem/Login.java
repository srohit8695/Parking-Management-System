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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.classes.database;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText uid,pwd;
    Button submit;
    TextView signup;
    database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uid=findViewById(R.id.userid);
        pwd=findViewById(R.id.password);
        submit=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        db=new database(this);

        Cursor c1=db.login_showall();
        if (c1.getCount() != 0){
            if (c1.moveToLast()) {
                String email=c1.getString(2);
                Intent i1=new Intent(Login.this,MainActivity.class);
                startActivity(i1);
                finish();
            }
        }
        c1.close();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(Login.this,Registration_form.class);
                startActivity(i1);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serverCall(1);

            }
        });

    }

    public void  serverCall(final int nos){

        String[] str=new String[3];
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/Login_process";
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","check login");
                jsonObject.put("mail",uid.getText().toString().trim());
                jsonObject.put("password",pwd.getText().toString().trim());
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        new Asynch_GET(Login.this, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1) {
                        JSONArray jsonArray=new JSONArray(aVoid.trim());
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        if(jsonObject.getString("status").trim().equals("success")){

                            if(db.login_insert(jsonObject.getString("name").trim(),jsonObject.getString("phone").trim(),jsonObject.getString("mail").trim())){
                                Intent i1=new Intent(Login.this,MainActivity.class);
                                startActivity(i1);
                                finish();
                            }else{
                                Toast.makeText(Login.this, "Issue in local database, clear catch and reinstall the app", Toast.LENGTH_SHORT).show();
                            }

                        }else if(jsonObject.getString("status").trim().equals("no data")){
                            Toast.makeText(Login.this, "Login not exist, Please Sign up", Toast.LENGTH_SHORT).show();
                            Intent i1=new Intent(Login.this,Registration_form.class);
                            startActivity(i1);
                            finish();
                        }else if(jsonObject.getString("status").trim().equals("password wrong")){
                            Toast.makeText(Login.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void exceptioncall(Exception e) {
                Toast.makeText(Login.this, "Exceptional Issue", Toast.LENGTH_SHORT).show();
            }
        }).execute(str);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}