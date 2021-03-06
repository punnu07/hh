package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class profile extends AppCompatActivity {


    final Context context=this;


    public String uname;
    public String pword;


    AbstractXMPPConnection connection;

    //global gd=new global();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uname=getIntent().getStringExtra(MainActivity.EXTRA_NAME);
        pword=getIntent().getStringExtra(MainActivity.EXTRA_PWD);

        new connecttoserver().execute();





        Button send_button= findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new sendmessage().execute();



            }
        });





    }









    private class sendmessage extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {











            String toPerson;
            EditText et=(EditText)findViewById(R.id.toperson);
            toPerson=et.getText().toString()+"@localhost";



            et=(EditText)findViewById(R.id.Messagetosend);
            String MessageToSend=et.getText().toString();


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


                et.setText("");
            }//send message end








            return null;
        }


    }//end of inner class

















    private class connecttoserver extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {



            String userName=uname.trim();
            String password=pword.trim();





            //TextView tv=findViewById(R.id.incomingmessage);
            //tv.setText(message.getBody());
          //   tv.setText(uname+" 1 "+pword);




//            String userName="rony";
 //           String password="ronypreacher";





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
                                                  TextView tv=findViewById(R.id.incomingmessage);
                                                  tv.setText(message.getBody());
                                                 // tv.setText(userName);
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
        }


    }//end of inner class


}