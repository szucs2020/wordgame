package com.washboardapps.moniker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Screen_Play extends Activity {

    private static final String TAG = "DebugTag";

    private final android.os.Handler handler = new android.os.Handler();
    private Timer timer;
    private TextView gameTimer;
    private Card currentCard;
    private double timeVal = (double)_Moniker.RoundTimer + 1.0;
    private double cardStartTime;

    private String packString;
    private String difficultyString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        gameTimer = (TextView) findViewById(R.id.Game_Timer);

        //load difficulty setting
        Context context;
        SharedPreferences sp;
        context = this.getApplicationContext();
        sp = context.getSharedPreferences("options", Context.MODE_PRIVATE);
        int diffLowerBound = sp.getInt(getString(R.string.sp_rangebar_index_left), 0);
        int diffUpperBound = sp.getInt(getString(R.string.sp_rangebar_index_right), 10);

        //make the SQL acceptable dificulty string
        difficultyString = " and Difficulty >= " + diffLowerBound + " and Difficulty <= " + diffUpperBound;

        //make the SQL acceptable pack string
        packString = "(";
        Cursor packs = _Moniker.Library.RawQuery("select * from Packs where Enabled=1");

        while (packs.moveToNext()){
            packString = packString + packs.getString(packs.getColumnIndex("PackID")) + ",";
        }
        packString = packString.substring(0, packString.length() - 1);
        packString = packString + ")";

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateTimer();}
        }, 0, 100);

        //Load next card
        LoadNextCard(packString, difficultyString);
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
                        Intent i = new Intent(Screen_Play.this, Screen_Main.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Button_Correct(View view) {
        Scorekeeper.AddPoint(_Moniker.CurrentTeam);
        currentCard.setCorrect(currentCard.getCorrect() + 1);
        LoadNextCard(packString, difficultyString);
    }

    public void Button_Pass(View view) {
        currentCard.setSkipped(currentCard.getSkipped() + 1);
        LoadNextCard(packString, difficultyString);
        Button b = (Button)view;
        b.setClickable(false);
        b.setTextColor(Color.rgb(100, 100, 100));
        b.setEnabled(false);
    }

    public void Button_Buzz(View view) {
        Scorekeeper.RemovePoint(_Moniker.CurrentTeam);
        currentCard.setBuzzed(currentCard.getBuzzed() + 1);
        LoadNextCard(packString, difficultyString);
    }

    //add the current card to the safety queue and update its db entry
    private void UpdateCardEntry(boolean timeOut){

        if (currentCard != null){

            currentCard.setCycle(2);
            currentCard.setCalled(currentCard.getCalled() + 1);

            if (timeOut){
                currentCard.setTimeOut(currentCard.getTimeOut() + 1);
            } else {
                double cardTotalTime = cardStartTime - timeVal;
                currentCard.setAvgTime((currentCard.getAvgTime() * (1.0-(1.0/currentCard.getCalled()))) + (cardTotalTime * (1.0/currentCard.getCalled())));
            }

            _Moniker.SafetyQueue.add(currentCard.getID());
            _Moniker.Library.UpdateCard(currentCard);
        }
    }

    //update the card entry then load the next one
    private void LoadNextCard(String packString, String difficultyString) {

        UpdateCardEntry(false);

        //remove elements from the safety queue that exceed the size limit
        while (_Moniker.SafetyQueue.size() > _Moniker.QueueSize){
            _Moniker.SafetyQueue.remove();
            _Moniker.Library.UpdateCycleByID(_Moniker.SafetyQueue.element(), 1);
        }

        currentCard = _Moniker.Library.GetNextCard(packString, difficultyString);
        Log.d(TAG, "Card " + currentCard.getID() + ": " + currentCard.getTitle());
        UpdateScreen();
        cardStartTime = timeVal;
    }

    /*
        Updates the text on the screen to match the current card
     */
    private void UpdateScreen() {

        //Updates card elements
        TextView title = (TextView) findViewById(R.id.Word_Title);
        title.setText(currentCard.getTitle());
        TextView word1 = (TextView) findViewById(R.id.Word_1);
        word1.setText(currentCard.getWord1());
        TextView word2 = (TextView) findViewById(R.id.Word_2);
        word2.setText(currentCard.getWord2());
        TextView word3 = (TextView) findViewById(R.id.Word_3);
        word3.setText(currentCard.getWord3());
        TextView word4 = (TextView) findViewById(R.id.Word_4);
        word4.setText(currentCard.getWord4());
        TextView word5 = (TextView) findViewById(R.id.Word_5);
        word5.setText(currentCard.getWord5());

        //updates team score
        TextView scoreView = (TextView) findViewById(R.id.Game_Score);
        scoreView.setText("Score: " + _Moniker.Teams.get(_Moniker.CurrentTeam).getScore());

        //set background colour
        if (_Moniker.CurrentTeam == 0){
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
        } else if (_Moniker.CurrentTeam == 1){
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        } else if (_Moniker.CurrentTeam == 2){
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        }
    }

    private void UpdateTimer() {

        timeVal-= 0.1;
        handler.post(timerRunnable);

        //clock has run out so cancel timer and go back to team ready activity
        if (timeVal <= 0){
            EndRound();
        }
    }

    private void EndRound(){

        UpdateCardEntry(true);
        currentCard = null;

        timer.cancel();
        Scorekeeper.NextTeam();
        _Moniker.RoundsLeft--;
        int winningTeam = 0, tempScore = 0, highestScore = 0;
        boolean isTie = false;
        String winTitle = "";

        if (_Moniker.RoundsLeft == 0){

            Intent i = new Intent(Screen_Play.this, Screen_End.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //find out which team won or if the game is a tie
            for (int x = 0; x < _Moniker.Teams.size(); x++){

                //get highest score
                tempScore = _Moniker.Teams.get(x).getScore();
                if (tempScore > highestScore){
                    highestScore = tempScore;
                    winningTeam = x;
                }

                //compare to other scores for tie
                for (int y = x + 1; y < _Moniker.Teams.size(); y++){
                    if (tempScore == _Moniker.Teams.get(y).getScore()){
                        isTie = true;
                    }
                }
            }

            //send message to the end screen about which team won
            if (!isTie){
                if (winningTeam == 0){
                    winTitle = "Blue Team Wins!";
                } else if (winningTeam == 1){
                    winTitle = "Red Team Wins!";
                } else if (winningTeam == 2){
                    winTitle = "Green Team Wins!";
                } else {
                    winTitle = "Yellow Team Wins!";
                }
            } else {
                winTitle = "Its a tie!";
            }
            i.putExtra("winTitle", winTitle);
            startActivity(i);
        } else {
            this.finish();
        }
    }

    private final Runnable timerRunnable = new Runnable() {
        public void run() {
            gameTimer.setText(String.valueOf((int)timeVal));
        }
    };
}
