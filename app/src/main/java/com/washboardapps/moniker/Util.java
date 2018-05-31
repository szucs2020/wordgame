package com.washboardapps.moniker;

import android.app.Activity;
import android.content.Intent;

import java.util.Map;

/**
 * Created by Christian on 5/30/2018.
 */
public class Util {
    public static void PushPage(Activity context, Class newPage) {
        Intent i = new Intent(context, newPage);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(i);
    }

    public static void ReplacePage(Activity context, Class newPage) {
        Intent i = new Intent(context, newPage);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    public static void ReplacePage(Activity context, Class newPage, Map<String, String> extras) {
        Intent i = new Intent(context, newPage);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        for (Map.Entry<String, String> extra : extras.entrySet()) {
            i.putExtra(extra.getKey(), extra.getValue());
        }
        context.startActivity(i);
    }
}
