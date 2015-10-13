package com.washboardapps.taboozle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Screen_Rounds extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounds);
        _Taboo.NumRounds = 3;
        updateScreen();
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
                        Intent i = new Intent(Screen_Rounds.this, Screen_Main.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Plus(View view){
        if (_Taboo.NumRounds < 10){
            _Taboo.NumRounds++;
            updateScreen();
        }
    }

    public void Minus(View view){
        if (_Taboo.NumRounds > 1){
            _Taboo.NumRounds--;
            updateScreen();
        }
    }

    public void StartGame(View view){
        _Taboo.RoundsLeft = _Taboo.NumRounds * _Taboo.Teams.size() * 2;
        Intent i = new Intent(this, Screen_Ready.class);
        startActivity(i);
    }

    //updates points/rounds text box
    private void updateScreen(){
        TextView text = (TextView) findViewById(R.id.Rounds);
        text.setText(String.valueOf(_Taboo.NumRounds));
    }
}
