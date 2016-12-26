package com.washboardapps.moniker;

import java.util.ArrayList;

/**
 * Created by Christian on 17/02/2015.
 */
public class Scorekeeper {

    /*
        Initializes team list to chosen game size
     */
    public static void InitializeTeams(int teams) {

        _Moniker.Teams = new ArrayList(teams);
        Team temp;

        //add team objects to the team list
        for (int i = 0; i < teams; i++){
            temp = new Team();
            _Moniker.Teams.add(i, temp);
        }
    }

    public static void NextTeam(){

        if (_Moniker.CurrentTeam >= _Moniker.Teams.size() - 1){
            _Moniker.CurrentTeam = 0;
        } else {
            _Moniker.CurrentTeam++;
        }
    }

    public static void AddPoint(int team) {
        _Moniker.Teams.get(team).setScore(_Moniker.Teams.get(team).getScore() + 1);
    }

    public static void RemovePoint(int team) {
        _Moniker.Teams.get(team).setScore(_Moniker.Teams.get(team).getScore() - 1);
    }
}
