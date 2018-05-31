package com.washboardapps.moniker;

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
        _Moniker.NumRounds = 3;
        updateScreen();
    }

    public void Plus(View view){
        if (_Moniker.NumRounds < 10){
            _Moniker.NumRounds++;
            updateScreen();
        }
    }

    public void Minus(View view){
        if (_Moniker.NumRounds > 1){
            _Moniker.NumRounds--;
            updateScreen();
        }
    }

    public void StartGame(View view){
        _Moniker.RoundsLeft = _Moniker.NumRounds * _Moniker.Teams.size() * 2;
        Util.PushPage(this, Screen_Ready.class);
    }

    //updates points/rounds text box
    private void updateScreen(){
        TextView text = (TextView) findViewById(R.id.Rounds);
        text.setText(String.valueOf(_Moniker.NumRounds));
    }
}
