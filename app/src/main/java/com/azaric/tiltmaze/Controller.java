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
    Polygon model;
    MyImageView imageView;
    private long lastMesurement=-1;
    String nameOfPolygonToLoad;

    @CheckResult
    public Polygon getModel() {
        return model;
    }

    @NonNull
    public void setModel(Polygon model) {
        this.model = model;
    }

    public MyImageView getImageView() {
        return imageView;
    }

    public void setImageView(MyImageView imageView) {
        this.imageView = imageView;
    }


    public String getNameOfPolygonToLoad() {
        return nameOfPolygonToLoad;
    }

    public void addNewAccelerometerValues(float x, float y, long time) {
        if(lastMesurement==-1)
        {
            lastMesurement=time;
        }
        else
        {
            long delta = time-lastMesurement;
            model.setVelocity(x, y, delta);
            lastMesurement = time;
            imageView.invalidate();
        }
    }


    public void loadPolygon(String name, Context context) {
        model.loadPolygon(name, context);
    }
}
