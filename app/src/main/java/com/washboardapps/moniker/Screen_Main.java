package com.washboardapps.moniker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.util.LinkedList;

public class Screen_Main extends Activity {

    private Context context;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set global context
        _Moniker.GlobalContext = getApplicationContext();

        //Initialize database and update it
        _Moniker.Library = new LibraryDB(this);
        UpdateCardsTask task = new UpdateCardsTask();
        task.execute();

        //load some settings
        context = this.getApplicationContext();
        sp = context.getSharedPreferences("options", Context.MODE_PRIVATE);
        _Moniker.RoundTimer = sp.getInt(getString(R.string.sp_options_round_timer), 60);

        //Initialize current team value
        _Moniker.CurrentTeam = 0;
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
        Util.PushPage(this, Screen_Teams.class);
    }

    //Starts options activity
    public void Start_Options(View view) {
        Util.PushPage(this, Screen_Options.class);
    }

    //Starts options activity
    public void Start_Cards(View view) {
        Util.PushPage(this, Screen_Cards.class);
    }

    //Starts options activity
    public void Start_Help(View view) {
        Util.PushPage(this, Screen_Help.class);
    }

    private class UpdateCardsTask extends AsyncTask<String, Void, Boolean> {

        View mainLayout = (View)findViewById(R.id.MainLinearLayout);
        View relativeLayout = (View)findViewById(R.id.relativelayout_progress);

        @Override
        protected void onPreExecute() {
            mainLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(final String... args) {

            //update local database
            return _Moniker.Library.UpdateLocalDatabases();
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            //go back to main menu
            mainLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

            //fill the safety queue up with entries that shouldn't be cycled
            _Moniker.SafetyQueue = new LinkedList<>();
            _Moniker.Library.GetCycleCards();
        }
    }
}
