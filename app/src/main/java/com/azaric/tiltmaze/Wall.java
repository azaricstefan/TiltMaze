package com.azaric.tiltmaze;

/**
 * Created by tamarasekularac on 2/16/17.
 */
public class Wall {
    double xS, yS, xE, yE;

    Wall()
    {

    }

    public Wall(double xS, double yS, double xE, double yE) {
        this.xS = xS;
        this.yS = yS;
        this.xE = xE;
        this.yE = yE;
    }

    public double getxS() {
        return xS;
    }

    public void setxS(double xS) {
        this.xS = xS;
    }

    public double getyS() {
        return yS;
    }

    public void setyS(double yS) {
        this.yS = yS;
    }

    public double getxE() {
        return xE;
    }

    public void setxE(double xE) {
        this.xE = xE;
    }

    public double getyE() {
        return yE;
    }

    public void setyE(double yE) {
        this.yE = yE;
    }
}
