package com.washboardapps.moniker;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Christian on 15/02/2015.
 */
public class _Moniker extends Application {

    public static Context GlobalContext;
    public static LibraryDB Library;
    public static LinkedList<Integer> SafetyQueue;
    public static ArrayList<Team> Teams;
    public static int CurrentTeam;
    public static int NumRounds;
    public static int RoundsLeft;
    public static int QueueSize;
    public static final double QueueMultiplier = 0.1;
}
