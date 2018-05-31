package com.washboardapps.moniker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Screen_End extends Activity {

    private String winText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        i = getIntent();
        winText = i.getStringExtra("winTitle");
        UpdateScreen();

        //update the card database usage fields and reset the local usage fields on success
        _Moniker.Library.UpdateUsageFields();

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
                        Util.ReplacePage(Screen_End.this, Screen_Main.class);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void PlayAgain(View view){
        //reset rounds and scores and go again
        _Moniker.RoundsLeft = _Moniker.NumRounds * _Moniker.Teams.size() * 2;
        Scorekeeper.InitializeTeams(_Moniker.Teams.size());
        _Moniker.CurrentTeam = 0;

        Util.ReplacePage(this, Screen_Ready.class);
    }

    public void Menu(View view){
        Util.ReplacePage(this, Screen_Main.class);
    }

    private void UpdateScreen(){
        TextView text = (TextView) findViewById(R.id.WinningTeam);
        text.setText(this.winText);
    }
}
