package com.washboardapps.moniker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.washboardapps.billing.IabHelper;
import com.washboardapps.billing.IabResult;
import com.washboardapps.billing.Purchase;

import java.util.ArrayList;

public class Screen_Cards extends Activity {

    public static View.OnClickListener buyButtonListener;

    private ListView cardPacks;
    private CardPackAdapter adapter;
    private IabHelper mHelper;

    private IabHelper.OnConsumeFinishedListener ConsumedListener;
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;

    private final String SKU_CARDS_PACK1 = "wordgame.pack.adult";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        buyButtonListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BuyCardPack(v.getId());
            }
        };

        mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhUad8+LQPImoIHj" +
                "gxDBbMKP2ikTU9avilAdgKeW6Ri8AgrmyEbLEZWOpXJcOQ1rPVOLLDzHz/vuwT5ybvlvYBmOW5jtW9k3I" +
                "R/bG9uVlxYsj2j2vXBSvKGj93eB7W8CG1qqHfGJ4kn9H6r2zPJe+PRkIzRsG2DazHAyoIsQyo7mKfJs/Z" +
                "unHdNKkT1ARoBSka2bJpDY9AqZOMqe6uNJbcbEelwV5mzV/5s8uuioNyVEqnd5xmAmRPspheUi1AZIZEU" +
                "dpkoYr4HhXlgHkdwGmOk1hcLYUKNeod+ekWA218QDuCKXRvAO4r2sAc2MW1dXpCntOAmNuXlb5GYJo7Oc" +
                "QnwIDAQAB");

        //callback when billing is finished setting up
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    System.err.println("Problem setting up In-app Billing: " + result);
                }
            }
        });

        //callback when purchase is finished
        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener(){
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

                try{

                    if (result.isFailure()) {
                        // Handle error
                        System.out.println("Purchase failed");
                        return;
                    }

                    //consume the purchase immediately
                    else if (purchase.getSku().equals(SKU_CARDS_PACK1)) {
                        mHelper.consumeAsync(purchase, ConsumedListener);
                    }

                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        };

        //callback when item is consumed
        ConsumedListener = new IabHelper.OnConsumeFinishedListener(){
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {

                if (purchase.getSku().equals(SKU_CARDS_PACK1)){

                    ListView cardPacks = (ListView) findViewById(R.id.card_list_view);
                    RelativeLayout rl = (RelativeLayout)cardPacks.getChildAt(1);
                    RelativeLayout rl2 = (RelativeLayout)rl.findViewById(R.id.pack_list_overlap_layout);
                    CheckBox cbEnabled = (CheckBox) rl2.getChildAt(0);
                    Button buyButton = (Button) rl2.getChildAt(1);

                    buyButton.setVisibility(View.GONE);
                    cbEnabled.setVisibility(View.VISIBLE);
                }
            }
        };

        cardPacks = (ListView) findViewById(R.id.card_list_view);
        final ArrayList<CardPack> recipeList = LibraryDB.getCardPacks();
        adapter = new CardPackAdapter(this, recipeList);
        cardPacks.setAdapter(adapter);

//        ListView cardPacks = (ListView) findViewById(R.id.card_list_view);
//        RelativeLayout rl = (RelativeLayout)cardPacks.getChildAt(1);
//        RelativeLayout rl2 = (RelativeLayout)rl.findViewById(R.id.pack_list_overlap_layout);
//        CheckBox cbEnabled = (CheckBox) rl2.getChildAt(0);
//        Button buyButton = (Button) rl2.getChildAt(1);
    }

    private void BuyCardPack(int pack){
        try {

            if (pack == 2){
                mHelper.launchPurchaseFlow(this, "wordgame.pack.adult", 10001, mPurchaseFinishedListener, "mypurchasetoken");
            } else {
                //add a message saying "this card pack is not available yet"
                System.err.println("ERROR: BUYING CARD PACK WHICH DOES NOT EXIST YET");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }
}
