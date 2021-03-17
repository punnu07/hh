package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.util.ArrayList;

public class PreacherProcessPrayerRequests extends AppCompatActivity {


    final Context context=this;

    public String uname="";
    public String pword="";
    AbstractXMPPConnection connection;


    public static final String EXTRA_NAME="a" ;
    public static final String EXTRA_PWD ="b";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preacher_process_prayer_requests);


        uname=getIntent().getStringExtra(PreacherProcessPrayerRequests.EXTRA_NAME);
        pword=getIntent().getStringExtra(PreacherProcessPrayerRequests.EXTRA_PWD);


        DatabaseAdaptorPrayerRequest dbp = new DatabaseAdaptorPrayerRequest(context);

        ArrayList<String> RequestTimes=dbp.getAllTimeRequests();
        ArrayList<String> RequestNames=dbp.getAllNameRequests();
        ArrayList<String> RequestPrayerMatters=dbp.getAllPrayerMatterRequests();



        //TextView tv=findViewById(R.id.PrayerRequestsView);

        //tv.setText(prayerrequests.);




        Button RequestsViewClear = new Button(this);
        RequestsViewClear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        RequestsViewClear.setText("Clear Requests");
        // RequestsViewClear.setId(Integer.parseInt("RequestsViewClearButton"));

        LinearLayout LinearLayoutView = new LinearLayout(this);

        LinearLayoutView.setOrientation(LinearLayout.VERTICAL);


        TextView DisplayStringArray = new TextView(this);

        DisplayStringArray.setGravity(Gravity.CENTER);
        DisplayStringArray.setTextSize(18);
        DisplayStringArray.append("\n\nPrayer Requests\n\n");
        DisplayStringArray.setTextSize(14);
        LinearLayoutView.addView(DisplayStringArray);
        for (int i=0; i<RequestNames.size();i++){
            DisplayStringArray.append("\t");
            DisplayStringArray.append(RequestNames.get(i));
            DisplayStringArray.append(" : ");
            DisplayStringArray.append(RequestPrayerMatters.get(i));
            DisplayStringArray.append("\n");
        }

        DisplayStringArray.append("\n\n");

        //add the button to the lienar view

        RequestsViewClear.setBackgroundColor(0xFF6EA470);
        RequestsViewClear.setTextColor(Color.WHITE);
        RequestsViewClear.setWidth(280);

        LinearLayoutView.addView(RequestsViewClear);



        //LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)RequestsViewClear.getLayoutParams();
        ll.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        RequestsViewClear.setLayoutParams(ll);


        setContentView(LinearLayoutView);




        RequestsViewClear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                dbp.truncatetable();


                Intent intent = new Intent(context, PreacherLandingPage.class);

                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);

                startActivity(intent);

            }//end of click
        });





    }//end of oncreate






}//end of class