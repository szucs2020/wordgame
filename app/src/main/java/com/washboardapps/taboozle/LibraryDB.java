package com.washboardapps.taboozle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.DatabaseUtils;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Christian on 29/09/2015.
 */
public class LibraryDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase db;

    private static String TABLE_LIBRARY = "library";

    public LibraryDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();

        //Check if the database is empty, and set the safety queue size
        db = getReadableDatabase();
        long numEntries = DatabaseUtils.queryNumEntries(db, TABLE_LIBRARY);
        _Taboo.QueueSize = (int)Math.round(numEntries * _Taboo.QueueMultiplier);
        if (numEntries <= 0){
            System.err.println("DATABASE IS EMPTY: ABORTING...");
            System.exit(0);
        }
        db.close();
    }

    /*
        GetNextCard grabs a random card from the database that has called = 0
     */
    public Card GetNextCard(){

        db = getReadableDatabase();
        Card card = new Card();

        //The query gets one random row with called=0
        String query = "SELECT * FROM " + TABLE_LIBRARY + " WHERE CYCLE = 0 ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        //the cursor is empty, so there are no items left that have not been called
        if (!cursor.moveToFirst()){
            System.out.println("CURSOR IS EMPTY");
            db.execSQL("UPDATE " + TABLE_LIBRARY + " SET CYCLE = 0 WHERE CYCLE = 1");
            query = "SELECT * FROM " + TABLE_LIBRARY + " WHERE CYCLE = 0 ORDER BY RANDOM() LIMIT 1";
            cursor = db.rawQuery(query, null);
        } else {
            System.out.println("CURSOR IS NOT EMPTY");
        }

        //Insert data into card
        card = new Card();
        card.setKey(cursor.getInt(0));
        card.setTitle(cursor.getString(1));
        card.setWord1(cursor.getString(2));
        card.setWord2(cursor.getString(3));
        card.setWord3(cursor.getString(4));
        card.setWord4(cursor.getString(5));
        card.setWord5(cursor.getString(6));
        card.setCategory(cursor.getInt(7));
        card.setCycle(cursor.getInt(8));
        card.setCalled(cursor.getInt(9));
        card.setCorrect(cursor.getInt(10));
        card.setSkipped(cursor.getInt(11));
        card.setBuzzed(cursor.getInt(12));
        card.setDifficulty(cursor.getFloat(13));
        card.setFlag(cursor.getInt(14));

        db.close();
        return card;
    }

    public void UpdateCard(Card card){
        db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_LIBRARY + " SET CYCLE = " + card.getCycle() +" WHERE KEY = " + card.getKey());
        db.execSQL("UPDATE " + TABLE_LIBRARY + " SET CALLED = " + card.getCalled() +" WHERE KEY = " + card.getKey());
        db.execSQL("UPDATE " + TABLE_LIBRARY + " SET CORRECT = " + card.getCorrect() +" WHERE KEY = " + card.getKey());
        db.execSQL("UPDATE " + TABLE_LIBRARY + " SET SKIPPED = " + card.getSkipped() +" WHERE KEY = " + card.getKey());
        db.execSQL("UPDATE " + TABLE_LIBRARY + " SET BUZZED = " + card.getBuzzed() +" WHERE KEY = " + card.getKey());
        db.close();
    }
}