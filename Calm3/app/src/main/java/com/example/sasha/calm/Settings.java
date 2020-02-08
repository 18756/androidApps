package com.example.sasha.calm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Settings extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Button confirm;
    EditText radius;
    EditText speed;
    Switch fill;
    EditText thickness;
    Spinner background;
    String[] data = {"bright", "middle", "dark"};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), FullscreenActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        confirm = (Button) findViewById(R.id.confirm);
        radius = (EditText) findViewById(R.id.radius);
        speed = (EditText) findViewById(R.id.speed);
        thickness = (EditText) findViewById(R.id.stroke);
        background = (Spinner) findViewById(R.id.background);


        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        background.setAdapter(adapter);
        // выделяем элемент
        background.setSelection(Ball.mood);
        // устанавливаем обработчик нажатия
        background.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // показываем позицию нажатого элемента
                Ball.mood = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fill = (Switch) findViewById(R.id.fill);
        fill.setOnCheckedChangeListener(this);
        fill.setChecked(DrawThread.is_fill);

        radius.setText("" + (int) Ball.R, TextView.BufferType.EDITABLE);
        speed.setText("" + (int) Ball.speed, TextView.BufferType.EDITABLE);
        thickness.setText("" + (int) DrawThread.thickness, TextView.BufferType.EDITABLE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float r = Float.parseFloat(radius.getText().toString());
                    float s = Float.parseFloat(speed.getText().toString());
                    float t = Float.parseFloat(thickness.getText().toString());
                    if (r <= 0 || t <= 0) {
                        throw new Exception("wrong input");
                    }

                    if (r * 2 > Ball.width || r * 2 > Ball.height) {
                        throw new Exception("Too big radius");
                    }

                    if (s > 500) {
                        throw new Exception("Too big speed (more than 500)");
                    }

                    if (r <= t / 2) {
                        throw new Exception("Too small raduis and big thickness");
                    }
                    Ball.speed = s;
                    Ball.R = r;
                    DrawThread.thickness = t;

                    printMessage("Saved");
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void printMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        DrawThread.is_fill = b;
    }
}


