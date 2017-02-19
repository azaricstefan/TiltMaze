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
    private double height;
    private double width;
    private Vector<Wall> walls=new Vector<>();
    private Vector<Point> holes =new Vector<>();
    private Point startPoint;
    private Point endPoint;
    private double r=0.05;
    private double rBall=0.04;
    static double w=0.02;
    private boolean finish;
    private boolean win;

    public void  makePolygon(double height, double width) {
        this.height=height;
        this.width=width;
        walls.add(new Wall(width/height-w,0, width/height, 1));
        walls.add(new Wall(0,0, w, 1));
        walls.add(new Wall(0,1-w, width/height, 1));
        walls.add(new Wall(0,0, width/height, w));
    }
    boolean checkedIntersection(Wall w, Point p, double r){
        if (w.getxS()<p.getX() && w.getxE()>p.getX() && w.getyS()-r<p.getY() && w.getyE()+r>p.getY())
            return true;
        if (w.getxS()-r<p.getX() && w.getxE()+r>p.getX() && w.getyS()<p.getY() && w.getyE()>p.getY())
            return true;
        if(dist(p,new Point(w.getxE(),w.getyE()))<r)
            return true;
        if(dist(p,new Point(w.getxE(),w.getyS()))<r)
            return true;
        if(dist(p,new Point(w.getxS(),w.getyE()))<r)
            return true;
        if(dist(p,new Point(w.getxS(),w.getyS()))<r)
            return true;
        return false;
    }

    public boolean checkedWall(Wall w) {
        Wall wall=new Wall(Math.min(w.getxS(),w.getxE()),Math.min(w.getyS(),w.getyE()),
                Math.max(w.getxS(), w.getxE()),Math.max(w.getyS(), w.getyE()));
        for(Point h:holes)
            if(checkedIntersection(wall,h,r))
                return false;
        if(endPoint!=null && checkedIntersection(wall,endPoint,r))
            return false;
        if(startPoint!=null && checkedIntersection(wall,startPoint,rBall))
            return false;
        return true;
    }
    public boolean checkedStartPoint(Point point) {
        for(Wall wall:walls)
            if(checkedIntersection(wall,point,rBall))
                return false;
        for(Point h:holes)
            if(dist(h,point)<rBall+r)
                return false;
        if(endPoint!=null && dist(endPoint,point)<rBall+r)
            return false;
        return true;
    }
    public boolean checkedEndPoint(Point point) {
        for(Wall wall:walls)
            if(checkedIntersection(wall,point,r))
                return false;
        for(Point h:holes)
            if(dist(h,point)<r+r)
                return false;
        if(startPoint!=null && dist(startPoint,point)<rBall+r)
            return false;
        return true;
    }
    public boolean checkedHole(Point point) {
        for(Wall wall:walls)
            if(checkedIntersection(wall,point,r))
                return false;
        for(Point h:holes)
            if(dist(h,point)<r+r)
                return false;
        if(endPoint!=null && dist(endPoint,point)<r+r)
            return false;
        if(startPoint!=null && dist(startPoint,point)<rBall+r)
            return false;
        return true;
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
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
        double scaleW=(width/height);
        height=1;
        width=1;
        for(Wall wall:walls)
        {
            wall.setxE(wall.getxE()/scaleW);
            wall.setxS(wall.getxS() / scaleW);
        }
        for(Point p:holes)
            p.setX(p.getX()/scaleW);
        endPoint.setX(endPoint.getX()/scaleW);
        startPoint.setX(startPoint.getX()/scaleW);

        File file = new File(context.getExternalFilesDir(null), name);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);

            //TODO: TAMARA: proveri to za sklaliranje
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
        width=height=1;
        File file = new File(context.getExternalFilesDir(null), name);
        FileReader reader = null;
        try{
            //TODO: TAMARA: proveri to za sklaliranje
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            //START POINT READ
            String startPoint = bufferedReader.readLine();
            String[] startPointArray = startPoint.split(":");
            this.startPoint = new Point();
            this.startPoint.setX(Double.parseDouble(startPointArray[0]));
            this.startPoint.setY(Double.parseDouble(startPointArray[1]));

            //END POINT READ
            String endPoint = bufferedReader.readLine();
            String[] endPointArray = endPoint.split(":");
            this.endPoint = new Point();
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
                        Double.parseDouble(loadedXstart),Double.parseDouble(loadedYstart),
                        Double.parseDouble(loadedXend), Double.parseDouble(loadedYend));
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
        width=height=1;

        endPoint=new Point(0.8, 0.8);
        startPoint=new Point(0.5, 0.5);
        for(int i=1; i<5; i++)
            holes.add(new Point(i*0.2,1-i*0.2));
        walls.add(new Wall(1-w,0, 1, 1));
        walls.add(new Wall(0,0, w, 1));
        walls.add(new Wall(0,1-w, 1, 1));
        walls.add(new Wall(0,0, 1, w));
    }


    double velocityX = 0, velocityY = 0;

    private  double traction=0.2;
    private  double collision=1.7;
    private double accTimeFactor = 1000000;
    private double g=9.81;
    public void setVelocity(float y, float x, long delta) {

        velocityX = velocityX+ x * delta /width/accTimeFactor/g/g;
        velocityY = velocityY+ y * delta / height/accTimeFactor/g/g;

        velocityX*=1-traction;
        velocityY*=1-traction;
        //Log.d("BRZINA", "x: " + x + "y: " + y + " delta: " + delta);
        //Log.d("BRZINA", "vX: " + velocityX + " x: " + velocityY);
        Point lastPoint=new Point(startPoint.getX(),startPoint.getY());
        startPoint.setX(startPoint.getX() + velocityX * delta / accTimeFactor);
        startPoint.setY(startPoint.getY() + velocityY * delta/ accTimeFactor);

        for(Point h:holes)
        {
            Point nh=pointToLineDistance(lastPoint,startPoint,h);
            double cr=dist(nh,h);
            if(cr<r)
            {

                if(cr+rBall>r) {
                    nh.setX(h.getX() + (nh.getX() - h.getX()) * (r - rBall) / cr);
                    nh.setY(h.getY() + (nh.getY() - h.getY()) * (r - rBall) / cr);
                }
                startPoint=nh;
                Log.d("kraj", "kraja");
                finish=true;
                win=false;
                return;
            }

        }
        Point nh=pointToLineDistance(lastPoint,startPoint,endPoint);
        double cr=dist(nh,endPoint);
        if(cr<r)
        {
            if(cr+rBall>r) {
                nh.setX(endPoint.getX() + (nh.getX() - endPoint.getX()) * (r - rBall) / cr);
                nh.setY(endPoint.getY() + (nh.getY() - endPoint.getY()) * (r - rBall) / cr);
            }
            startPoint=nh;
            finish=true;
            win=true;
            return;
        }
        for(Wall w:walls)
        {
            if(w.getxS()-rBall>lastPoint.getX() && w.getxS()-rBall<startPoint.getX())
            {
                double yy=lastPoint.getY()+(startPoint.getY()-lastPoint.getY())*(w.getxS()-rBall-lastPoint.getX())/(startPoint.getX()-lastPoint.getX());
                if(yy>w.getyS() && yy<w.getyE())
                {
                    startPoint.setX(startPoint.getX()-2*(startPoint.getX()-w.getxS()+rBall));
                    velocityX=-velocityX*collision;
                    velocityY*=collision;
                }
            }
            if(w.getxE()+rBall<lastPoint.getX() && w.getxE()+rBall>startPoint.getX())
            {
                double yy=lastPoint.getY()+(startPoint.getY()-lastPoint.getY())*(w.getxE()+rBall-lastPoint.getX())/(startPoint.getX()-lastPoint.getX());
                if(yy>w.getyS() && yy<w.getyE())
                {
                    startPoint.setX(startPoint.getX()-2*(startPoint.getX()-w.getxE()-rBall));
                    velocityX=-velocityX*collision;
                    velocityY*=collision;
                }
            }
            if(w.getyS()-rBall>lastPoint.getY() && w.getyS()-rBall<startPoint.getY())
            {
                double xx=lastPoint.getX()+(startPoint.getX()-lastPoint.getX())*(w.getyS()-rBall-lastPoint.getY())/(startPoint.getY()-lastPoint.getY());
                if(xx>w.getxS() && xx<w.getxE())
                {
                    startPoint.setY(startPoint.getY() - 2 * (startPoint.getY() - w.getyS() + rBall));
                    velocityY=-velocityY*collision;
                    velocityX*=collision;
                }
            }
            if(w.getyE()+rBall<lastPoint.getY() && w.getyE()+rBall>startPoint.getY())
            {
                double xx=lastPoint.getX()+(startPoint.getX()-lastPoint.getX())*(w.getyS()+rBall-lastPoint.getY())/(startPoint.getY()-lastPoint.getY());
                if (xx > w.getxS() && xx < w.getxE()) {
                    startPoint.setY(startPoint.getY()-2*(startPoint.getY()-w.getyE()-rBall));
                    velocityY=-velocityY*collision;
                    velocityX*=collision;
                }
            }
        }
    }

    public double dist(Point A, Point B) {
        return Math.sqrt((A.getX()-B.getX())*(A.getX()-B.getX())+(A.getY()-B.getY())*(A.getY()-B.getY()));
    }

    public Point pointToLineDistance(Point A, Point B, Point P) {
        Point v=new Point(B.getX() - A.getX(), B.getY() - A.getY());
        Point w=new Point(P.getX() - A.getX(), P.getY() - A.getY());

        double c1 =  w.getX() * v.getX() + w.getY() * v.getY();
        double c2 = v.getX() * v.getX() + v.getY() * v.getY();
        double b = c1 / c2;

        Point Pb = new Point(A.getX()+ b * v.getX(),A.getX()+ b * v.getX());
        if(Pb.getX()>A.getX() && Pb.getX()>B.getX())
        {
            if(A.getX()>B.getX())
                Pb=A;
            else
                Pb=B;
        }
        else if(Pb.getX()<A.getX() && Pb.getX()<B.getX()) {
            if (A.getX() < B.getX())
                Pb = A;
            else
                Pb = B;
        } else if(Pb.getY()>A.getY() && Pb.getY()>B.getY())
        {
            if(A.getY()>B.getY())
                Pb=A;
            else
                Pb=B;
        }
        else if(Pb.getY()<A.getY() && Pb.getY()<B.getY())
        {
            if(A.getY()<B.getY())
                Pb=A;
            else
                Pb=B;
        }
        return Pb;
    }

    public void setSize(double h, double w) {
        double scaleW=(w/h)/(width/height);
        height=h;
        width=w;
        for(Wall wall:walls)
        {
            wall.setxE(wall.getxE()*scaleW);
            wall.setxS(wall.getxS() * scaleW);
        }
        for(Point p:holes)
            p.setX(p.getX()*scaleW);
        endPoint.setX(endPoint.getX()*scaleW);
        startPoint.setX(startPoint.getX()*scaleW);
    }

    void removeLast(char c)
    {
        switch (c)
        {
            case 'w':
                walls.remove(walls.size()-1);
                break;
            case 'e':
                endPoint=null;
                break;
            case 's':
                startPoint=null;
                break;
            case 'h':
                holes.remove(holes.size()-1);
                break;
        }
    }

}
