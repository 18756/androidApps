package com.example.sasha.calm;


import android.graphics.Color;

/**
 * Created by sasha on 18.05.18.
 */

public class Ball {
    private float x;
    private float y;
    private double dx = 0;
    private double dy = 0;
    private int color;
    public static double width;  // size
    public static double height; // of screen
    public static float speed = 10;
    public static float R = 100; // radius
    private float realR; // real radius = radius + thickness / 2

    public static int mood = 0; // 0 - bright; 1 - middle; 2 - dark;


    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        realR = R + DrawThread.thickness / 2;
        if (mood == 0) {  // generate color "rgb" (red green blue)
            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);
            if (r < 200 && g < 200 && b < 200) {
                float rand = (float) (Math.random() * 3);
                if (rand <= 1) {
                    r = (int) (240 + Math.random() * 15);
                } else if (rand <= 2) {
                    g = (int) (240 + Math.random() * 15);
                } else {
                    b = (int) (240 + Math.random() * 15);
                }
            }
            color = Color.rgb(r, g, b);
        } else if (mood == 2) {
            color = Color.rgb((int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
        } else {
            color = Color.rgb((int) (Math.random() * 180), (int) (Math.random() * 180), (int) (Math.random() * 180));
        }

    }

    public void go(float x2, float y2) { // start to move
        if (x == x2) {
            if (y > y2) {
                dy = -speed;
            } else {
                dy = speed;
            }
        } else {
            float k = (float) Math.abs((y - y2) / (x - x2));
            float corner = (float) Math.atan(k);
            dy = Math.sin(corner) * speed;
            dx = Math.cos(corner) * speed;
            if (x > x2) {
                dx = -dx;
            }
            if (y > y2) {
                dy = -dy;
            }
        }
    }

    public void update() {
        x += dx;
        y += dy;

        if (x + realR >= width) {
            dx = -Math.abs(dx);
        }
        if (x - realR <= 0) {
            dx = Math.abs(dx);
        }
        if (y + realR >= height) {
            dy = -Math.abs(dy);
        }
        if (y - realR <= 0) {
            dy = Math.abs(dy);
        }

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static float getR() {
        return R;
    }

    public int getColor() {
        return color;
    }

    public static void setWidth(int width) {
        Ball.width = width;
    }

    public static void setHeight(int height) {
        Ball.height = height;
    }
}
