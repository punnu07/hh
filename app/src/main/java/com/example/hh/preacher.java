package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.HashMap;

public class preacher extends AppCompatActivity {



    final Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preacher);






        Button post_button= findViewById(R.id.Post);
        post_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){









            }
        });


    }


}


