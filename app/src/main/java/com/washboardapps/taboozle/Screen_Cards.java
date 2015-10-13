package com.washboardapps.taboozle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Screen_Cards extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
    }

    //Prevent user from quitting game with back button without confirming first
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Screen_Cards.this, Screen_Main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
