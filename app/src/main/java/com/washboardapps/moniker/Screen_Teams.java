package com.washboardapps.moniker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Screen_Teams extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
    }

    public void TwoTeams(View view){
        Scorekeeper.InitializeTeams(2);
        Start_Rounds();
    }
    public void ThreeTeams(View view){
        Scorekeeper.InitializeTeams(3);
        Start_Rounds();
    }
    public void FourTeams(View view){
        Scorekeeper.InitializeTeams(4);
        Start_Rounds();
    }

    private void Start_Rounds(){
        Util.PushPage(this, Screen_Rounds.class);
    }
}
