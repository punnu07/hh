package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;



import java.io.StringReader;
import java.io.StringWriter;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class profile extends AppCompatActivity {


    final Context context=this;



    public static final String EXTRA_NAME="a" ;
    public static final String EXTRA_PWD ="b";

    public String uname;
    public String pword;


    AbstractXMPPConnection connection;

    //global gd=new global();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        TextView textView = (TextView) findViewById(R.id.Send);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        textView = (TextView) findViewById(R.id.incoming_heading);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



        uname=getIntent().getStringExtra(profile.EXTRA_NAME);
        pword=getIntent().getStringExtra(profile.EXTRA_PWD);



        //listening to the server
              new connecttoserver().execute();


        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */

        TextView tv=findViewById(R.id.incomingmessage);
        DatabaseAdaptor dba = new DatabaseAdaptor(this);
       tv.setText(dba.getCurrentMessage());






        //sending message back to
        Button send_button= findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new sendmessage().execute();



            }
        });


        //handler for logout button
        Button logout_button= findViewById(R.id.logoutbutton);
        logout_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

              if(connection.isConnected())
              {
                  connection.disconnect();
                  uname="";
                  pword="";
                  Intent intent = new Intent(context, MainActivity.class);
                  startActivity(intent);

              }



            }
        });





    }








// this will send a prayer request to the preacher
    private class sendmessage extends AsyncTask<Void, Void, Void> {
        String result;

    final ProgressDialog dialog = new ProgressDialog(context);



    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage(" ");
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



            et=(EditText)findViewById(R.id.Messagetosend);
            String PrayerSubject=et.getText().toString();
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());


            String MessageToSend="<message><type>two</type><name>"+uname+"</name><time>"+currentDateTimeString+"</time><prayer>"+PrayerSubject+"</prayer></message>";




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
                    chat.sendMessage(MessageToSend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                //clear the chat window
                et.setText("");
            }//send message end








            return null;
        }


    }//end of inner class
















// listen to the server and  display any messages
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
                                                     StringHandler sh=new StringHandler();
                                                     sh.set(message.getBody());

                                                      TextView tv=findViewById(R.id.incomingmessage);
                                                      tv.setText(message.getBody());
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

    }




}//end of outer class