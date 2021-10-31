package com.example.parkingmanagementsystem.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parkingmanagementsystem.interfaces.AsynchCallback;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Asynch_GET extends AsyncTask<String,Void,String> {
    Context context;
    AsynchCallback call;//Its interface and object is not
    Exception exception;
    URL url;
    HttpURLConnection httpURLConnection;
    BufferedReader br;
    StringBuilder sb;
    String s1="";
    ProgressDialog pd;

    public Asynch_GET(Context context, AsynchCallback call){//Constructor: To assign context and make sure that it is connected with its parent activity
        this.context=context;
        this.call=call;//make sure that it is connected with its activity
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setTitle("PMS");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    @Override
    protected String doInBackground(String... strings) {
//        strings[0] is URL
//        strings[1] is JSON to string data
        exception=null;

        try {
            url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod(strings[1].trim());
            if (strings[1].trim().equals("GET")) {
            } else if(strings[1].trim().equals("POST")) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "text/plain");
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(strings[2]);
                wr.flush();
                wr.close();
            }

            br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            sb = new StringBuilder();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                exception=e;
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            s1 = sb.toString().replaceAll("\\\\", "");
            s1 = s1.substring(1,s1.length()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s1;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        try{
            super.onPostExecute(aVoid);
            pd.dismiss();
            if(call!=null){//make sure that it is connected with the activity
                if(exception==null){
                    call.responsecall(aVoid);
                }else{
                    call.exceptioncall(exception);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



}

