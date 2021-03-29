package com.example.hh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class PreacherLandingPage extends AppCompatActivity {

    final Context context=this;
    public String uname="";
    public String pword="";
    AbstractXMPPConnection connection;


    public static final String EXTRA_NAME="a" ;
    public static final String EXTRA_PWD ="b";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preacher_landing_page);


        uname=getIntent().getStringExtra(PreacherLandingPage.EXTRA_NAME);
        pword=getIntent().getStringExtra(PreacherLandingPage.EXTRA_PWD);


        //check whether preacher logged correctly
        if(!uname.equals("rony")||!pword.equals("houseofhope"))
        {

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);

        }

         //listen for any initiation message
        new listenForInitiationMessage().execute();



        //handler for post message button
        Button post_button= findViewById(R.id.PreacherLandingPagePostButton);
        post_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){



                    Intent intent = new Intent(context, preacher.class);

                    intent.putExtra(EXTRA_PWD, pword);
                    intent.putExtra(EXTRA_NAME, uname);

                    startActivity(intent);




            }//end of click
        });



        //prayer requests
        Button pq_button= findViewById(R.id.PreacherLandingPagePrayerRequestsButton);
        pq_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                if(connection.isConnected()) {
                    connection.disconnect();
                }

                    Intent intent = new Intent(context, PreacherProcessPrayerRequests.class);

                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);

                startActivity(intent);




            }//end of click
        });








        //handler for logout button
        Button preacherlandingpagelogout= findViewById(R.id.PreacherLandingPageLogoutButton);
        preacherlandingpagelogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){



                if(connection.isConnected())
                {
                    connection.disconnect();
                }


                    uname="";
                    pword="";
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);





            }
        });




    }//end of oncreate




/*

    protected void onDestroy(){
        super.onDestroy();

        if(connection.isConnected())
        {
            connection.disconnect();
            uname="";
            pword="";
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }

        finish();
    }


*/




    // listen to the server
    //this handles  initiation messages from the clients
    private class listenForInitiationMessage extends AsyncTask<Void, Void, Void> {
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
                                                int valid_xml=CheckforXML(message.getBody());
                                                if(valid_xml==1)
                                                {

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

                                                    //check if the type
                                                        String type=doc.getElementsByTagName("type").item(0).getTextContent();
                                                        String newusername = doc.getElementsByTagName("name").item(0).getTextContent();
                                                        String jid = newusername + "@localhost";

                                                     //initiation message
                                                    if(type.equals("one"))
                                                    {
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

                                                        Log.d("User Initiaition", newusername);
                                                    }//end of type =1

                                                    //prayer requests
                                                    if(type.equals("two")) {
                                                        //put the requests in a db

                                                        String m_time=doc.getElementsByTagName("time").item(0).getTextContent();
                                                        String m_name = doc.getElementsByTagName("name").item(0).getTextContent();
                                                        String m_matter=doc.getElementsByTagName("prayer").item(0).getTextContent();
                                                        InsertPrayerRequestsIntoDB ip=new InsertPrayerRequestsIntoDB();
                                                        ip.insertrequest(m_name, m_time, m_matter);
                                                        Log.d("Prayer Request",m_matter);
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









        private class InsertPrayerRequestsIntoDB  {

            String  name;
            String time;
            String prayerrequestmatter;

            public void insertrequest(String n, String t, String p)
            {
                name=n;
                time=t;
                prayerrequestmatter=p;

                DatabaseAdaptorPrayerRequest dbp = new DatabaseAdaptorPrayerRequest(context);
                Log.d("DB created", "done");
                dbp.addPrayerRequest(name, time,prayerrequestmatter);

            }

        }





    }//end of inner class






}//end of outer class