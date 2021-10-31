package com.example.parkingmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration_form extends AppCompatActivity {
    EditText con_pwd,pwd,email,phone_nos,name;
    String con_pwdstr,pwdstr,emailstr="",phone_nosstr,namestr,phoneotp="",emailotp="";
    Button submit;
    boolean chkphone_nos=false,chkemail=false,chkpwd=false;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        con_pwd=findViewById(R.id.con_pwd);
        pwd=findViewById(R.id.pwd);
        email=findViewById(R.id.email);
        phone_nos=findViewById(R.id.phone_nos);
        name=findViewById(R.id.name);
        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chkphone_nos) {
                    if(chk(name.getText().toString().trim())){
                        if(chk(phone_nos.getText().toString().trim())&&phone_nos.getText().toString().trim().length()==10){

                            serverCall(1);

                        }else{
                            Toast.makeText(Registration_form.this, "Check Phone number must have 10 digits.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if(chkphone_nos&&(!chkemail)){
                    if(chk(email.getText().toString().trim())&&email.getText().toString().contains("@")){

                        serverCall(2);

                    }else{
                        Toast.makeText(Registration_form.this, "Check email is correct.", Toast.LENGTH_SHORT).show();
                    }
                }

                if (chkphone_nos&&chkemail&&(!chkpwd)) {
                    if(chk(pwd.getText().toString().trim())&&chkpwd(pwd.getText().toString().trim())){
                        if(con_pwd.getText().toString().trim().equals(pwd.getText().toString())){
                            serverCall(5);
                        }else{
                            Toast.makeText(Registration_form.this, "Check Both password are correct.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }




            }
        });




    }


    public boolean chk(String str){
        if(str.trim().equals("")){
            Toast.makeText(this, "Values should not be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*if(str.contains("")){
            Toast.makeText(this, "Values should not be Contain .", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }

    public boolean chkpwd(String str){
        boolean upper=false,lower=false,special=false,nos=false;

        for(int i=0;i<str.length();i++){
            char chr=str.charAt(i);
            int chk=(int)chr;

            if((chk>=33&&chk<=47)||(chk>=58&&chk<=64)){
                special=true;
            }
            else if(chk>=48&&chk<=57){
                nos=true;
            }
            else if(chk>=65&&chk<=90){
                upper=true;
            }
            else if(chk>=97&&chk<=122){
                lower=true;
            }

            if(special&&nos&&upper&&lower){
                return true;
            }

        }

        if(!special){
            Toast.makeText(this, "Password must have special characters like !,@,#,&,$..", Toast.LENGTH_SHORT).show();
        }
        if(!nos){
            Toast.makeText(this, "Password must have numeric values like 0,1,2....8,9", Toast.LENGTH_SHORT).show();
        }
        if(!lower){
            Toast.makeText(this, "Password must have lower case like a,b,c..y,z", Toast.LENGTH_SHORT).show();
        }
        if(!upper){
            Toast.makeText(this, "Password must have upper case like A,B...Y,Z", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void  serverCall(final int nos){

        String[] str=new String[3];
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/Login_process";
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","generate sms otp");
                jsonObject.put("phone",phone_nos.getText().toString().trim());
                jsonObject.put("name",name.getText().toString().trim());
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(nos==2){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","generate mail otp");
                jsonObject.put("mail",email.getText().toString().trim());
                jsonObject.put("name",name.getText().toString().trim());
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(nos==3){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","verify otp");
                jsonObject.put("mail",phone_nos.getText().toString().trim());
                jsonObject.put("name",name.getText().toString().trim());
                jsonObject.put("otp",phoneotp);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(nos==4){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","verify otp");
                jsonObject.put("mail",email.getText().toString().trim());
                jsonObject.put("name",name.getText().toString().trim());
                jsonObject.put("otp",emailotp);
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(nos==5){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","create new login");
                jsonObject.put("name",name.getText().toString().trim());
                jsonObject.put("mail",email.getText().toString().trim());
                jsonObject.put("phone",phone_nos.getText().toString().trim());
                jsonObject.put("password",pwd.getText().toString().trim());
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        new Asynch_GET(Registration_form.this, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1) {

                        if(aVoid.trim().equals("success")){
                            Toast.makeText(Registration_form.this, "OTP Generated to phone", Toast.LENGTH_SHORT).show();
                            showPhoneDialog();
                        }else if(aVoid.trim().equals("wrong")){

                        }else if(aVoid.trim().equals("error")){
                            Toast.makeText(Registration_form.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                        }


                    }else if(nos==2){

                        if(aVoid.trim().equals("success")){
                            Toast.makeText(Registration_form.this, "OTP Generated to email", Toast.LENGTH_SHORT).show();
                            showEmailDialog();
                        }else if(aVoid.trim().equals("wrong")){

                        }else if(aVoid.trim().equals("error")){
                            Toast.makeText(Registration_form.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                        }

                    }else if(nos==3){

                        if(aVoid.trim().equals("success")){
                            Toast.makeText(Registration_form.this, "Verified", Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                            chkphone_nos=true;
                            phone_nosstr=phone_nos.getText().toString().trim();
                            namestr=name.getText().toString().trim();
                            email.setVisibility(View.VISIBLE);
                        }else if(aVoid.trim().equals("error")){
                            Toast.makeText(Registration_form.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }else if(nos==4){
                        if(aVoid.trim().equals("success")){
                            Toast.makeText(Registration_form.this, "Verified", Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                            emailstr=email.getText().toString().trim();
                        chkemail=true;
                        pwd.setVisibility(View.VISIBLE);
                        con_pwd.setVisibility(View.VISIBLE);

                        }else if(aVoid.trim().equals("error")){
                            Toast.makeText(Registration_form.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }else if(nos==5){
                        if(aVoid.trim().equals("success")){
                            Toast.makeText(Registration_form.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent i1=new Intent(Registration_form.this,Login.class);
                            startActivity(i1);
                        }else if(aVoid.trim().equals("already exist")){
                            Toast.makeText(Registration_form.this, "Already Login Exist,Please do sign up again", Toast.LENGTH_SHORT).show();
                        }else if(aVoid.trim().equals("error")){
                            Toast.makeText(Registration_form.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void exceptioncall(Exception e) {
                Toast.makeText(Registration_form.this, "Exceptional Issue", Toast.LENGTH_SHORT).show();
            }
        }).execute(str);

    }


    public void showPhoneDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText text =  dialog.findViewById(R.id.edit1);
        Button chk=dialog.findViewById(R.id.chk);
        TextView topic=dialog.findViewById(R.id.topic);
        topic.setText("Enter OPT send to your registered phone number.");

        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text.getText().toString().trim().equals("")) {
                    phoneotp=text.getText().toString().trim();
                    serverCall(3);

                } else {
                    Toast.makeText(Registration_form.this, "Enter otp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

    public void showEmailDialog() {
        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText text =  dialog.findViewById(R.id.edit1);
        Button chk=dialog.findViewById(R.id.chk);
        TextView topic=dialog.findViewById(R.id.topic);
        topic.setText("Enter OPT send to your registered Email Address.");

        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text.getText().toString().trim().equals("")) {
                    emailotp=text.getText().toString().trim();
                    serverCall(4);
                } else {
                    Toast.makeText(Registration_form.this, "Enter otp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

}