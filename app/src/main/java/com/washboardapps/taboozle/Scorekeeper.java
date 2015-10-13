package com.washboardapps.taboozle;

import java.util.ArrayList;

/**
 * Created by Christian on 17/02/2015.
 */
public class Scorekeeper {

    /*
        Initializes team list to chosen game size
     */
    public static void InitializeTeams(int teams) {

        _Taboo.Teams = new ArrayList(teams);
        Team temp;

        //add team objects to the team list
        for (int i = 0; i < teams; i++){
            temp = new Team();
            _Taboo.Teams.add(i, temp);
        }
    }

    public static void NextTeam(){

        if (_Taboo.CurrentTeam >= _Taboo.Teams.size() - 1){
            _Taboo.CurrentTeam = 0;
        } else {
            _Taboo.CurrentTeam++;
        }
    }

    public static void AddPoint(int team) {
        _Taboo.Teams.get(team).setScore(_Taboo.Teams.get(team).getScore() + 1);
    }

    public static void RemovePoint(int team) {
        _Taboo.Teams.get(team).setScore(_Taboo.Teams.get(team).getScore() - 1);
    }
}
