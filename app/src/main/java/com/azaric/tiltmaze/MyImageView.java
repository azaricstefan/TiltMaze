package com.azaric.tiltmaze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Stefan on 1/15/17.
 */
public class MyImageView extends ImageView{

    private static final String DEBUG_TAG = "TAG";
    Controller controller;
    Model model;
    Paint paint=new Paint();
    Paint paintBlue=new Paint();
    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context) {
        super(context);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(GameActivity activity, Controller controller) {
        this.controller = controller;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        model.setHeight(h);
        model.setWidth(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        paintBlue.setColor(Color.BLUE);

        for(int i=0; i<model.num; i++)
        {
            canvas.drawCircle(
                    (float)model.getCircles(i).getX()*model.getWidth(),
                    (float)model.getCircles(i).getY()*model.getHeight(),
                    (float)model.getCircles(i).getR()*model.getR(),
                    paint);
        }

        if(model.index != -1) {
            Circle circle = model.getCircles(model.index);
            if(circle.y - circle.r == 0)
                circle.y = circle.r;
            if(circle.x - circle.r == 0) circle.x = circle.r;

            canvas.drawCircle(
                    (float) circle.getX() * model.getWidth(),
                    (float) circle.getY() * model.getHeight(),
                    (float) circle.getR() * model.getR(),
                    paintBlue);
        }
    }

    protected float[] fillCurrentCoordinates(MotionEvent event) {
        float[] coords = new float[2 * event.getPointerCount()];

        for (int pointerNum = 0; pointerNum < event.getPointerCount(); pointerNum++) {
            coords[2 * pointerNum] = event.getX(pointerNum);
            coords[2 * pointerNum + 1] = event.getY(pointerNum);
        }
        return  coords;
    }

    protected float[] fillID(MotionEvent event) {
        float[] ids = new float[event.getPointerCount()];

        for (int pointerNum = 0; pointerNum < event.getPointerCount(); pointerNum++){
            ids[pointerNum] = event.getPointerId(pointerNum);
        }
        return ids;
    }
}
