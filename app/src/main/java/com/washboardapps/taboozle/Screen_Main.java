package com.washboardapps.taboozle;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import java.util.LinkedList;

public class Screen_Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set global context
        _Taboo.GlobalContext = getApplicationContext();

        //Initialize database and safety queue
        if (_Taboo.Library == null){
            _Taboo.Library = new LibraryDB(this);
            _Taboo.SafetyQueue = new LinkedList<>();
        }

        //Initialize current team value
        _Taboo.CurrentTeam = 0;
    }

    //Starts game activity
    public void Start_Pregame(View view) {
        Intent i = new Intent(this, Screen_Teams.class);
        startActivity(i);
    }

    //Starts options activity
    public void Start_Options(View view) {
        Intent i = new Intent(this, Screen_Options.class);
        startActivity(i);
    }

    //Starts options activity
    public void Start_Cards(View view) {
        Intent i = new Intent(this, Screen_Cards.class);
        startActivity(i);
    }

    //Starts options activity
    public void Start_Help(View view) {
        Intent i = new Intent(this, Screen_Help.class);
        startActivity(i);
    }
}