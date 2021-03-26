package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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



    CardView cardview,cardview2;
    LinearLayout.LayoutParams layoutparams;
    TextView textview,textview2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preacher_process_prayer_requests);


        uname=getIntent().getStringExtra(PreacherProcessPrayerRequests.EXTRA_NAME);
        pword=getIntent().getStringExtra(PreacherProcessPrayerRequests.EXTRA_PWD);

        layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );



        DatabaseAdaptorPrayerRequest dbp = new DatabaseAdaptorPrayerRequest(context);

        ArrayList<String> RequestTimes=dbp.getAllTimeRequests();
        ArrayList<String> RequestNames=dbp.getAllNameRequests();
        ArrayList<String> RequestPrayerMatters=dbp.getAllPrayerMatterRequests();





        String displayText="";


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



        //LinearLayoutView.addView(DisplayStringArray);

        CardView []cv=new CardView[RequestNames.size()];

        ScrollView sv=new ScrollView(context);

        for (int i=0; i<RequestNames.size();i++){


            displayText="\t"+RequestNames.get(i)+" : "+RequestPrayerMatters.get(i);


            cv[i] = new CardView(context);

            layoutparams.setMargins(5,5,5,5);
            cv[i].setLayoutParams(layoutparams);
            cv[i].setRadius(5);
            cv[i].setPadding(5, 5, 5, 5);
            cv[i].setCardBackgroundColor(0xFDFDFDFF);
            cv[i].setMaxCardElevation(10);


            textview = new TextView(context);
            textview.setLayoutParams(layoutparams);
            textview.setText(displayText);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textview.setTextColor(Color.BLACK);
            textview.setPadding(55,55,55,55);
            textview.setGravity(Gravity.CENTER);

            cv[i].addView(textview);

            LinearLayoutView.addView(cv[i]);

        }


        sv.addView(LinearLayoutView);

        DisplayStringArray.append("\n\n");




        //add the button to the lienar view
        RequestsViewClear.setBackgroundColor(0xFF6EA470);
        RequestsViewClear.setTextColor(Color.WHITE);
        RequestsViewClear.setWidth(280);
        RequestsViewClear.setPadding(25,25,25,25);

        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)RequestsViewClear.getLayoutParams();
        ll.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        ll.setMargins(0,55,0,0);


        RequestsViewClear.setLayoutParams(ll);

        LinearLayoutView.addView(RequestsViewClear);


//end of button

        //ScrollView sv=new ScrollView(this);
        //sv.addView(LinearLayoutView);
        setContentView(sv);




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