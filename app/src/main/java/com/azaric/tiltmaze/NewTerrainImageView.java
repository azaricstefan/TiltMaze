package com.azaric.tiltmaze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Stefan on 1/15/17.
 */
public class NewTerrainImageView extends ImageView {

    Polygon model;
    Paint paintRed=new Paint();
    Paint paintOrange=new Paint();
    Paint paintGreen=new Paint();
    Paint paintWhite=new Paint();
    Paint paintGray=new Paint();

    private Point startPoint;
    private Point endPoint;
    private Point hole;

    private Wall currWall=null;

    public NewTerrainImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewTerrainImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewTerrainImageView(Context context) {
        super(context);
    }

    public Polygon getModel() {
        return model;
    }

    public void setModel(Polygon model) {
        this.model = model;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(model!=null) {
            model.setHeight(h);
            model.setWidth(w);
            if(model.getWalls().size()==0)
                model.makePolygon(h,w);
            invalidate();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("draw", "draw");
        paintRed.setColor(Color.RED);
        paintGreen.setColor(Color.GREEN);
        paintWhite.setColor(Color.YELLOW);
        paintOrange.setColor(Color.MAGENTA);
        paintGray.setColor(Color.BLACK);

        float width=(float)model.getHeight();
        float height=(float)model.getHeight();

        for (Wall wall : model.getWalls()) {
            canvas.drawRect((float) wall.getxS() * width, (float) wall.getyS() * height,
                    (float) wall.getxE() * width, (float) wall.getyE() * height, paintGray);

        }

        for(Point h:model.getHoles())
        {
            canvas.drawCircle((float) h.getX() * width, (float) h.getY() * height, (float) model.getR() * height, paintOrange);
        }
        if(model.getGoal()!=null)
            canvas.drawCircle((float)model.getGoal().getX()*width, (float)model.getGoal().getY()*height,
                    (float)model.getR()*height,paintGreen);
        if(model.getBall()!=null)
            canvas.drawCircle((float)model.getBall().getX()*width, (float)model.getBall().getY()*height,
                    (float)model.getrBall()*height,paintWhite);


        if(currWall!=null)
            if(model.checkedWall(currWall))
                canvas.drawRect((float)(Math.min(currWall.getxS(),currWall.getxE())*width),(float)Math.min(currWall.getyS(), currWall.getyE())*height,
                        (float)Math.max(currWall.getxS(), currWall.getxE())*width,(float) Math.max(currWall.getyS(), currWall.getyE())*height, paintGray);
            else
                canvas.drawRect((float)(Math.min(currWall.getxS(),currWall.getxE())*width),(float)Math.min(currWall.getyS(), currWall.getyE())*height,
                        (float)Math.max(currWall.getxS(), currWall.getxE())*width,(float) Math.max(currWall.getyS(), currWall.getyE())*height, paintRed);
        if(hole!=null)
            if(model.checkedHole(hole))
                canvas.drawCircle((float) hole.getX() * width, (float) hole.getY() * height, (float) model.getR() * height, paintOrange);
            else
                canvas.drawCircle((float) hole.getX() * width, (float) hole.getY() * height, (float) model.getR() * height, paintRed);

        if (startPoint != null)
            if (model.checkedStartPoint(startPoint))
                canvas.drawCircle((float) startPoint.getX() * width, (float) startPoint.getY() * height, (float) model.getrBall() * height, paintWhite);
            else
                canvas.drawCircle((float) startPoint.getX() * width, (float) startPoint.getY() * height, (float) model.getrBall() * height, paintRed);

        if(endPoint!=null)
            if(model.checkedEndPoint(endPoint))
                canvas.drawCircle((float) endPoint.getX() * width, (float) endPoint.getY() * height, (float) model.getR() * height, paintGreen);
            else
                canvas.drawCircle((float) endPoint.getX() * width, (float) endPoint.getY() * height, (float) model.getR() * height, paintRed);

    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
        invalidate();
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public Wall getCurrWall() {
        return currWall;
    }

    public void setCurrWall(Wall currWall) {
        this.currWall = currWall;
    }

    public Point getHole() {
        return hole;
    }

    public void setHole(Point hole) {
        this.hole = hole;
    }
}
