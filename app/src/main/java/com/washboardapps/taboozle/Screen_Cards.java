package com.washboardapps.taboozle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Screen_Cards extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        LinearLayout main = (LinearLayout)findViewById(R.id.CardLinearLayout);
        View header = getLayoutInflater().inflate(R.layout.card_pack_header, main,false);
        main.addView(header);

        Cursor cursor = _Taboo.Library.RawQuery("select * from Packs");
        int i = 1;
        while (cursor.moveToNext()){

            View pack = getLayoutInflater().inflate(R.layout.card_pack_layout, main,false);
            main.addView(pack);

            TextView packName = (TextView)  pack.findViewWithTag("pack_name");
            packName.setText(cursor.getString(cursor.getColumnIndex("PackName")));

            TextView packSize = (TextView)  pack.findViewWithTag("pack_size");
            packSize.setText(cursor.getString(cursor.getColumnIndex("PackSize")));

            CheckBox packEnabled = (CheckBox) pack.findViewWithTag("pack_checkbox");

            if (_Taboo.Library.GetEnabled(i)){
                packEnabled.setChecked(true);
            }
            packEnabled.setTag(i);

            i++;
        }
    }

    public void onCheckboxClicked(View view) {

        int ID = (int)view.getTag();

        Cursor cursor = _Taboo.Library.RawQuery("select * from Packs where Enabled=1");
        cursor.moveToFirst();

        System.out.println("count: " + cursor.getCount());

        if (cursor.getCount() == 1 && cursor.getInt(cursor.getColumnIndex("PackID")) == ID){

            System.out.println("last pack");

            CheckBox cb = (CheckBox)view;
            cb.setChecked(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You must enable at least one card pack.");
            builder.setCancelable(false);

            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            //enable/disable the pack
            System.out.println("sending: " + ((CheckBox) view).isChecked() + " to " + ID);
            _Taboo.Library.SetEnabled(ID, ((CheckBox) view).isChecked());
        }
    }

}
