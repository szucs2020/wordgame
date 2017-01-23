package com.washboardapps.moniker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.washboardapps.billing.IabHelper;
import com.washboardapps.billing.IabResult;
import com.washboardapps.billing.Inventory;
import com.washboardapps.billing.Purchase;

import java.util.ArrayList;

public class Screen_Cards extends Activity {

    public static View.OnClickListener buyButtonListener;

    private ListView cardPacks;
    private CardPackAdapter adapter;
    private IabHelper mHelper;

//    private IabHelper.OnConsumeFinishedListener ConsumedListener;
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;

    //syncronization variables
    private boolean billingFinished = false;
    private boolean adapterFinished = false;

    private final String SKU_CARDS_PACK1 = "wordgame.pack.adult";
//    private final String SKU_CARDS_PACK1 = "android.test.purchased";

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
                } else {
                    billingFinished = true;
                    QueryInventory();
                }
            }
        });

        //callback when purchase is finished
        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener(){
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

                try{

                    if (result.isFailure()) {
                        return;
                    }

                    //consume the purchase immediately
                    else if (purchase.getSku().equals(SKU_CARDS_PACK1)) {
                        UnhidePack(1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //Add the view elements for each pack
        cardPacks = (ListView) findViewById(R.id.card_list_view);
        final ArrayList<CardPack> packList = LibraryDB.getCardPacks();
        adapter = new CardPackAdapter(this, packList);
        cardPacks.setAdapter(adapter);

        cardPacks.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                cardPacks.removeOnLayoutChangeListener(this);
                adapterFinished = true;
                QueryInventory();
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println("onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
//            System.out.println("onActivityResult handled by IABUtil.");
        }
    }

    private void QueryInventory(){

        final IabHelper.QueryInventoryFinishedListener inventoryFinishedListener
                = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {

                if (result.isFailure()) {
                    System.out.println("QUERY INVENTORY FAILED");
                }
                else {
                    // does the user have the premium upgrade?
                    boolean bHasPack1 = inventory.hasPurchase(SKU_CARDS_PACK1);
                    System.out.println("has pack 1: " + bHasPack1);
                    if (bHasPack1){
                        UnhidePack(1);
                    }
                }
            }
        };

        if (billingFinished && adapterFinished){
            //query the purchases and open the packs up if they are owned
            try {
                mHelper.queryInventoryAsync(inventoryFinishedListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    //Makes the pack checkbox visible given a pack number
    private void UnhidePack(int pack){

        ListView cardPacks = (ListView) findViewById(R.id.card_list_view);
        RelativeLayout rl = (RelativeLayout)cardPacks.getChildAt(pack);
        RelativeLayout rl2 = (RelativeLayout)rl.findViewById(R.id.pack_list_overlap_layout);
        CheckBox cbEnabled = (CheckBox) rl2.getChildAt(0);
        Button buyButton = (Button) rl2.getChildAt(1);

        buyButton.setVisibility(View.GONE);
        cbEnabled.setVisibility(View.VISIBLE);
    }

    private void BuyCardPack(int pack){

        ListView lv = (ListView) findViewById(R.id.card_list_view);

        try {
            if (pack == 2){
                mHelper.launchPurchaseFlow(this, SKU_CARDS_PACK1, 10001, mPurchaseFinishedListener, "mypurchasetoken");
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
