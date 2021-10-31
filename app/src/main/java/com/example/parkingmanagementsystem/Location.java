package com.example.parkingmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parkingmanagementsystem.adapters.Parking_status_adapter;
import com.example.parkingmanagementsystem.classes.Asynch_GET;
import com.example.parkingmanagementsystem.classes.Parking_location;
import com.example.parkingmanagementsystem.classes.database;
import com.example.parkingmanagementsystem.interfaces.AsynchCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Location extends AppCompatActivity {

    ListView list;
    ArrayList<Parking_location> parking_location;
    ArrayAdapter<String> arrayAdapter;
    String location_id="",email="",fromdatestr="",todatestr="",fromtimestr="",totimestr="",location_name="";
    database db;
    Parking_status_adapter parking_status_adapter;
    String inputFormat = "HH:mm:ss";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setTitle("Location");

        try {
            list=findViewById(R.id.list);

            arrayAdapter=new ArrayAdapter<>(Location.this,android.R.layout.simple_list_item_1);
            list.setAdapter(arrayAdapter);

            serverCall(1);
            db=new database(this);

            Cursor c1=db.login_showall();
            if (c1.getCount() != 0){
                if (c1.moveToLast()) {
                    email=c1.getString(2);
                }
            }
            c1.close();





        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void  serverCall(final int nos){

        String[] str=new String[3];
        str[0]="https://xcmg-api.schwingcloud.com/XCMG.svc/booking_process";
        str[1]="POST";
        if(nos==1){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","get area name");
                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(nos==2){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("json_type","get slot status");
                jsonObject.put("mail",email);
                jsonObject.put("area_id",location_id);
                jsonObject.put("start",fromdatestr+" "+fromtimestr);
                jsonObject.put("end",todatestr+" "+totimestr);

                str[2]=jsonObject.toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        new Asynch_GET(Location.this, new AsynchCallback() {
            @Override
            public void responsecall(String aVoid) {

                try{
                    if (nos==1) {

                        Gson gson=new Gson();
                        Type type=new TypeToken<ArrayList<Parking_location>>(){}.getType();
                        parking_location=gson.fromJson(aVoid,type);

                        for(Parking_location pl:parking_location){
                            arrayAdapter.add(pl.getName());
                        }

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    Parking_location pl=parking_location.get(position);
                                    location_id=pl.getId();
                                    location_name=pl.getName();
                                    showEmailDialog();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });



                    }else if(nos==2){

                        Intent i1=new Intent(Location.this,seats_in_location.class);
                        i1.putExtra("data",aVoid);
                        i1.putExtra("area_id",location_id);
                        i1.putExtra("from",fromdatestr+" "+fromtimestr);
                        i1.putExtra("to",todatestr+" "+totimestr);
                        i1.putExtra("area",location_name);
                        startActivity(i1);

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void exceptioncall(Exception e) {
                Toast.makeText(Location.this, "Exceptional Issue", Toast.LENGTH_SHORT).show();
            }
        }).execute(str);

    }

    public void showEmailDialog() {
       Dialog  dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.date_time);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView fromtime =dialog.findViewById(R.id.fromtime);
        TextView fromdate =dialog.findViewById(R.id.fromdate);
        TextView todate =dialog.findViewById(R.id.todate);
        TextView totime =dialog.findViewById(R.id.totime);
        Button submit =dialog.findViewById(R.id.submit);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 int mYear, mMonth, mDay, mHour, mMinute;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Location.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromdatestr=( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                fromdate.setText(fromdatestr);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mYear, mMonth, mDay, mHour, mMinute;
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Location.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                fromtimestr=(hourOfDay + ":" + minute+":00");
                                fromtime.setText(fromtimestr);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });

        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mYear, mMonth, mDay, mHour, mMinute;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Location.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                todatestr=( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                todate.setText(todatestr);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();


            }
        });

        totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int mYear, mMonth, mDay, mHour, mMinute;
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Location.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                totimestr=(hourOfDay + ":" + minute+":00");
                                totime.setText(totimestr);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();


            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fromdatestr.trim().equals("")){
                    if(!fromtimestr.trim().equals("")){
                        if(!todatestr.trim().equals("")){
                            if(!totimestr.trim().equals("")){

                                Calendar now = Calendar.getInstance();
                                int hour = now.get(Calendar.HOUR_OF_DAY);
                                int minute = now.get(Calendar.MINUTE);
                                int second = now.get(Calendar.SECOND);

                                Date date = parseDate(hour + ":" + minute+":"+second);
                                date = parseDate(hour + ":" + minute+":"+second);
                                Date dateCompareOne = parseDate(fromtimestr);
                                Date dateCompareTwo = parseDate(totimestr);

//                                if(dateCompareOne.after( date ) && dateCompareTwo.after(dateCompareOne)){
                                    serverCall(2);
//                                }else {
//                                    Toast.makeText(Location.this, "Check timings", Toast.LENGTH_SHORT).show();
//                                }

                            }
                            else{
                                Toast.makeText(Location.this, "Select To time", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Location.this, "Select To date", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Location.this, "Select From time", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Location.this, "Select From date", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();

    }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    // this BookedList is placed here by some mistake
    // on changing the location of BookedList from this location is making some issue in binding
    //so it is placed here
    public static class BookedList {
        private String rcd_id;
        private String area_id;
        private String area_name;
        private String slot_no;
        private String reg_no;
        private String st_time;
        private String et_time;
        private String reg_time;
        private String open_btn;
        private String close_btn;

        @Override
        public String toString() {
            return "BookedList{" +
                    "rcd_id='" + rcd_id + '\'' +
                    ", area_id='" + area_id + '\'' +
                    ", area_name='" + area_name + '\'' +
                    ", slot_no='" + slot_no + '\'' +
                    ", reg_no='" + reg_no + '\'' +
                    ", st_time='" + st_time + '\'' +
                    ", et_time='" + et_time + '\'' +
                    ", reg_time='" + reg_time + '\'' +
                    ", open_btn='" + open_btn + '\'' +
                    ", close_btn='" + close_btn + '\'' +
                    '}';
        }

        public String getOpen_btn() {
            return open_btn;
        }

        public void setOpen_btn(String open_btn) {
            this.open_btn = open_btn;
        }

        public String getClose_btn() {
            return close_btn;
        }

        public void setClose_btn(String close_btn) {
            this.close_btn = close_btn;
        }

        public String getRcd_id() {
            return rcd_id;
        }

        public void setRcd_id(String rcd_id) {
            this.rcd_id = rcd_id;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getSlot_no() {
            return slot_no;
        }

        public void setSlot_no(String slot_no) {
            this.slot_no = slot_no;
        }

        public String getReg_no() {
            return reg_no;
        }

        public void setReg_no(String reg_no) {
            this.reg_no = reg_no;
        }

        public String getSt_time() {
            return st_time;
        }

        public void setSt_time(String st_time) {
            this.st_time = st_time;
        }

        public String getEt_time() {
            return et_time;
        }

        public void setEt_time(String et_time) {
            this.et_time = et_time;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }

    }
}