package com.washboardapps.taboozle;

import android.app.Activity;
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
