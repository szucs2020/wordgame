package com.washboardapps.moniker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Christian on 10/21/2016.
 */
public class CardPackAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CardPack> dataSource;

    public CardPackAdapter(Context context, ArrayList<CardPack> items) {
        context = context;
        dataSource = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = inflater.inflate(R.layout.card_pack_layout, parent, false);

        //get references to elements
        TextView titleTextView = (TextView) rowView.findViewById(R.id.pack_list_title);
        TextView subtitleTextView = (TextView) rowView.findViewById(R.id.pack_list_subtitle);
        Button buyButton = (Button) rowView.findViewById(R.id.pack_list_buy_button);
        final CheckBox cbEnabled = (CheckBox) rowView.findViewById(R.id.pack_list_detail);

        CardPack pack = (CardPack) getItem(position);
        titleTextView.setText(pack.title);
        subtitleTextView.setText(pack.packSize + " cards");

        if (pack.packID > 1){
            cbEnabled.setVisibility(View.GONE);
            buyButton.setVisibility(View.VISIBLE);
        }

        buyButton.setId(pack.packID);
        cbEnabled.setId(pack.packID);
        cbEnabled.setChecked(pack.enabled);

        //create the on click listener to change the database value when the checkbox is changed
        cbEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _Moniker.Library.SetEnabled(buttonView.getId(), isChecked);
            }
        });

        buyButton.setOnClickListener(Screen_Cards.buyButtonListener);

        return rowView;
    }

}
