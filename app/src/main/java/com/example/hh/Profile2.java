package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

public class Profile2 extends AppCompatActivity {



    final Context context=this;

    public String uname="";
    public String pword="";
    AbstractXMPPConnection connection;


    public static final String EXTRA_NAME="a" ;
    public static final String EXTRA_PWD ="b";



    CardView cardview,cardview2;
    LinearLayout.LayoutParams layoutparams,layoutparams2;

    RelativeLayout.LayoutParams rlayoutparams,rlayoutparams2,rlayoutparams3;
    TextView textview,textview2;

    EditText MessageToSend;

    LinearLayout LinearLayoutView;




    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);



        uname=getIntent().getStringExtra(Profile2.EXTRA_NAME);
        pword=getIntent().getStringExtra(Profile2.EXTRA_PWD);


        //call the indirect  class


        DatabaseAdaptor dba = new DatabaseAdaptor(this);







        RelativeLayout rlayoutView = new RelativeLayout(this);
        rlayoutparams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);


        rlayoutparams2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlayoutparams3=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);


        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();




        ScrollView scrollviewmessage = new ScrollView(context);

        CardView cvmessage=new CardView(context);

        cvmessage.setId(989898);

        rlayoutparams.setMargins(5,5,5,5);

        rlayoutparams.height=5*screenHeight/10;

        cvmessage.setLayoutParams(rlayoutparams);
        cvmessage.setRadius(5);
        cvmessage.setPadding(5, 5, 5, 5);
        cvmessage.setCardBackgroundColor(0xFDFDFDFF);
        cvmessage.setMaxCardElevation(10);


        textview = new TextView(context);
        textview.setLayoutParams(rlayoutparams);
        textview.setText(dba.getCurrentMessage());
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textview.setTextColor(Color.BLACK);
        textview.setPadding(5,5,5,5);
        textview.setGravity(Gravity.TOP);


        cvmessage.addView(textview);



       // scrollviewmessage.addView(cvmessage);


        rlayoutView.addView(cvmessage);






        //add card for message

        rlayoutparams2.height=3*screenHeight/10;
        rlayoutparams2.width=6*screenWidth/10;

        CardView cvpq=new CardView(context);

        cvpq.setId(898989);
        rlayoutparams2.setMargins(25,55,5,5);
        cvpq.setLayoutParams(rlayoutparams2);
        cvpq.setRadius(5);
        cvpq.setPadding(5, 5, 5, 5);
        cvpq.setCardBackgroundColor(Color.WHITE);
        cvpq.setMaxCardElevation(0);



        //messaage to send
        rlayoutparams3.width=rlayoutparams2.width;
        rlayoutparams3.height=rlayoutparams2.height;
        //rlayoutparams3.setMargins(20,10,0,10);


        MessageToSend = new EditText(context);
        MessageToSend.setId(787878);
        MessageToSend.setLayoutParams(rlayoutparams3);
        //textview.setText(dba.getCurrentMessage());
        MessageToSend.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        MessageToSend.setTextColor(Color.BLACK);
        MessageToSend.setPadding(5,5,5,5);
        MessageToSend.setHint("Type your prayer request here");
        MessageToSend.setGravity(Gravity.TOP);


        cvpq.addView(MessageToSend);


        rlayoutparams2.addRule(RelativeLayout.BELOW, 989898);


        rlayoutView.addView(cvpq);




//add button


        Button SubmitPrayerRequest = new Button(this);
        RelativeLayout.LayoutParams ll =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        SubmitPrayerRequest.setId(676767);
        SubmitPrayerRequest.setText("Submit");


        //add submit button
        SubmitPrayerRequest.setBackgroundColor(0xFF6EA470);
        SubmitPrayerRequest.setTextColor(Color.WHITE);
       // SubmitPrayerRequest.setWidth(180);
        SubmitPrayerRequest.setPadding(5,5,5,5);

        ll.setMargins(25,rlayoutparams2.height/3,5,5);



        //ll.addRule(RelativeLayout.BELOW, 787878);
//      ll.addRule(RelativeLayout.ALIGN_PARENT_END);

        SubmitPrayerRequest.setLayoutParams(ll);




        //cvpq.addView(SubmitPrayerRequest);


        ll.addRule(RelativeLayout.RIGHT_OF,898989);
        ll.addRule(RelativeLayout.BELOW,989898);



        rlayoutView.addView(SubmitPrayerRequest);










        setContentView(rlayoutView);




        //get the incoming messages
        new connecttoserver().execute();

        SubmitPrayerRequest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new Profile2.sendmessage().execute();



            }
        });






    }//end of oncreate




    protected void onDestroy(){
        super.onDestroy();


        if(connection.isConnected())
        {
            connection.disconnect();
        }
           uname="";
           pword="";
           Intent intent = new Intent(context, MainActivity.class);
           startActivity(intent);



        finish();
    }//end of onDestroy







    // this will send a prayer request to the preacher
    @SuppressLint("StaticFieldLeak")
    private  class sendmessage extends AsyncTask<Void, Void, Void> {
        String result;

        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Submitting...");
            dialog.show();
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }



        @Override
        protected Void doInBackground(Void... voids) {


            EditText et;

            String toPerson="rony@localhost";
            //EditText et=(EditText)findViewById(R.id.toperson);
            //toPerson=et.getText().toString()+"@localhost";



            //et=(EditText)findViewById(R.id.Messagetosend);
            String PrayerSubject=MessageToSend.getText().toString();
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());


            String Messagetosend="<message><type>two</type><name>"+uname+"</name><time>"+currentDateTimeString+"</time><prayer>"+PrayerSubject+"</prayer></message>";




            /*
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(userName, password)
                    .setServiceName("localhost")
                    .setHost("3.139.90.132")
                    .setPort(5222)
                    .setConnectTimeout(25000)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // Do not disable TLS except for test purposes!
                    .setDebuggerEnabled(true)
                    .setSendPresence(true)
                    .build();



            connection = new XMPPTCPConnection(config);
            ((XMPPTCPConnection) connection).setUseStreamManagement(false);
            try {
                connection.connect().login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/


            if(connection.isAuthenticated()) {


                //send message

                EntityBareJid jid = null;
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                try {
                    jid = JidCreate.entityBareFrom(toPerson);
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                Chat chat = chatManager.createChat(String.valueOf(jid));
                try {
                    chat.sendMessage(Messagetosend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                //clear the chat window



                runOnUiThread(new Runnable(){
                    public void run() {

                        MessageToSend.setText("");

                    }
                    });


            }//send message end




            return null;
        }


    }//end of inner class












    private class connecttoserver extends AsyncTask<Void, Void, Void> {

        String result;
        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Fetching...");
            dialog.show();
        }



        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        protected Void doInBackground(Void... voids) {


            String userName=uname;
            String password=pword;


            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(userName, password)
                    .setServiceName("localhost")
                    .setHost("3.139.90.132")
                    .setPort(5222)
                    .setConnectTimeout(25000)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // Do not disable TLS except for test purposes!
                    .setDebuggerEnabled(true)
                    .setSendPresence(true)
                    .build();



            connection = new XMPPTCPConnection(config);
            ((XMPPTCPConnection) connection).setUseStreamManagement(true);           //changed from false to true
            ((XMPPTCPConnection) connection).setUseStreamManagementResumption(true); //added
            try {
                connection.connect().login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            if(connection.isAuthenticated())
            {
                Log.w("app", "Auth done");
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                chatManager.addChatListener(
                        new ChatManagerListener() {
                            @Override
                            public void chatCreated(Chat chat, boolean createdLocally)
                            {
                                chat.addMessageListener(new ChatMessageListener()
                                {
                                    @Override
                                    public void processMessage(Chat chat, Message message) {

                                        runOnUiThread(new Runnable(){
                                            public void run()
                                            {


                                                //check whether incoming message is an xml type format
                                                //if it is an xml its roster request meant for the admin.
                                                //otherwise  store it and display it

                                                int valid_xml=CheckforXML(message.getBody());
                                                if(valid_xml==0)
                                                {
                                                    Profile2.StringHandler sh=new Profile2.StringHandler();
                                                    sh.set(message.getBody());

                                                    //TextView tv=findViewById(R.id.incomingmessage);
                                                    textview.setText(message.getBody());







                                                }

                                            }

                                        });



                                    }
                                });

                                Log.w("app", chat.toString());
                            }
                        });
            }//end of received message
            else
            {// authentication error
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

            }


            return null;
        }//end of do in background





        int CheckforXML(String ms)
        {
            try {
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(ms)));
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }


    }//end of inner class





    private class StringHandler  {

        String  message;
        public void set(String ms)
        {
            message=ms;
            DatabaseAdaptor db = new DatabaseAdaptor(context);
            db.truncatetable();
            db.addMessage(message,"");
        }
        public String get()
        {

            return message;
        }

    }//end of inner class








}//end of outer class