package com.example.finance;

import android.annotation.SuppressLint;

import android.content.SharedPreferences.Editor;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static com.example.finance.FullscreenActivity.BUDGET;
import static com.example.finance.FullscreenActivity.DAYS_LEFT;
import static com.example.finance.FullscreenActivity.LAST_DATE;
import static com.example.finance.FullscreenActivity.MONEY_BOX;
import static com.example.finance.FullscreenActivity.PREFERENCES_NAME;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Reset extends AppCompatActivity implements View.OnClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    private static EditText budget;
    private static EditText moneyBox;
    private static EditText days;

    private static Button save;

    private static SharedPreferences sharedPreferences;

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

    private boolean mVisible;

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset);

        mVisible = true;


        // Set up the user interaction to manually show or hide the system UI.

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        // my code

        budget = (EditText) findViewById(R.id.budgetValue);
        moneyBox = (EditText) findViewById(R.id.moneyBoxValue);
        days = (EditText) findViewById(R.id.daysValue);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
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
        try {
            int bud = Integer.parseInt(budget.getText().toString());
            int mB = Integer.parseInt(moneyBox.getText().toString());
            int d = Integer.parseInt(days.getText().toString());
            if (bud <= 0 || d <= 0) {
                throw new Exception();
            }

            sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putInt(BUDGET, bud);
            editor.putInt(MONEY_BOX, mB);
            editor.putInt(DAYS_LEFT, d);
            editor.putLong(LAST_DATE, 0);
            editor.apply();

            startActivity(new Intent(getApplicationContext(), FullscreenActivity.class));
            finish();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Wrong input",Toast.LENGTH_SHORT).show();
        }
    }
}
