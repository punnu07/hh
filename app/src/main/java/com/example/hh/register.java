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
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;

public class register extends AppCompatActivity {


    public static final String EXTRA_NAME="a" ;
    public static final String EXTRA_PWD ="b";

    final Context context=this;


    public String uname;
    public String pword;
    public boolean IsAccountCreated=false;




    AbstractXMPPConnection connection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);






        //handler for register button
        Button register_button= findViewById(R.id.Register);
        register_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new createnewuser().execute();






                if(IsAccountCreated)
                {
                    Intent intent = new Intent(context, profile.class);


                    String username;
                    String password;
                    EditText et=findViewById(R.id.register_username);
                    username=et.getText().toString();

                    EditText etp=findViewById(R.id.register_password);
                    password=etp.getText().toString();

                    intent.putExtra(EXTRA_PWD, password);
                    intent.putExtra(EXTRA_NAME, username);
                    startActivity(intent);
                }

            }
        });




    }//end  of oncreate




    //starting of inner class
    private class createnewuser extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {


            EditText et=(EditText)findViewById(R.id.register_username);
            uname=et.getText().toString();

            EditText etp=(EditText)findViewById(R.id.register_password);
            pword=etp.getText().toString();





            String Username="admin";
            String Password="punnooseak";





//login as admin
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(Username, Password)
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
                //create new user
                AccountManager accountManager = AccountManager.getInstance(connection);
                try {
                    if (accountManager.supportsAccountCreation()) {
                        accountManager.sensitiveOperationOverInsecureConnection(true);
                        accountManager.createAccount(uname,pword);

                    }
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                //at this point disconnect
                connection.disconnect();
                IsAccountCreated=true;







            }
            else
            {// authentication error
                //Intent intent = new Intent(context, MainActivity.class);
                //startActivity(intent);

            }


            return null;
        }


    }//end of inner class




}//end of outer class



