package com.azaric.tiltmaze;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Stefan on 14-Feb-17 | 19:34.
 * Created in project with name: "Tiltmaze"
 */

public class Controller {
    Model model;
    MyImageView imageView;
    private long lastMesurement;
    public String nameOfDrawingToLoad;

    @CheckResult
    public Model getModel() {
        return model;
    }

    @NonNull
    public void setModel(@NonNull Model model) {
        this.model = model;
    }

    public MyImageView getImageView() {
        return imageView;
    }

    public void setImageView(MyImageView imageView) {
        this.imageView = imageView;
    }

    public void actionDown(float x, float y, int id){
        model.actionDown(x,y,id);
    }

    public void actionMove(float[] currentCoordinates, float[] IDS) {
        model.actionMove(currentCoordinates, IDS);
        imageView.invalidate();

    }
    public void actionUp(float x, float y, int id){
        model.actionUp(x,y,id);
        imageView.invalidate();
    }

    public void longClick(float x, float y)
    {
        model.longClick(x,y);
    }
    public void setLongClick(float x, float y)
    {
        model.setLongClick(x,y);
    }
    public void endLongClick(float x, float y)
    {
        model.endLongClick(x,y);
        lastMesurement = 0;
        model.velocityX =model.velocityY =0;
    }
    public void endLongClick()
    {
        model.endLongClick();
        lastMesurement = 0;
        model.velocityX =model.velocityY =0;
    }
    public void addNewAccelerometerValues(float x, float y, long time) {
        if (model.index != -1) {
            if (lastMesurement != 0) {
                Log.d("dd",""+time);
                long delta = time-lastMesurement;
                model.setVelocity(x, -y, delta);
            }
            lastMesurement = time;
            imageView.invalidate();
        }
    }

    /**
     * Ova metoda cuva trenutni crtez kao fajl sa imenom nameOfTheDrawing
     * @param nameOfTheDrawing Ime crteza (ime fajla) kako ce se cuvati
     * @param context Kontekst zbog pristupa fajlovima
     */
    public void saveDrawing(String nameOfTheDrawing, Context context) {
        model.saveDrawing(nameOfTheDrawing, context);
    }

    public void saveDrawing(Context context) {
        model.saveDrawing(nameOfDrawingToLoad, context);
    }

    /**
     * Ova metoda ucitava iz fajla koordinate krugova i update-uje koordinate krugova
     * @param nameOfDrawingToLoad ime crteza koji se ucitava
     * @param context kontekst za trazenje fajla
     */
    public void loadDrawing(String nameOfDrawingToLoad, Context context) {
        model.loadDrawing(nameOfDrawingToLoad, context);
    }
}
