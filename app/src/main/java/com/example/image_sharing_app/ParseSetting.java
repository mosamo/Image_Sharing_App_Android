package com.example.image_sharing_app;

import com.parse.Parse;
import com.parse.ParseACL;

import android.app.Application;
import android.util.Xml;

import java.io.File;


public class ParseSetting extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    
        Parse.initialize(new Parse.Configuration.Builder(this)
                // add your own AppID, ClientKey, Server
                .applicationId(getResources().getString(R.string.app_id_parse))
                .clientKey(getResources().getString(R.string.client_key_parse))
                .server(getResources().getString(R.string.server_parse))
                .build()
        );
    
        // ParseUser.enableAutomaticUser(); // Allows Guests
        // AnonymousUser =/= AutomaticUser

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}