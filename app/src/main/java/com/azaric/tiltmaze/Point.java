package com.azaric.tiltmaze;

/**
 * Created by Stefan on 2/16/17 | 01:48.
 * Created in project with name: "Tiltmaze"
 */
public class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
