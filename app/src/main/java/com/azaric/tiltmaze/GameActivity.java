package com.azaric.tiltmaze;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class GameActivity extends Activity
        implements
        SensorEventListener {

    Controller controller;
    Polygon model;


    SensorManager sensorManager;
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //create model and controller
        controller = new Controller();
        model = new Polygon();
        controller.setModel(model);

        //create imageView and connect it with model and controller
        MyImageView imageView = (MyImageView) findViewById(R.id.my_image_view);
        imageView.setController(this,controller);
        controller.setImageView(imageView);
        imageView.setModel(model);

        //SENSOR CODE
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        //GET intent - which polygon to load
        //TODO: get info about polygon and load it
        Intent intent = getIntent();
        controller.nameOfDrawingToLoad = intent.getStringExtra(MainActivity.NAME_OF_DRAWING);
        if (controller.nameOfDrawingToLoad != null) {
            controller. loadPolygon(controller.nameOfDrawingToLoad, getApplicationContext());
        }



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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onBackPressed() {/*
        if(controller.nameOfDrawingToLoad!=null)
        {
            DialogFragment backPressedDialog = new BPDialog2();
            backPressedDialog.show(getFragmentManager(), "BackPressedDialogWithName");
        }
        else
        {
            DialogFragment backPressedDialog = new BackPressedDialog();
            backPressedDialog.show(getFragmentManager(), "BackPressedDialogTadWithoutName");
        }*/
        //TODO nznm sta treba za backPress
        //super.onBackPressed();
    }

    public Controller getController() { return controller; }
}
