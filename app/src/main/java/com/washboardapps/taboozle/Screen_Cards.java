package com.washboardapps.taboozle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Screen_Cards extends Activity {

    private ListView cardPacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        cardPacks = (ListView) findViewById(R.id.card_list_view);
        final ArrayList<CardPack> recipeList = CardPack.getCardPacks();
        CardPackAdapter adapter = new CardPackAdapter(this, recipeList);
        cardPacks.setAdapter(adapter);
    }

    public void onCheckboxClicked(View view) {
//
//        int ID = (int)view.getTag();
//
//        Cursor cursor = _Taboo.Library.RawQuery("select * from Packs where Enabled=1");
//        cursor.moveToFirst();
//
//        System.out.println("count: " + cursor.getCount());
//
//        if (cursor.getCount() == 1 && cursor.getInt(cursor.getColumnIndex("PackID")) == ID){
//
//            System.out.println("last pack");
//
//            CheckBox cb = (CheckBox)view;
//            cb.setChecked(true);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("You must enable at least one card pack.");
//            builder.setCancelable(false);
//
//            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        } else {
//            //enable/disable the pack
//            System.out.println("sending: " + ((CheckBox) view).isChecked() + " to " + ID);
//            _Taboo.Library.SetEnabled(ID, ((CheckBox) view).isChecked());
//        }
    }

}
