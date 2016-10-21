package com.washboardapps.taboozle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
        CheckBox cbEnabled = (CheckBox) rowView.findViewById(R.id.pack_list_detail);

        CardPack pack = (CardPack) getItem(position);
        titleTextView.setText(pack.title);
        subtitleTextView.setText(pack.packSize + " cards");
        cbEnabled.setChecked(pack.enabled);

        return rowView;
    }

}
