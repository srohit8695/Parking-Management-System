package com.example.parkingmanagementsystem.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {


    String login_table="LOGIN_TABLE";
    String name="NAME";
    String phone="PHONE_NOS";
    String email="EMAIL";



    public database(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+login_table+" (NAME TEXT,PHONE_NOS TEXT,EMAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+login_table);
        onCreate(db);
    }


    public boolean login_insert(String unam,String enos,String dept1){
        SQLiteDatabase dbms=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(name,unam);
        cv.put(phone,enos);
        cv.put(email,dept1);
        if(dbms.insert(login_table,null,cv)!=-1){
            return true;
        }
        return false;
    }





    public void login_delete(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+login_table);
    }



    public Cursor login_showall(){
        SQLiteDatabase dblt = this.getWritableDatabase();
        String s2 = "SELECT * FROM " + login_table;
        Cursor cr = dblt.rawQuery(s2, null);
        return cr;
    }



}


