package com.washboardapps.taboozle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Screen_Help extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    //Prevent user from quitting game with back button without confirming first
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Screen_Help.this, Screen_Main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}