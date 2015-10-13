package com.washboardapps.taboozle;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        _Taboo.Library = new LibraryDB(this);
        _Taboo.SafetyQueue = new LinkedList<>();

        //fill the safety queue up with entries that shouldnt be cycled
        _Taboo.Library.GetCycleCards();

        //Initialize current team value
        _Taboo.CurrentTeam = 0;
    }

    //Prevent user from quitting game with back button without confirming first
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(false);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
