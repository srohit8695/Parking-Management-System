package com.example.parkingmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Support extends AppCompatActivity {
    private static final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        setTitle("Support & Help");

    }


    public void calls(View view) {

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
            if (!hasPermissions(Support.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(Support.this, PERMISSIONS, REQUEST );
            } else {
                makeCall();
            }
        } else {
            makeCall();
        }


    }

    public void email(View view) {

        /*Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "","testsuser12345@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));*/

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"testsuser12345@gmail.com"} );
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Support: Needed help for Parking Management System.");
        startActivity(Intent.createChooser(shareIntent, "Parking Management System "));

    }

    public void makeCall()
    {
        Uri u = Uri.parse("tel:" + "9751969282");
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        startActivity(i);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(Support.this, "Need permission to make call", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}