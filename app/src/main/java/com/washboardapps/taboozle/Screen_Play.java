package com.washboardapps.taboozle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Screen_Play extends Activity {

    final android.os.Handler handler = new android.os.Handler();
    private Timer timer;
    private Card currentCard;
    private int timeVal = 60 + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {UpdateTimer();}
        }, 0, 1000);

        //Load next card
        LoadNextCard();
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
        Scorekeeper.AddPoint(_Taboo.CurrentTeam);
        currentCard.setCorrect(currentCard.getCorrect() + 1);
        LoadNextCard();
    }

    public void Button_Pass(View view) {
        currentCard.setSkipped(currentCard.getSkipped() + 1);
        LoadNextCard();
    }

    public void Button_Buzz(View view) {
        Scorekeeper.RemovePoint(_Taboo.CurrentTeam);
        currentCard.setBuzzed(currentCard.getBuzzed() + 1);
        LoadNextCard();
    }

    private void LoadNextCard() {

        Card temp;

        if (currentCard != null){
            currentCard.setCycle(2);
            currentCard.setCalled(currentCard.getCalled() + 1);
            _Taboo.SafetyQueue.add(currentCard);
            _Taboo.Library.UpdateCard(currentCard);

            if (_Taboo.SafetyQueue.size() > _Taboo.QueueSize){
                temp = _Taboo.SafetyQueue.element();
                temp.setCycle(1);
                _Taboo.SafetyQueue.remove();
                _Taboo.Library.UpdateCard(temp);
            }
        }
        currentCard = _Taboo.Library.GetNextCard();
        UpdateScreen();
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
        scoreView.setText("Score: " + _Taboo.Teams.get(_Taboo.CurrentTeam).getScore());

        //set background colour
        if (_Taboo.CurrentTeam == 0){
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
        } else if (_Taboo.CurrentTeam == 1){
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        } else if (_Taboo.CurrentTeam == 2){
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        }
    }

    private void UpdateTimer() {

        timeVal--;
        handler.post(timerRunnable);

        //clock has run out so cancel timer and go back to team ready activity
        if (timeVal <= 0){
            EndRound();
        }
    }

    private void EndRound(){

        timer.cancel();
        Scorekeeper.NextTeam();
        _Taboo.RoundsLeft--;
        int winningTeam = 0, tempScore = 0, highestScore = 0;
        boolean isTie = false;
        String winTitle = "";

        if (_Taboo.RoundsLeft == 0){

            Intent i = new Intent(Screen_Play.this, Screen_End.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //find out which team won or if the game is a tie
            for (int x = 0; x < _Taboo.Teams.size(); x++){

                //get highest score
                tempScore = _Taboo.Teams.get(x).getScore();
                System.out.println("x: " + x + " score: " + _Taboo.Teams.get(x).getScore());
                if (tempScore > highestScore){
                    highestScore = tempScore;
                    winningTeam = x;
                }

                //compare to other scores for tie
                for (int y = x + 1; y < _Taboo.Teams.size(); y++){
                    if (tempScore == _Taboo.Teams.get(y).getScore()){
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
            TextView title = (TextView) findViewById(R.id.Game_Timer);
            title.setText(String.valueOf(timeVal));
        }
    };
}
