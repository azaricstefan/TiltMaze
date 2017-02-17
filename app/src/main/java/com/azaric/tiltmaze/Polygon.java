package com.azaric.tiltmaze;

import android.content.Context;
import android.util.Log;

import java.util.Vector;

/**
 * Created by tamarasekularac on 2/16/17.
 */
public class Polygon {
    private int height;
    private int width;
    private Vector<Wall> walls=new Vector<>();
    private Vector<Point> holes =new Vector<>();
    private Point startPoint;
    private  Point endPoint;
    private double r=0.05;
    private double rBall=0.04;
    static double w=0.02;

    public void  makePolygon(int height, int width)
    {
        this.height=height;
        this.width=width;
        walls.add(new Wall(1-w*height/width,0, 1, 1));
        walls.add(new Wall(0,0, w*height/width, 1));
        walls.add(new Wall(0,1-w, 1, 1));
        walls.add(new Wall(0,0, 1, w));
    }

    public boolean checkedWall(Wall wall)
    {
        return true;// TODO
    }
    public boolean checkedStartPoint(Point point)
    {
        return true;// TODO
    }
    public boolean checkedEndPoint(Point point)
    {
        return true;// TODO
    }
    public boolean checkedHole(Point hole)
    {
        return true;// TODO
    }

    public void addWall(Wall w)
    {
        walls.add(w);
    }

    public void addHole(Point h)
    {
        holes.add(h);
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Vector<Point> getHoles() {
        return holes;
    }

    public void setHoles(Vector<Point> hols) {
        this.holes = hols;
    }

    public Vector<Wall> getWalls() {
        return walls;
    }

    public void setWalls(Vector<Wall> walls) {
        this.walls = walls;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getrBall() {
        return rBall;
    }

    public void setrBall(double rBall) {
        this.rBall = rBall;
    }

    public void savePolygon(String aaa, Context context) {
    }

    public void loadPolygon(String name, Context context) {
        endPoint=new Point(0.8, 0.8);
        startPoint=new Point(0.5, 0.5);
        for(int i=1; i<5; i++)
            holes.add(new Point(i*0.2,1-i*0.2));
    }





    double velocityX = 0, velocityY = 0;

    private  double traction=0.2;
    private int accTimeFactor = 1000000;

    public void setVelocity(float y, float x, long delta) {
        double ax=x*delta/accTimeFactor/accTimeFactor;
        if(ax>0)
            ax-=traction*9.81/accTimeFactor/accTimeFactor;
        else
            ax+=traction*9.81/accTimeFactor/accTimeFactor;
        double ay=y*delta/accTimeFactor/accTimeFactor;
        if(ay>0)
            ay-=traction*9.81/accTimeFactor/accTimeFactor;
        else
            ay+=traction*9.81/accTimeFactor/accTimeFactor;
        if((velocityX+x * delta / accTimeFactor/accTimeFactor>0 && velocityX<=0) ||
                (velocityX+x * delta / accTimeFactor/accTimeFactor<0 && velocityX>=0) )
            velocityX=0;
        else
            velocityX = velocityX+ x * delta / accTimeFactor/accTimeFactor;
        if((velocityY+y * delta / accTimeFactor/accTimeFactor>0 && velocityY<=0) ||
                (velocityY+y * delta / accTimeFactor/accTimeFactor<0 && velocityY>=0) )
            velocityY=0;
        else
            velocityY = velocityY+ y * delta / accTimeFactor/accTimeFactor;
        Log.d("BRZINA", "x: " + x + "y: " + y + " delta: " + delta);
        Log.d("BRZINA", "vX: " + velocityX + " x: " + velocityY);
        startPoint.setX(startPoint.getX() - velocityX * delta/accTimeFactor);
        startPoint.setY(startPoint.getY() - velocityY * delta / accTimeFactor);


        if(startPoint.getX()<0 || startPoint.getX()>1 || startPoint.getY()>1 || startPoint.getY()<0)
        {
            startPoint.setX(0.5);
            startPoint.setY(0.5);
            velocityX=0;
            velocityY=0;
        }
            //TODO



    }
}
