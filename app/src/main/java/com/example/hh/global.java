package com.example.hh;

import android.app.Application;
import android.text.Editable;

public class global extends Application {

        private String pword;
        private String uname;

        public String getuname() {
            return uname;
        }

        public String getpword() {
            return pword;
        }


        public void setuname(String uname) {
            this.uname = uname;
        }



          public void setpword(String pword) {
               this.pword = pword;
            }


       }
