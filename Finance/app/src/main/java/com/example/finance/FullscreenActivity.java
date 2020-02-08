package com.example.finance;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    private static Button spendMoney;
    private static Button eraseBudget;
    private static Button recount;
    private static Button reset;
    private static TextView budget;
    private static TextView moneyBox;
    private static TextView dailyMoney;
    private static TextView daysLeft;

    private static SharedPreferences sharedPreferences;

    public final static String BUDGET = "budget";
    public final static String MONEY_BOX = "moneyBox";
    public final static String DAYS_LEFT = "daysLeft";
    public final static String LAST_DATE = "lastDate";
    public final static String PREFERENCES_NAME = "preferencesName";


    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;


        // Set up the user interaction to manually show or hide the system UI.


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


        // my code

        budget = findViewById(R.id.budgetValue);
        moneyBox = findViewById(R.id.moneyBoxValue);
        dailyMoney = findViewById(R.id.dailyMoneyValue);
        daysLeft = findViewById(R.id.daysLeftValue);

        spendMoney = (Button) findViewById(R.id.spendMoney);
        eraseBudget = (Button) findViewById(R.id.eraseBudget);
        recount = (Button) findViewById(R.id.recount);
        reset = (Button) findViewById(R.id.reset);

        spendMoney.setOnClickListener(this);
        eraseBudget.setOnClickListener(this);
        recount.setOnClickListener(this);
        reset.setOnClickListener(this);

        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        int b = sharedPreferences.getInt(BUDGET, 0);
        int mBox = sharedPreferences.getInt(MONEY_BOX, 0);
        int dLeft = sharedPreferences.getInt(DAYS_LEFT, 0);
        long lastDate = sharedPreferences.getLong(LAST_DATE, 0);


        if (dLeft != 0) {
            if (lastDate == 0) {
                mBox += b / dLeft;
                b -= b / dLeft;
            } else {
                long today = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
                long lastDay = lastDate / (1000 * 60 * 60 * 24);
                long passedDays = Math.min(today - lastDay, dLeft);

                if (dLeft > 1) {
                    mBox += (b * passedDays) / (dLeft - 1);
                    b -= (b * passedDays) / (dLeft - 1);
                }
                dLeft -= passedDays;
            }
        }

        lastDate = System.currentTimeMillis();

        Editor editor = sharedPreferences.edit();
        editor.putInt(BUDGET, b);
        editor.putInt(MONEY_BOX, mBox);
        editor.putInt(DAYS_LEFT, dLeft);
        editor.putLong(LAST_DATE, lastDate);
        editor.apply();

        budget.setText(b + "");
        moneyBox.setText(mBox + "");
        dailyMoney.setText((dLeft <= 1 ? 0 : b / (dLeft - 1)) + "");
        daysLeft.setText(dLeft + "");


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }


    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eraseBudget: {
                startActivity(new Intent(this, EraseBudget.class));
                break;
            }
            case R.id.spendMoney: {
                startActivity(new Intent(this, SpendMoney.class));
                break;
            }
            case R.id.reset: {
                startActivity(new Intent(this, Reset.class));
                break;
            }
            case R.id.recount: {
                sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                if (sharedPreferences.getInt(DAYS_LEFT, 0) == 0) {
                    Toast.makeText(getApplicationContext(),"Impossible",Toast.LENGTH_SHORT).show();
                    return;
                }
                Editor editor = sharedPreferences.edit();
                editor.putInt(BUDGET, sharedPreferences.getInt(BUDGET, 0) + sharedPreferences.getInt(MONEY_BOX, 0));
                editor.putInt(MONEY_BOX, 0);
                editor.putLong(LAST_DATE, 0);
                editor.apply();
                // reload
                Intent i = new Intent(this, this.getClass());
                this.startActivity(i);
                break;
            }
        }
        finish();
    }
}
