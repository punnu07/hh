package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RegistrationSuccess extends AppCompatActivity {




    final Context context=this;

    public String uname="";
    public String pword="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_success);
    }//end of oncreate


    protected void onDestroy(){
        super.onDestroy();

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);

        finish();
    }//end




}