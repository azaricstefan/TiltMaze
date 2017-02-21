package com.azaric.tiltmaze;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Stefan on 2/16/17 | 01:48.
 * Created in project with name: "Tiltmaze"
 */
public class Polygon {
    private Controller.MyPlayer myPlayer;
    private double height;
    private double width;
    private Vector<Wall> walls = new Vector<>();
    private Vector<Point> holes = new Vector<>();
    private Point ball;
    private Point goal;
    private double r = 0.05;
    private double rBall = 0.04;
    static double w = 0.02;
    private boolean finish;
    private boolean win;

    private final Context context;
    private long gameTime;

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public Polygon(Controller.MyPlayer myPlayer, Context context) {
        this.myPlayer = myPlayer;
        this.context = context;
    }

    public void makePolygon(double height, double width) {
        this.height = height;
        this.width = width;
        walls.add(new Wall(width / height - w, 0, width / height, 1));
        walls.add(new Wall(0, 0, w, 1));
        walls.add(new Wall(0, 1 - w, width / height, 1));
        walls.add(new Wall(0, 0, width / height, w));
    }

    boolean checkedIntersection(Wall wall, Point point, double r) {
        if (wall.getxS() < point.getX() &&
                wall.getxE() > point.getX() &&
                wall.getyS() - r < point.getY() &&
                wall.getyE() + r > point.getY())
            return true;
        if (wall.getxS() - r < point.getX() &&
                wall.getxE() + r > point.getX() &&
                wall.getyS() < point.getY() &&
                wall.getyE() > point.getY())
            return true;
        if (dist(point, new Point(wall.getxE(), wall.getyE())) < r)
            return true;
        if (dist(point, new Point(wall.getxE(), wall.getyS())) < r)
            return true;
        if (dist(point, new Point(wall.getxS(), wall.getyE())) < r)
            return true;
        if (dist(point, new Point(wall.getxS(), wall.getyS())) < r)
            return true;
        return false;
    }

    public boolean checkedWall(Wall w) {
        Wall wall = new Wall(Math.min(w.getxS(), w.getxE()), Math.min(w.getyS(), w.getyE()),
                Math.max(w.getxS(), w.getxE()), Math.max(w.getyS(), w.getyE()));
        for (Point h : holes)
            if (checkedIntersection(wall, h, r))
                return false;
        if (goal != null && checkedIntersection(wall, goal, r))
            return false;
        if (ball != null && checkedIntersection(wall, ball, rBall))
            return false;
        return true;
    }

    public boolean checkedStartPoint(Point point) {
        for (Wall wall : walls)
            if (checkedIntersection(wall, point, rBall))
                return false;
        for (Point h : holes)
            if (dist(h, point) < rBall + r)
                return false;
        if (goal != null && dist(goal, point) < rBall + r)
            return false;
        return true;
    }

    public boolean checkedEndPoint(Point point) {
        for (Wall wall : walls)
            if (checkedIntersection(wall, point, r))
                return false;
        for (Point h : holes)
            if (dist(h, point) < r + r)
                return false;
        if (ball != null && dist(ball, point) < rBall + r)
            return false;
        return true;
    }

    public boolean checkedHole(Point point) {
        for (Wall wall : walls)
            if (checkedIntersection(wall, point, r))
                return false;
        for (Point h : holes)
            if (dist(h, point) < r + r)
                return false;
        if (goal != null && dist(goal, point) < r + r)
            return false;
        if (ball != null && dist(ball, point) < rBall + r)
            return false;
        return true;
    }

    public void addWall(Wall w) {
        walls.add(w);
    }

    public void addHole(Point h) {
        holes.add(h);
    }

    public Point getGoal() {
        return goal;
    }

    public void setGoal(Point goal) {
        this.goal = goal;
    }

    public Point getBall() {
        return ball;
    }

    public void setBall(Point ball) {
        this.ball = ball;
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
        double scaleW = (width / height);
        height = 1;
        width = 1;
        for (Wall wall : walls) {
            wall.setxE(wall.getxE() / scaleW);
            wall.setxS(wall.getxS() / scaleW);
        }
        for (Point p : holes)
            p.setX(p.getX() / scaleW);
        goal.setX(goal.getX() / scaleW);
        ball.setX(ball.getX() / scaleW);

        File file = new File(context.getExternalFilesDir(null), name);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);

            //START POINT
            double x = ball.getX();
            double y = ball.getY();
            String stringStartPoint = x + ":" + y + "\n";
            writer.append(stringStartPoint);

            //END POINT
            x = goal.getX();
            y = goal.getY();
            String stringEndPoint = x + ":" + y + "\n";
            writer.append(stringEndPoint);

            //HOLE
            String sizeOfHoles = "" + holes.size() + "\n";
            writer.append(sizeOfHoles);
            for (Point p : holes) {
                x = p.getX();
                y = p.getY();
                String stringHole = x + ":" + y + "\n";
                writer.append(stringHole);
            }

            //WALL
            String sizeOfWalls = "" + walls.size() + "\n";
            writer.append(sizeOfWalls);
            for (Wall w : walls) {
                double xS = w.getxS();
                double xE = w.getxE();
                double yS = w.getyS();
                double yE = w.getyE();
                String stringWall = xS + ":" + xE + ":" + yS + ":" + yE + "\n";
                writer.append(stringWall); //WALL
            }

            //TIME
            writer.append(gameTime + "\n");

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPolygonFromFile(String name, Context context) {
        width = height = 1;
        File file = new File(context.getExternalFilesDir(null), name);
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            //START POINT READ
            String startPoint = bufferedReader.readLine();
            String[] startPointArray = startPoint.split(":");
            this.ball = new Point();
            this.ball.setX(Double.parseDouble(startPointArray[0]));
            this.ball.setY(Double.parseDouble(startPointArray[1]));

            //END POINT READ
            String endPoint = bufferedReader.readLine();
            String[] endPointArray = endPoint.split(":");
            this.goal = new Point();
            this.goal.setX(Double.parseDouble(endPointArray[0]));
            this.goal.setY(Double.parseDouble(endPointArray[1]));

            //READ HOLES
            String sizeOfHolesString = bufferedReader.readLine();
            int sizeOfHoles = Integer.parseInt(sizeOfHolesString);

            for (int i = 0; i < sizeOfHoles; i++) {
                String hole = bufferedReader.readLine();
                if (hole == null)
                    break;
                String[] holeArray = hole.split(":");
                Log.d("HOLE_ARRAY", "X: " + holeArray[0] + " Y: " + holeArray[1]);
                String loadedX = holeArray[0];
                String loadedY = holeArray[1];
                Point h = new Point(Double.parseDouble(loadedX), Double.parseDouble(loadedY));
                holes.add(h);
            }

            //READ WALLS
            String sizeOfWallsString = bufferedReader.readLine();
            int sizeOfWalls = Integer.parseInt(sizeOfWallsString);

            for (int i = 0; i < sizeOfWalls; i++) {
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
                        Double.parseDouble(loadedXstart), Double.parseDouble(loadedYstart),
                        Double.parseDouble(loadedXend), Double.parseDouble(loadedYend));
                walls.add(w);
            }

            //READ TIME
            String tmpString = bufferedReader.readLine();
            if (tmpString != null)
                gameTime = Long.parseLong(tmpString);


        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPolygon(String name, Context context) {
        width = height = 1;

        goal = new Point(0.8, 0.8);
        ball = new Point(0.5, 0.5);
        for (int i = 1; i < 5; i++)
            holes.add(new Point(i * 0.2, 1 - i * 0.2));
        walls.add(new Wall(1 - w, 0, 1, 1));
        walls.add(new Wall(0, 0, w, 1));
        walls.add(new Wall(0, 1 - w, 1, 1));
        walls.add(new Wall(0, 0, 1, w));
    }


    double velocityX = 0, velocityY = 0;

    private double traction = 0.2;
    private double collision = 0.7;
    private double accTimeFactor = 1000000;
    private double g = 9.81;

    public void setVelocity(float y, float x, long delta) {
        //get data from settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        traction = Double.parseDouble(sharedPreferences.getString(context.getString(R.string.preference_traction_key), "0.2"));
        collision = Double.parseDouble(sharedPreferences.getString(context.getString(R.string.preference_collision), "0.7"));
        g = Double.parseDouble(sharedPreferences.getString(context.getString(R.string.gravity), "9.81"));

        velocityX = velocityX + x * delta / width / accTimeFactor / g / g;
        velocityY = velocityY + y * delta / height / accTimeFactor / g / g;

        velocityX *= 1 - traction;
        velocityY *= 1 - traction;
        //Log.d("BRZINA", "x: " + x + "y: " + y + " delta: " + delta);
        //Log.d("BRZINA", "vX: " + velocityX + " x: " + velocityY);
        Point lastPoint = new Point(ball.getX(), ball.getY());
        ball.setX(ball.getX() + velocityX * delta / accTimeFactor);
        ball.setY(ball.getY() + velocityY * delta / accTimeFactor);

        for (Point h : holes) {
            Point nh = pointToLineDistance(lastPoint, ball, h);
            double cr = dist(nh, h);
            if (cr < r * 0.9) {

                if (cr + rBall > r) {
                    nh.setX(h.getX() + (nh.getX() - h.getX()) * (r - rBall) / cr);
                    nh.setY(h.getY() + (nh.getY() - h.getY()) * (r - rBall) / cr);
                }
                ball = nh;
                Log.d("kraj", "kraj igre");
                finish = true;
                win = false;
                myPlayer.play(2);
                //
                return;
            }

        }
        Point nh = pointToLineDistance(lastPoint, ball, goal);
        double cr = dist(nh, goal);
        if (cr < r) {
            if (cr + rBall > r) {
                nh.setX(goal.getX() + (nh.getX() - goal.getX()) * (r - rBall) / cr);
                nh.setY(goal.getY() + (nh.getY() - goal.getY()) * (r - rBall) / cr);
            }
            ball = nh;
            finish = true;
            win = true;
            myPlayer.play(1);
            //dialog
            return;
        }
        Log.d("loptica", "noviKrug");
        for (Wall w : walls) {
            if (w.getxS() - rBall + 0.001 > lastPoint.getX() && w.getxS() - rBall - 0.001 < ball.getX()) {
                double yy = lastPoint.getY() + (ball.getY() - lastPoint.getY()) * (w.getxS() - rBall - lastPoint.getX()) / (ball.getX() - lastPoint.getX());
                if (yy > w.getyS() - rBall && yy < w.getyE() + rBall) {
                    ball.setX(ball.getX() - 2 * (ball.getX() - w.getxS() + rBall));
                    if (ball.getX() < w.getxS() - rBall - 0.001)
                        ball.setX(w.getxS() - rBall - 0.001);
                    velocityX = -velocityX * collision;
                    velocityY *= collision;
                    myPlayer.play(0);
                    Log.d("loptica", "xS");
                }
            }
            if (w.getxE() + rBall - 0.001 < lastPoint.getX() && w.getxE() + rBall + 0.001 > ball.getX()) {
                double yy = lastPoint.getY() + (ball.getY() - lastPoint.getY()) * (w.getxE() + rBall - lastPoint.getX()) / (ball.getX() - lastPoint.getX());
                if (yy > w.getyS() - rBall && yy < w.getyE() + rBall) {
                    ball.setX(ball.getX() - 2 * (ball.getX() - w.getxE() - rBall));
                    velocityX = -velocityX * collision;
                    if (ball.getX() < w.getxE() + rBall + 0.001)
                        ball.setX(w.getxE() + rBall + 0.001);
                    velocityY *= collision;
                    myPlayer.play(0);
                    Log.d("loptica", "xE");
                }
            }
            if (w.getyS() - rBall + 0.001 > lastPoint.getY() && w.getyS() - rBall - 0.001 < ball.getY()) {
                double xx = lastPoint.getX() + (ball.getX() - lastPoint.getX()) * (w.getyS() - rBall - lastPoint.getY()) / (ball.getY() - lastPoint.getY());
                if (xx > w.getxS() - rBall && xx < w.getxE() + rBall) {
                    ball.setY(ball.getY() - 2 * (ball.getY() - w.getyS() + rBall));
                    if (ball.getY() > w.getyS() - rBall - 0.001)
                        ball.setY(w.getyS() - rBall - 0.001);
                    velocityY = -velocityY * collision;
                    velocityX *= collision;
                    myPlayer.play(0);
                    Log.d("loptica", "yS");
                }
            }
            if (w.getyE() + rBall - 0.001 < lastPoint.getY() && w.getyE() + rBall + 0.001 > ball.getY()) {
                double xx = lastPoint.getX() + (ball.getX() - lastPoint.getX()) * (w.getyS() + rBall - lastPoint.getY()) / (ball.getY() - lastPoint.getY());
                if (xx > w.getxS() - rBall && xx < w.getxE() + rBall) {
                    ball.setY(ball.getY() - 2 * (ball.getY() - w.getyE() - rBall));
                    if (ball.getY() < w.getyE() + rBall + 0.001)
                        ball.setY(w.getyE() + rBall + 0.001);
                    velocityY = -velocityY * collision;
                    velocityX *= collision;
                    myPlayer.play(0);
                    Log.d("loptica", "yE");
                }
            }
        }
        for (Wall wall : walls)
            if (checkedIntersection(wall, ball, rBall)) {
                ball = lastPoint;
                Log.d("loptica", "reset");
            }
    }

    public double dist(Point A, Point B) {
        return Math.sqrt((A.getX() - B.getX()) * (A.getX() - B.getX()) + (A.getY() - B.getY()) * (A.getY() - B.getY()));
    }

    public Point pointToLineDistance(Point A, Point B, Point P) {
        Point v = new Point(B.getX() - A.getX(), B.getY() - A.getY());
        Point w = new Point(P.getX() - A.getX(), P.getY() - A.getY());

        double c1 = w.getX() * v.getX() + w.getY() * v.getY();
        double c2 = v.getX() * v.getX() + v.getY() * v.getY();
        double b = c1 / c2;

        Point Pb = new Point(A.getX() + b * v.getX(), A.getX() + b * v.getX());
        if (Pb.getX() > A.getX() && Pb.getX() > B.getX()) {
            if (A.getX() > B.getX())
                Pb = A;
            else
                Pb = B;
        } else if (Pb.getX() < A.getX() && Pb.getX() < B.getX()) {
            if (A.getX() < B.getX())
                Pb = A;
            else
                Pb = B;
        } else if (Pb.getY() > A.getY() && Pb.getY() > B.getY()) {
            if (A.getY() > B.getY())
                Pb = A;
            else
                Pb = B;
        } else if (Pb.getY() < A.getY() && Pb.getY() < B.getY()) {
            if (A.getY() < B.getY())
                Pb = A;
            else
                Pb = B;
        }
        return Pb;
    }

    public void setSize(double h, double w) {
        double scaleW = (w / h) / (width / height);
        height = h;
        width = w;
        for (Wall wall : walls) {
            wall.setxE(wall.getxE() * scaleW);
            wall.setxS(wall.getxS() * scaleW);
        }
        for (Point p : holes)
            p.setX(p.getX() * scaleW);
        goal.setX(goal.getX() * scaleW);
        ball.setX(ball.getX() * scaleW);
    }

    void removeLast(char c) {
        switch (c) {
            case 'w':
                walls.remove(walls.size() - 1);
                break;
            case 'e':
                goal = null;
                break;
            case 's':
                ball = null;
                break;
            case 'h':
                holes.remove(holes.size() - 1);
                break;
        }
    }

    public boolean isGameOver() {
        return finish;
    }

    public boolean isWin() {
        return win;
    }
}
