package com.azaric.tiltmaze;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.azaric.tiltmaze.Dialog.SaveDialog;

import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class NewTerrainActivity extends Activity implements View.OnTouchListener, Controller.MyPlayer {
    Polygon polygon;

    private NewTerrainImageView imageView;

    @Override
    public void play(int i) {

    }

    private enum Option {HOLE, START_POINT, END_POINT, WALL, MOVE, DELETE};
    private Option option=Option.WALL;
    private Menu menu;
    private Stack<Character> typeS=new Stack<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_terrain);
        setTitle("Tilt maze CRETE NEW TERRAIN");

        //create imageView and connect it with model and controller
        polygon=new Polygon(this);
        imageView = (NewTerrainImageView) findViewById(R.id.new_terrain_image_view);
        imageView.setModel(polygon);
        imageView.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (option) {
            case WALL:
                return add_wall(event);
            case END_POINT:
                return add_end_point(event);
            case START_POINT:
                return add_start_point(event);
            case HOLE:
                return add_hole(event);

        }
        return true;

    }


    public boolean add_wall(MotionEvent event)
    {

        int action = event.getActionMasked();

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                imageView.setCurrWall(new Wall(event.getX() / polygon.getHeight(), event.getY() / polygon.getHeight(), event.getX() / polygon.getHeight(), event.getY() / polygon.getHeight()));
                return true;

            case (MotionEvent.ACTION_MOVE) :
                imageView.getCurrWall().setxE(event.getX() / polygon.getHeight());
                imageView.getCurrWall().setyE(event.getY() / polygon.getHeight());
                imageView.invalidate();
                return true;
            case (MotionEvent.ACTION_UP) :
                imageView.getCurrWall().setxE(event.getX() / polygon.getHeight());
                imageView.getCurrWall().setyE(event.getY() / polygon.getHeight());
                if(polygon.checkedWall(imageView.getCurrWall())) {
                    Wall w = imageView.getCurrWall();
                    polygon.addWall(new Wall(Math.min(w.getxS(), w.getxE()), Math.min(w.getyS(), w.getyE()), Math.max(w.getxS(), w.getxE()), Math.max(w.getyS(), w.getyE())));
                    typeS.push('w');
                    menu.findItem(R.id.undo).setEnabled(true);
                }
                imageView.setCurrWall(null);
                imageView.invalidate();

                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    public boolean add_end_point(MotionEvent event)
    {

        int action = event.getActionMasked();

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                imageView.setEndPoint(new Point(event.getX() / polygon.getHeight(), event.getY() / polygon.getHeight()));
                return true;
            case (MotionEvent.ACTION_MOVE) :
                imageView.getEndPoint().setX(event.getX() / polygon.getHeight());
                imageView.getEndPoint().setY(event.getY() / polygon.getHeight());
                imageView.invalidate();
                return true;
            case (MotionEvent.ACTION_UP) :
                imageView.getEndPoint().setX(event.getX() / polygon.getHeight());
                imageView.getEndPoint().setY(event.getY() / polygon.getHeight());
                if(polygon.checkedEndPoint(imageView.getEndPoint())) {
                    polygon.setEndPoint(imageView.getEndPoint());
                    ((MenuItem)menu.findItem(R.id.add_end_point)).setEnabled(false);
                    ((MenuItem)menu.findItem(R.id.add_wall)).setChecked(true);
                    option=Option.WALL;
                    typeS.push('e');
                    menu.findItem(R.id.undo).setEnabled(true);
                    if(!menu.findItem(R.id.add_start_point).isEnabled())
                        menu.findItem(R.id.save).setEnabled(true);
                }
                imageView.setEndPoint(null);
                imageView.invalidate();
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    public boolean add_start_point(MotionEvent event)
    {

        int action = event.getActionMasked();

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                imageView.setStartPoint(new Point(event.getX() / polygon.getHeight(), event.getY() / polygon.getHeight()));
                return true;
            case (MotionEvent.ACTION_MOVE):
                imageView.getStartPoint().setX(event.getX() / polygon.getHeight());
                imageView.getStartPoint().setY(event.getY() / polygon.getHeight());
                imageView.invalidate();
                return true;
            case (MotionEvent.ACTION_UP) :
                imageView.getStartPoint().setX(event.getX() / polygon.getHeight());
                imageView.getStartPoint().setY(event.getY() / polygon.getHeight());
                if(polygon.checkedStartPoint(imageView.getStartPoint())) {
                    polygon.setStartPoint(imageView.getStartPoint());
                    ((MenuItem)menu.findItem(R.id.add_start_point)).setEnabled(false);
                    ((MenuItem)menu.findItem(R.id.add_wall)).setChecked(true);
                    option=Option.WALL;
                    imageView.invalidate();
                    typeS.push('s');
                    menu.findItem(R.id.undo).setEnabled(true);
                    if(!menu.findItem(R.id.add_end_point).isEnabled())
                        menu.findItem(R.id.save).setEnabled(true);
                }
                imageView.setStartPoint(null);
                imageView.invalidate();
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    public boolean add_hole(MotionEvent event)
    {

        int action = event.getActionMasked();

        switch(action) {
            case (MotionEvent.ACTION_DOWN):
                imageView.setHole(new Point(event.getX() / polygon.getHeight(), event.getY() / polygon.getHeight()));
                return true;
            case (MotionEvent.ACTION_MOVE) :
                imageView.getHole().setX(event.getX() / polygon.getHeight());
                imageView.getHole().setY(event.getY() / polygon.getHeight());
                imageView.invalidate();
                return true;
            case (MotionEvent.ACTION_UP):
                imageView.getHole().setX(event.getX() / polygon.getHeight());
                imageView.getHole().setY(event.getY() / polygon.getHeight());
                if(polygon.checkedHole(imageView.getHole())) {
                    polygon.addHole(imageView.getHole());
                    typeS.push('h');
                    menu.findItem(R.id.undo).setEnabled(true);
                }
                imageView.setHole(null);
                imageView.invalidate();

                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_terrain_menu, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.add_hole:
                option=Option.HOLE;
                return true;
            case R.id.add_wall:
                option=Option.WALL;
                return true;
            case R.id.add_start_point:
                option=Option.START_POINT;
                return true;
            case R.id.add_end_point:
                option=Option.END_POINT;
                return true;
            case R.id.move:
                option=Option.MOVE;
                return true;
            case R.id.delete:
                option=Option.DELETE;
                return true;
            case R.id.undo:
                undo();
                return true;
            case R.id.save:
                return save();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void undo() {
        char c=typeS.pop();
        polygon.removeLast(c);
        if(typeS.isEmpty())
            menu.findItem(R.id.undo).setEnabled(false);
        if(c=='s') {
            menu.findItem(R.id.add_start_point).setEnabled(true);
            menu.findItem(R.id.save).setEnabled(false);
        }
        if(c=='e'){
            menu.findItem(R.id.add_end_point).setEnabled(true);
            menu.findItem(R.id.save).setEnabled(false);
        }
        imageView.invalidate();
    }

    public Polygon getPolygon(){ return polygon; }

    public boolean save(){
        //prikazi dialog
        DialogFragment saveDialog = new SaveDialog();
        saveDialog.show(getFragmentManager(), "saveDialog");
        return true;
    }
}
