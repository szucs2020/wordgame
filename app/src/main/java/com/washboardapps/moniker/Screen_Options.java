package com.washboardapps.moniker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;

public class Screen_Options extends Activity {

    private Context context;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        context = this.getApplicationContext();
        sp = context.getSharedPreferences("options", Context.MODE_PRIVATE);

        final int leftIndex = sp.getInt(getString(R.string.sp_rangebar_index_left), 0);
        final int rightIndex = sp.getInt(getString(R.string.sp_rangebar_index_right), 10);

        RangeBar rangebar = (RangeBar)findViewById(R.id.rangebar);
        rangebar.setThumbIndices(leftIndex, rightIndex);
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

                if (leftThumbIndex < 0){
                    rangeBar.setThumbIndices(0, rightThumbIndex);
                } else if (rightThumbIndex > 10){
                    rangeBar.setThumbIndices(leftThumbIndex, 10);
                } else if (leftThumbIndex == 0 && rightThumbIndex == 0){
                    rangeBar.setThumbIndices(0, 1);
                } else if (leftThumbIndex == 10 && rightThumbIndex == 10){
                    rangeBar.setThumbIndices(9, 10);
                } else if (leftThumbIndex == rightThumbIndex){
                    rangeBar.setThumbIndices(leftThumbIndex, rightThumbIndex + 1);
                } else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(getString(R.string.sp_rangebar_index_left), leftThumbIndex);
                    editor.putInt(getString(R.string.sp_rangebar_index_right), rightThumbIndex);
                    editor.commit();
                }
            }
        });

        //Timer counter settings
        _Moniker.RoundTimer = sp.getInt(getString(R.string.sp_options_round_timer), 60);
        updateScreen();
    }

    public void Timer_Plus(View view){
        if (_Moniker.RoundTimer < 120){
            _Moniker.RoundTimer += 10;
            SaveSharedPreferences();
            updateScreen();
        }
    }

    public void Timer_Minus(View view){
        if (_Moniker.RoundTimer > 10){
            _Moniker.RoundTimer -= 10;
            SaveSharedPreferences();
            updateScreen();
        }
    }

    private void SaveSharedPreferences(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(getString(R.string.sp_options_round_timer), _Moniker.RoundTimer);
        editor.commit();
    }

    //updates view
    private void updateScreen(){
        TextView timerVal = (TextView) findViewById(R.id.Timer_Value);
        timerVal.setText(String.valueOf(_Moniker.RoundTimer));
    }
}
