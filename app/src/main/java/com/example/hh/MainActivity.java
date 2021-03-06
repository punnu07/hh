package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;



import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NAME="a" ;
    public static final String EXTRA_PWD ="b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context=this;


        TextView tv= findViewById(R.id.notamember);
        tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(context, register.class);

                startActivity(intent);
            }
        });





        Button login_button= findViewById(R.id.Login);
        login_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                final ToggleButton tB = (ToggleButton) findViewById(R.id.role);

                if(tB.isChecked()){
                    //preacher case
                    Intent intent = new Intent(context, preacher.class);
                    startActivity(intent);


                }
                else
                {

                    Intent intent = new Intent(context, profile.class);
                    String uname;
                    String pword;
                    EditText et=findViewById(R.id.login_username);
                    uname=et.getText().toString();

                    EditText etp=findViewById(R.id.login_password);
                    pword=etp.getText().toString();

                    intent.putExtra(EXTRA_PWD, pword);
                    intent.putExtra(EXTRA_NAME, uname);
                    startActivity(intent);
                }
                //Button is OFF

            }//end of click
        });



        //toggle button handler


    }//end of on create








    private void login(String userName, String password) throws Exception {

/*




        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(userName, password)
                .setServiceName("localhost")
                .setHost("3.139.90.132")
                .setPort(5280)
                .setSecurityMode(SecurityMode.disabled) // Do not disable TLS except for test purposes!
                .setDebuggerEnabled(true)
                .build();




                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(userName, password)
                .setXmppDomain("servicename")
                .setHost("3.139.90.132")
                .setPort(5280)
                .setSecurityMode(SecurityMode.disabled) // Do not disable TLS except for test purposes!
                .setDebuggerEnabled(true)
                .build();





        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        connection.connect().login();


        try {
            connection.connect();
            if(connection.isConnected()) {
                Log.w("app", "conn done");
            }
            connection.login();

            if(connection.isAuthenticated()) {
                Log.w("app", "Auth done");
            }
        }
        catch (Exception e) {
            Log.w("app", e.toString());
        }


        */
    }








    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {


            String userName="joseph";
            String password="josephpreacher";


            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(userName, password)
                    .setServiceName("localhost")
                    .setHost("3.139.90.132")
                    .setPort(5222)
                    .setConnectTimeout(25000)
                    .setSecurityMode(SecurityMode.disabled) // Do not disable TLS except for test purposes!
                    .setDebuggerEnabled(true)
                    .build();




//start from here
            /*
            DomainBareJid serviceName = null;
            try
            {
                serviceName = JidCreate.domainBareFrom("localhost");
            }
            catch (XmppStringprepException e)
            {
                e.printStackTrace();
            }

            try {
                InetAddress addr = InetAddress.getByName("3.139.90.132");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            XMPPTCPConnectionConfiguration config = null;
            try {
                config = XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(userName, password)
                        .setXmppDomain(JidCreate.domainBareFrom("localhost"))
                        .setHostAddress(InetAddress.getByName("3.139.90.132"))
                        .setPort(5280)
                        .setSecurityMode(SecurityMode.disabled) // Do not disable TLS except for test purposes!
                        //.setDebuggerEnabled(true)
                        .build();
            } catch (XmppStringprepException | UnknownHostException e) {
                e.printStackTrace();
            }
             */

//comment upto here


            AbstractXMPPConnection connection = new XMPPTCPConnection(config);
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

            /*
            try {
                connection.connect();
                if(connection.isConnected()) {
                    Log.w("app", "conn done");
                }
                connection.login();

                if(connection.isAuthenticated()) {
                    Log.w("app", "Auth done");
                }
            }
            catch (Exception e) {
                Log.w("app", e.toString());
            }

            */


            /*
            AccountManager accountManager = AccountManager.getInstance(connection);
            try {
                if (accountManager.supportsAccountCreation()) {
                    accountManager.sensitiveOperationOverInsecureConnection(true);
                    accountManager.createAccount("jacob", "jacobpreacher");

                }
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
             */



            /*
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            EntityBareJid jid = null;
            try {
                jid = JidCreate.entityBareFrom("rony@3.139.90.132");
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
            Chat chat = chatManager.createChat(String.valueOf(jid));
            try {
                chat.sendMessage("hello");
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }


             */



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
                                        System.out.println("Received message: "
                                                + (message != null ? message.getBody() : "NULL"));
                                    }
                                });

                                Log.w("app", chat.toString());
                            }
                        });
            }//end of received message


            //send message

            EntityBareJid jid = null;
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            try {
                jid = JidCreate.entityBareFrom("rony@localhost");
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
            Chat chat = chatManager.createChat(String.valueOf(jid));
            try {
                chat.sendMessage("hello");
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }


            //send message end








            return null;
        }


    }//end of inner class




} //end of outer class