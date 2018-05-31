package com.washboardapps.moniker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Screen_Ready extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        UpdateScreen();
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateScreen();
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
                        Util.ReplacePage(Screen_Ready.this, Screen_Main.class);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Start_Round(View view){
        Util.PushPage(this, Screen_Play.class);
    }

    private void UpdateScreen(){

        //get current team an print that to the title
        TextView title = (TextView) findViewById(R.id.Team_Start);

        //Set title and background colour by current team playing
        if (_Moniker.CurrentTeam == 0){
            title.setText("Blue Team Start!");
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
        } else if (_Moniker.CurrentTeam == 1){
            title.setText("Red Team Start!");
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        } else if (_Moniker.CurrentTeam == 2){
            title.setText("Green Team Start!");
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        } else {
            title.setText("Yellow Team Start!");
            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        }
    }
}
