package com.azaric.tiltmaze;

/**
 * Created by  Stefan on 1/15/17 | 15:32.
 * Created in project with name: "Tiltmaze"
 */
public class Circle {
    double x,y,r;
    boolean touched;
    boolean inPlace;
    double xT,yT;
    int id;

    boolean isMovable;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public double getxT() {
        return xT;
    }

    public void setxT(double xT) {
        this.xT = xT;
    }

    public double getyT() {
        return yT;
    }

    public void setyT(double yT) {
        this.yT = yT;
    }

    public Circle(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
}
