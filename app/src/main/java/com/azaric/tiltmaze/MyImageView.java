package com.azaric.tiltmaze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Stefan on 1/15/17.
 */
public class MyImageView extends ImageView{


    Controller controller;
    Polygon model;
    Paint paintRed=new Paint();
    Paint paintOrange=new Paint();
    Paint paintGreen=new Paint();
    Paint paintWhite=new Paint();
    Paint paintGray=new Paint();


    Paint paint=new Paint();
    Paint paintBlue=new Paint();

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

    public MyImageView(Context context, AttributeSet attrs) { super(context, attrs); }

    public MyImageView(Context context) { super(context); }

    public Controller getController() { return controller; }

    public void setController(GameActivity activity, Controller controller) { this.controller = controller; }

    public Polygon getModel() { return model; }

    public void setModel(Polygon model) { this.model = model; }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(model!=null) {
            model.setSize(h,w);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("draw", "draw");
        paintRed.setColor(Color.RED);
        paintGreen.setColor(Color.GREEN);
        paintWhite.setColor(Color.YELLOW);
        paintOrange.setColor(Color.MAGENTA);
        paintGray.setColor(Color.BLACK);

        float width=(float)model.getWidth();
        float height=(float)model.getHeight();

        for (Wall wall : model.getWalls())
            canvas.drawRect((float) wall.getxS() * height, (float) wall.getyS() * height,
                    (float) wall.getxE() * height, (float) wall.getyE() * height, paintGray);

        for(Point h:model.getHoles())
        {
            canvas.drawCircle((float) h.getX() * height, (float) h.getY() * height, (float) model.getR() * height, paintOrange);
        }
        if(model.getEndPoint()!=null)
            canvas.drawCircle((float)model.getEndPoint().getX()*height, (float)model.getEndPoint().getY()*height,
                    (float)model.getR()*height,paintGreen);
        if(model.getStartPoint()!=null)
            canvas.drawCircle((float)model.getStartPoint().getX()*height, (float)model.getStartPoint().getY()*height,
                    (float)model.getrBall()*height,paintWhite);
    }


}
