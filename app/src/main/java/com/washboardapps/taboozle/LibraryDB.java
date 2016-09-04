package com.washboardapps.taboozle;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Christian on 29/09/2015.
 */
public class LibraryDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LIBRARY = "Library";
    private static final String TABLE_PACKS = "Packs";

    private static SQLiteDatabase db;

    public LibraryDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //force onCreate to be called
        db = getWritableDatabase();
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
            "create table " + TABLE_LIBRARY + " " +
            "(ID integer primary key," +
                    "Title text," +
                    "Word1 text," +
                    "Word2 text," +
                    "Word3 text," +
                    "Word4 text," +
                    "Word5 text," +
                    "Category integer," +
                    "Cycle integer," +
                    "Called integer," +
                    "Correct integer," +
                    "Skipped integer," +
                    "Buzzed integer," +
                    "Difficulty real," +
                    "Flags integer)"
        );
        db.execSQL("create table " + TABLE_PACKS + " (PackID integer primary key, PackName text, PackSize integer, Enabled integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKS);
        onCreate(database);
    }

    public boolean UpdateLocalDatabases(){

        long numEntries = 0;

        db = getWritableDatabase();
        try {
            //truncate local database
            db.execSQL("DELETE FROM " + TABLE_LIBRARY);
            db.execSQL("DELETE FROM " + TABLE_PACKS);

            JSONArray ja = MySQLConnector.query("select * from Library");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jobj = ja.getJSONObject(i);
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + TABLE_LIBRARY + " (ID, Title, Word1, Word2, Word3, Word4, Word5, Category, Cycle, Called, Correct, Skipped, Buzzed, Difficulty, Flags) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                stmt.bindLong(1, jobj.getInt("ID"));
                stmt.bindString(2, jobj.getString("Title"));
                stmt.bindString(3, jobj.getString("Word1"));
                stmt.bindString(4, jobj.getString("Word2"));
                stmt.bindString(5, jobj.getString("Word3"));
                stmt.bindString(6, jobj.getString("Word4"));
                stmt.bindString(7, jobj.getString("Word5"));
                stmt.bindLong(8, jobj.getInt("Category"));
                stmt.bindLong(9, 0);
                stmt.bindLong(10, jobj.getInt("Called"));
                stmt.bindLong(11, jobj.getInt("Correct"));
                stmt.bindLong(12, jobj.getInt("Skipped"));
                stmt.bindLong(13, jobj.getInt("Buzzed"));
                stmt.bindDouble(14, jobj.getDouble("Difficulty"));
                stmt.bindLong(15, jobj.getInt("Flags"));
                stmt.executeInsert();
                stmt.close();
            }

            JSONArray cats = MySQLConnector.query("select * from " + TABLE_PACKS);
            for (int i = 0; i < cats.length(); i++) {
                JSONObject jobj = cats.getJSONObject(i);
                SQLiteStatement catstmt = db.compileStatement("INSERT INTO " + TABLE_PACKS + " (PackID, PackName, PackSize, Enabled) VALUES (?,?,?,?)");
                catstmt.bindLong(1, jobj.getInt("PackID"));
                catstmt.bindString(2, jobj.getString("PackName"));
                catstmt.bindLong(3, jobj.getInt("PackSize"));

                //set the standard pack as enabled by default
                if (jobj.getInt("PackID") == 1){
                    catstmt.bindLong(4, 1);
                } else {
                    catstmt.bindLong(4, 0);
                }
                catstmt.executeInsert();
                catstmt.close();
            }

            //Set the safety queue size
            numEntries = DatabaseUtils.queryNumEntries(db, TABLE_LIBRARY);
            _Taboo.QueueSize = (int)Math.round(numEntries * _Taboo.QueueMultiplier);

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        db.close();

        if (numEntries > 0){
            return true;
        } else {
            return false;
        }
    }

    /*
        GetNextCard grabs a random card from the database that has cycle = 0
     */
    public Card GetNextCard(){

        long startTime = System.currentTimeMillis();

        db = getReadableDatabase();

        //make the SQL acceptable string
        String packString = "(";
        Cursor packs = _Taboo.Library.RawQuery("select * from Packs where Enabled=1");

        while (packs.moveToNext()){
            packString = packString + packs.getString(packs.getColumnIndex("PackID")) + ",";
        }
        packString = packString.substring(0, packString.length() - 1);
        packString = packString + ")";

        System.out.println(packString);

        //The query gets one random row with cycle=0
        String query = "SELECT * FROM " + TABLE_LIBRARY + " WHERE Cycle = 0 and Category in " + packString + " ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        //the cursor is empty, so there are no items left that have not been called
        if (!cursor.moveToFirst()){
            db.execSQL("UPDATE " + TABLE_LIBRARY + " SET Cycle = 0 and Category in " + packString + " WHERE Cycle = 1");
            cursor = db.rawQuery(query, null);
        }

        //Insert data into card
        Card card = new Card();
        card.setID(cursor.getInt(0));
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

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("GetNextCard: " + elapsedTime);

        return card;
    }

    public void UpdateCard(Card card){
        long startTime = System.currentTimeMillis();

        db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_LIBRARY
                + " SET Cycle = " + card.getCycle()
                + ", Called = " + card.getCalled()
                + ", Correct = " + card.getCorrect()
                + ", Skipped = " + card.getSkipped()
                + ", Buzzed = " + card.getBuzzed()
                + " WHERE ID = " + card.getID());
        db.close();

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("UpdateCard: " + elapsedTime);
    }

    public void UpdateCycleByID(int ID, int cycle){
        long startTime = System.currentTimeMillis();

        db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_LIBRARY
                + " SET Cycle = " + cycle + " WHERE ID = " + ID);
        db.close();

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("UpdateCycleByID: " + elapsedTime);
    }

    public void GetCycleCards(){
        db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LIBRARY + " WHERE Cycle = 2";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()){
            _Taboo.SafetyQueue.add(cursor.getInt(0));
        }
        db.close();
    }

    //attempt to upload usage data to the MySQL database
    public boolean UpdateUsageFields(){
        new Thread(new Runnable() {
            public void run() {

                db = getWritableDatabase();
                String query = "SELECT * FROM " + TABLE_LIBRARY + " WHERE Called <> 0";
                Cursor cursor = db.rawQuery(query, null);

                while (cursor.moveToNext()){

                    String q = "update " + TABLE_LIBRARY + " set Called=Called + " + cursor.getInt(9)
                            + ",Correct=Correct + " + cursor.getInt(10)
                            + ",Skipped=Skipped + " + cursor.getInt(11)
                            + ",Buzzed=Buzzed + " + cursor.getInt(12)
                            + " where ID=" + cursor.getInt(0) + ";";

                    if (MySQLConnector.nonquery(q) > 0){
                        db.execSQL("UPDATE " + TABLE_LIBRARY
                                + " SET Called=0, Correct=0, Skipped=0, Buzzed=0 WHERE ID = " + cursor.getInt(0));
                    } else {
                        System.err.println("There was a problem uploading the statistics to the server.");
                    }
                }
                db.close();
            }
        }).start();
        return true;
    }

    public Cursor RawQuery(String query){

        db = getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void SetEnabled(int PackID, boolean enabled){

        int bool;
        if(enabled){
            bool = 1;
        } else {
            bool = 0;
        }

        String q = "update " + TABLE_PACKS + " set Enabled=" + bool + " where PackID=" + PackID;
        db = getWritableDatabase();
        db.execSQL(q);
    }

    public boolean GetEnabled(int PackID){
        Cursor cursor = RawQuery("select Enabled from " + TABLE_PACKS + " where PackID=" + PackID);
        boolean enabled = false;

        while (cursor.moveToNext()){
            if(cursor.getInt(cursor.getColumnIndex("Enabled")) == 1){
                enabled = true;
            }
        }
        return enabled;
    }
}