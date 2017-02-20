package com.azaric.tiltmaze;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.azaric.tiltmaze.DB.DbOperationsHelper;
import com.azaric.tiltmaze.Dialog.NameDialog;
import com.azaric.tiltmaze.Dialog.SaveDialog;

public class GameActivity extends Activity
        implements
        SensorEventListener, Controller.MyPlayer {

    Controller controller;
    Polygon model;
    Intent services;

    SensorManager sensorManager;
    Sensor accelerometer;
    DbOperationsHelper dbOperationsHelper;

    private long startTime;
    private long endTime;
    private float gameTime = 0;
    private float score;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        startTime = System.currentTimeMillis();

        //create model and controller
        controller = new Controller();
        model = new Polygon(this);
        controller.setModel(model);

        //create imageView and connect it with model and controller
        MyImageView imageView = (MyImageView) findViewById(R.id.my_image_view);
        imageView.setController(this,controller);
        controller.setImageView(imageView);
        imageView.setModel(model);

        dbOperationsHelper = new DbOperationsHelper(this);

        //SENSOR CODE
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        //GET intent - which polygon to load
        //TODO: get info about polygon and load it
        Intent intent = getIntent();
        controller.nameOfPolygonToLoad = intent.getStringExtra(MainActivity.NAME_OF_POLYGON);
        if (controller.nameOfPolygonToLoad != null) {
            model.loadPolygonFromFile(controller.nameOfPolygonToLoad,this);
        } else
            controller.loadPolygon("", getApplicationContext());



    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    //SENSOR METHODS
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        long time = event.timestamp;

        controller.addNewAccelerometerValues(x, y, time);
        boolean gameOver = model.isGameOver(); //TODO: returns true
        if(gameOver && firstTime){
            if(model.isWin()) {
                firstTime = false;
                endTime = System.currentTimeMillis();
                score = getGameTime();
                Toast t;
                t = Toast.makeText(this,"Pobedili ste",Toast.LENGTH_LONG);
                t.show();
                //otvori dialog
                DialogFragment nameDialog = new NameDialog();
                nameDialog.show(getFragmentManager(), "nameDialog");
            } else {
                Toast t;
                t = Toast.makeText(this,"Izgubili ste",Toast.LENGTH_LONG);
                t.show();
                finish();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


    @Override
    public void onBackPressed() {/*
        if(controller.nameOfDrawingToLoad!=null)
        {
            DialogFragment backPressedDialog = new NameDialog();
            backPressedDialog.show(getFragmentManager(), "BackPressedDialogWithName");
        }
        else
        {
            DialogFragment backPressedDialog = new SaveDialog();
            backPressedDialog.show(getFragmentManager(), "BackPressedDialogTadWithoutName");
        }*/
        //TODO ako je WIN situacija pitaj za ime pokreni dialog i Toast sa (POBEDIO SI)
        //TODO ako je lose situacija samo vrati na mainActivity i Toast sa (IZGUBIO SI)
        super.onBackPressed();
    }

    public Controller getController() { return controller; }

    public DbOperationsHelper getDbOperationsHelper() { return dbOperationsHelper; }

    public void setStartTime(long startTime) { this.startTime = startTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public float getScore() { return score; }
    public float getGameTime() { return gameTime + (((float)(endTime - startTime)) / 1000f); }

    @Override
    public void play(int i) {
        Log.d("muzika", "lallala "+i);
        services=new Intent(this, MyService.class);
        services.putExtra("naziv", i);
        services.setAction(MyService.ACTION_PLAY);
        startService(services);
    }
}
