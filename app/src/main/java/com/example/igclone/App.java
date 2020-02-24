package com.example.igclone;

import com.parse.Parse;
import com.parse.ParseACL;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("423c4cb7f79ca647488f2be37cfbeb1acea52061")
                // if defined
                .clientKey("6f533b454e7ea31d6973409857eff39b4d392bbd")
                .server("http://3.136.25.229:80/parse/")
                .build()
        );

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}