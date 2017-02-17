package com.azaric.tiltmaze;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Stefan on 14-Feb-17 | 15:36.
 * Created in project with name: "Tiltmaze"
 */
public class Model {
    private int height;
    private int width;
    private boolean longClickMode=false;
    private double xT, yT;
    final int num = 1;
    int index;
    private Circle[] circles=new Circle[num];


    double velocityX = 0, velocityY = 0;

    private int accTimeFactor = 1000000;
    private int velTimeFactor = 100000;

    public Model()
    {
        for(int i=0; i<num; i++)
        {
            circles[i]=new Circle(
                    ThreadLocalRandom.current().nextDouble(0.1,0.9),
                    ThreadLocalRandom.current().nextDouble(0.3,0.7),
                    ThreadLocalRandom.current().nextDouble(0,0.1));
        }
        index=-1;
    }

    public Circle getCircles(int i) { return circles[i]; }

    public int getWidth() { return width; }

    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }

    public int getR() {
        if(height>width)
            return width;
        return height;
    }

    public void setHeight(int height) { this.height = height; }

    public void actionDown(float x, float y, int id) {
        for(int i = 0; i < num; i++){
            if( Math.pow(height * circles[i].y - y,2) + Math.pow(width * circles[i].x - x,2) < Math.pow(getR() * circles[i].r, 2)) {
                circles[i].setTouched(true);
                circles[i].setyT(height * circles[i].y - y);
                circles[i].setxT(width * circles[i].x - x);
                circles[i].setId(id);
            }
        }
    }

    public void actionMove(float[] currentCoordinates, float[] ids) {

        for(int i = 0; i < num; i++){
            if(circles[i].touched)
                for( int j = 0; j < ids.length; j++){
                    if(circles[i].getId() == ids[j]){
                        circles[i].setX((currentCoordinates[2 * j] + circles[i].getxT()) / width);
                        circles[i].setY((currentCoordinates[ 2*j+1] + circles[i].getyT()) / height);
                        // Log.d("S"," " + circles[i].getX() + " " + circles[i].getY());
                    }
                }
        }
    }

    public void actionUp(float x, float y, int id) {
        for(int i = 0; i < num; i++){
            if(circles[i].getId() == id) {
                circles[i].setTouched(false);
            }
        }
    }

    public void findNearestCircle(float x, float y){
        index = 0;
        double distance = height * width;
        for(int i = 0; i < num; i++){
            double d = Math.pow(height * circles[i].y - y,2) + Math.pow(width * circles[i].x - x,2) - Math.pow(getR() * circles[i].r, 2);
            if(d < distance) {
                distance=d;
                index=i;
            }
        }
        Log.d("fff","LONG_CLK");
    }

    public void longClick(float x, float y)
    {
        if(Math.pow(x-xT,2)+Math.pow(y-yT,2)>0.05*getR()) longClickMode=false;
    }

    public void setLongClick(float x, float y)
    {
        index = -1;
        xT=x;
        yT=y;
        longClickMode=true;
    }

    public void endLongClick(float x, float y)
    {
        if(longClickMode)
            findNearestCircle(x,y);
        longClickMode=false;
    }

    public void endLongClick() { longClickMode=false; }

    public void setVelocity(float x, float y, long delta) {

        if(index!=-1) {
            velocityX = velocityX /1.5+ x * delta / accTimeFactor/accTimeFactor;
            velocityY = velocityY /1.5+ y * delta / accTimeFactor/accTimeFactor;
            Log.d("BRZINA","x: " +  x + "y: " + y + " delta: " + delta);
            Log.d("BRZINA","vX: " + velocityX + " x: " + velocityY);
            circles[index].x -= velocityX * delta/accTimeFactor;
            circles[index].y -= velocityY * delta/accTimeFactor;

            if (circles[index].x > 1)
                circles[index].x = 1;
            if (circles[index].x < 0)
                circles[index].x = 0;

            if (circles[index].y > 1)
                circles[index].y = 1;
            if (circles[index].y < 0)
                circles[index].y = 0;

        }
        else
        {
            velocityX = velocityY =0;
        }


    }

    /**
     * Ova metoda cuva trenutni crtez kao fajl sa imenom nameOfTheDrawing
     * @param nameOfTheDrawing Ime crteza (ime fajla) kako ce se cuvati
     * @param context Kontekst zbog pristupa fajlovima
     */
    public void saveDrawing(String nameOfTheDrawing, Context context) {
        File file = new File(context.getFilesDir(), nameOfTheDrawing);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);

            writer.append(""+circles.length+"\n");
            for (Circle circle : circles) {
                String c = circle.x + ":" + circle.y + ":" + circle.r + "\n";
                writer.append(c);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ova metoda ucitava iz fajla koordinate krugova i update-uje koordinate krugova
     * @param nameOfDrawingToLoad ime crteza koji se ucitava
     * @param context kontekst za trazenje fajla
     */
    public void loadDrawing(String nameOfDrawingToLoad, Context context) {
        File file = new File(context.getFilesDir(), nameOfDrawingToLoad);
        FileReader reader = null;
        try{
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String numOfCircles = bufferedReader.readLine();
            int numOfCirclesInt = Integer.parseInt(numOfCircles);

            for(int i = 0; i < numOfCirclesInt; i++) {
                String oneCircle = bufferedReader.readLine();
                if (oneCircle == null)
                    break;
                String[] array = oneCircle.split(":");
                Log.d("ARRAY", "X: " + array[0] + " Y: " + array[1] + " R: " + array[2]);
                String loadedX = array[0];
                String loadedY = array[1];
                String loadedR = array[2];

                //parse double
                double tmpX = Double.parseDouble(loadedX);
                double tmpY = Double.parseDouble(loadedY);
                double tmpR = Double.parseDouble(loadedR);

                //update drawing coordinates
                circles[i].x = tmpX;
                circles[i].y = tmpY;
                circles[i].r = tmpR;

            }


        } catch (NumberFormatException nfe){
            nfe.printStackTrace();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


