package com.washboardapps.taboozle;

import android.database.Cursor;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Christian on 10/21/2016.
 */
public class CardPack {

    public String title;
    public String packSize;
    public boolean enabled;

    //downloads the packs from the webservice and adds them to an arraylist
    public static ArrayList<CardPack> getCardPacks(){

        ArrayList<CardPack> cardPacks = new ArrayList<CardPack>();
        Cursor cursor = _Taboo.Library.RawQuery("select * from Packs");
        int i = 1;

        while (cursor.moveToNext()){

            CardPack cp = new CardPack();
            cp.title = cursor.getString(cursor.getColumnIndex("PackName"));
            cp.packSize = cursor.getString(cursor.getColumnIndex("PackSize"));
            cp.enabled = _Taboo.Library.GetEnabled(i);
            cardPacks.add(cp);

            i++;
        }
        return cardPacks;
    }
}
