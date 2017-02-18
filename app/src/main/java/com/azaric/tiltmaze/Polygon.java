package com.azaric.tiltmaze;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public void savePolygon(String name, Context context) {
        File file = new File(context.getFilesDir(), name);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);

            //START POINT
            double x = startPoint.getX();
            double y = startPoint.getY();
            String stringStartPoint = x + ":" + y +"\n";
            writer.append(stringStartPoint);

            //END POINT
            x = endPoint.getX(); y = endPoint.getY();
            String stringEndPoint = x + ":" + y +"\n";
            writer.append(stringEndPoint);

            //HOLE
            String sizeOfHoles = ""+holes.size()+"\n";
            writer.append(sizeOfHoles);
            for(Point p : holes){
                x = p.getX(); y = p.getY();
                String stringHole = x + ":" + y +"\n";
                writer.append(stringHole);
            }

            //WALL
            String sizeOfWalls = ""+walls.size()+"\n";
            writer.append(sizeOfWalls);
            for(Wall w : walls){
                double xS = w.getxS(); double xE = w.getxE();
                double yS = w.getyS(); double yE = w.getyE();
                String stringWall = xS + ":" + xE + ":" + yS + ":" + yE  +"\n";
                writer.append(stringWall); //WALL
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPolygonFromFile(String name, Context context) {
        File file = new File(context.getFilesDir(), name);
        FileReader reader = null;
        try{
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            //START POINT READ
            String startPoint = bufferedReader.readLine();
            String[] startPointArray = startPoint.split(":");
            this.startPoint.setX(Double.parseDouble(startPointArray[0]));
            this.startPoint.setY(Double.parseDouble(startPointArray[1]));

            //END POINT READ
            String endPoint = bufferedReader.readLine();
            String[] endPointArray = startPoint.split(":");
            this.endPoint.setX(Double.parseDouble(endPointArray[0]));
            this.endPoint.setY(Double.parseDouble(endPointArray[1]));

            //READ HOLES
            String sizeOfHolesString = bufferedReader.readLine();
            int sizeOfHoles = Integer.parseInt(sizeOfHolesString);

            for(int i = 0; i < sizeOfHoles; i++) {
                String hole = bufferedReader.readLine();
                if (hole == null)
                    break;
                String[] holeArray = hole.split(":");
                Log.d("HOLE_ARRAY", "X: " + holeArray [0] + " Y: " + holeArray [1]);
                String loadedX = holeArray [0];
                String loadedY = holeArray [1];
                Point h = new Point(Double.parseDouble(loadedX),Double.parseDouble(loadedY));
                holes.add(h);
            }

            //READ WALLS
            String sizeOfWallsString = bufferedReader.readLine();
            int sizeOfWalls = Integer.parseInt(sizeOfWallsString);

            for(int i = 0; i < sizeOfWalls; i++) {
                String wall = bufferedReader.readLine();
                if (wall == null)
                    break;
                String[] wallArray = wall.split(":");
                Log.d("WALL_ARRAY",
                        "X start: " + wallArray[0] + " X end: " + wallArray[1] +
                                "Y start: " + wallArray[2] + " Y end: " + wallArray[3]);

                String loadedXstart = wallArray[0];
                String loadedXend = wallArray[1];
                String loadedYstart = wallArray[2];
                String loadedYend = wallArray[3];

                Wall w = new Wall(
                        Double.parseDouble(loadedXstart),Double.parseDouble(loadedXend),
                        Double.parseDouble(loadedYstart),Double.parseDouble(loadedYend));
                walls.add(w);
            }


        } catch (NumberFormatException nfe){
            nfe.printStackTrace();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPolygon(String name, Context context) {
        endPoint=new Point(0.8, 0.8);
        startPoint=new Point(0.5, 0.5);
        for(int i=1; i<5; i++)
            holes.add(new Point(i*0.2,1-i*0.2));
    }


    double velocityX = 0, velocityY = 0;

    private  double traction=0.5;
    private double accTimeFactor = 1000000;
    private double g=9.81;
    public void setVelocity(float y, float x, long delta) {

        velocityX = velocityX+ x * delta /width/accTimeFactor/g/g;
        velocityY = velocityY+ y * delta / height/accTimeFactor/g/g;
        velocityX*=1-traction;
        velocityY*=1-traction;
        Log.d("BRZINA","x: " +  x + "y: " + y + " delta: " + delta);
        Log.d("BRZINA","vX: " + velocityX + " x: " + velocityY);
        startPoint.setX(startPoint.getX() + velocityX * delta/ accTimeFactor);
        startPoint.setY(startPoint.getY() + velocityY * delta/ accTimeFactor);


        if(startPoint.getX()<0 || startPoint.getX()>1 || startPoint.getY()>1 || startPoint.getY()<0)
        {
            startPoint.setX(0.5);
            startPoint.setY(0.5);
            velocityX=0;
            velocityY=0;
        }
        //TODO
    }


//    double velocityX = 0, velocityY = 0;
//
//    private  double traction=0.2;
//    private int accTimeFactor = 1000000;
//
//    public void setVelocity(float y, float x, long delta) {
//
//        double ax=x*delta/accTimeFactor/accTimeFactor;
//        if(ax>0)
//            ax-=traction*9.81/accTimeFactor/accTimeFactor;
//        else
//            ax+=traction*9.81/accTimeFactor/accTimeFactor;
//        double ay=y*delta/accTimeFactor/accTimeFactor;
//        if(ay>0)
//            ay-=traction*9.81/accTimeFactor/accTimeFactor;
//        else
//            ay+=traction*9.81/accTimeFactor/accTimeFactor;
//        if((velocityX+x * delta / accTimeFactor/accTimeFactor>0 && velocityX<=0) ||
//                (velocityX+x * delta / accTimeFactor/accTimeFactor<0 && velocityX>=0) )
//            velocityX=0;
//        else
//            velocityX = velocityX+ x * delta / accTimeFactor/accTimeFactor;
//        if((velocityY+y * delta / accTimeFactor/accTimeFactor>0 && velocityY<=0) ||
//                (velocityY+y * delta / accTimeFactor/accTimeFactor<0 && velocityY>=0) )
//            velocityY=0;
//        else
//            velocityY = velocityY+ y * delta / accTimeFactor/accTimeFactor;
//        Log.d("BRZINA", "x: " + x + "y: " + y + " delta: " + delta);
//        Log.d("BRZINA", "vX: " + velocityX + " x: " + velocityY);
//        startPoint.setX(startPoint.getX() - velocityX * delta/accTimeFactor);
//        startPoint.setY(startPoint.getY() - velocityY * delta / accTimeFactor);
//
//
//        if(startPoint.getX()<0 || startPoint.getX()>1 || startPoint.getY()>1 || startPoint.getY()<0)
//        {
//            startPoint.setX(0.5);
//            startPoint.setY(0.5);
//            velocityX=0;
//            velocityY=0;
//        }
            //TODO
//    }
}
