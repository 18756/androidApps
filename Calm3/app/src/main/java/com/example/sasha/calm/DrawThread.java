package com.example.sasha.calm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;

/**
 * Created by sasha on 18.05.18.
 */

public class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private Paint paint = new Paint();
    private ArrayList<Ball> balls = new ArrayList<>();
    private int counter = 0;
    public static boolean is_fill = true;
    public static float thickness = 5;

    //public static Canvas canvas;

    private volatile boolean running = true;//флаг для остановки потока

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;

        if (is_fill) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }
        paint.setStrokeWidth(thickness);
        paint.setTextSize(100);
    }

    public void requestStop() {
        running = false;
    }

    public void requestResume() {
        running = true;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {

                    // рисование на canvas
                    canvas.drawRGB(255, 255, 255); // white background
                    for (int i = 0; i < balls.size(); i++) { // foreach may fails, because 2 threads
                        Ball b = balls.get(i);
                        paint.setColor(b.getColor());
                        canvas.drawCircle(b.getX(), b.getY(), Ball.getR(), paint);
                        b.update();
                    }
                    paint.setColor(Color.BLACK);
                    canvas.drawText("Amount: " + balls.size(), (float) (Ball.width - 650), 150, paint);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void touch(float x, float y) {
        if (counter % 2 == 0) { // create point
            balls.add(new Ball(x, y));

        } else { // move point
            balls.get(counter / 2).go(x, y);
        }
        counter++;
    }
}
