package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class preacher extends AppCompatActivity {


    final Context context=this;
    public String uname="";
    public String pword="";


    AbstractXMPPConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preacher);

        uname=getIntent().getStringExtra(MainActivity.EXTRA_NAME);
        pword=getIntent().getStringExtra(MainActivity.EXTRA_PWD);


        //check whether preacher logged correctly
        if(!uname.equals("rony")||!pword.equals("ronypreacher"))
        {

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);

        }


        //listen to server for any xmls
        new preacher.listentoserver().execute();



        //handler for post button
        Button post_button= findViewById(R.id.Post);
        post_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


            }
        });


        //handler for logout
        Button preacherlogout= findViewById(R.id.preacherlogoutbutton);
        preacherlogout.setOnClickListener(new View.OnClickListener(){
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




    }//end of oncreate function








    // listen to the server and  display any messages
    private class listentoserver extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
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
                                                TextView tv=findViewById(R.id.incomingmessage);

                                                //check whether incoming message is an xml type format
                                                int valid_xml=CheckforXML(message.getBody());
                                                if(valid_xml==1)
                                                {
                                                    //tv.setText(message.getBody());
                                                    //send a roster request to the new client
                                                    String xmlstring=message.getBody();


                                                    DocumentBuilder builder = null;
                                                    try {
                                                        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                                                    } catch (ParserConfigurationException e) {
                                                        e.printStackTrace();
                                                    }
                                                    InputSource src = new InputSource();
                                                    src.setCharacterStream(new StringReader(xmlstring));
                                                    Document doc = null;
                                                    try {
                                                        doc = builder.parse(src);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (SAXException e) {
                                                        e.printStackTrace();
                                                    }
                                                    String newusername = doc.getElementsByTagName("name").item(0).getTextContent();

                                                    String jid=newusername+"@localhost";


                                                    Toast.makeText(getApplicationContext(),  newusername,  Toast.LENGTH_LONG);




                                                  Roster roster = Roster.getInstanceFor(connection);
                                                  roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                                                    try {
                                                        roster.createEntry(newusername, jid, null);
                                                    } catch (SmackException.NotLoggedInException e) {
                                                        e.printStackTrace();
                                                    } catch (SmackException.NoResponseException e) {
                                                        e.printStackTrace();
                                                    } catch (XMPPException.XMPPErrorException e) {
                                                        e.printStackTrace();
                                                    } catch (SmackException.NotConnectedException e) {
                                                        e.printStackTrace();
                                                    }








                                                }//end of valid xml


                                            }//end of run

                                        });



                                    }
                                });

                                Log.w("app", chat.toString());
                            }// end of chat created
                        });
            }//end of received message


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




        private Document convertStringToDocument(String xmlStr) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try
            {
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
                return doc;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }




    }//end of inner class






}//end of class


