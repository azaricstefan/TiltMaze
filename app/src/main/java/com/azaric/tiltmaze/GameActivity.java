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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.azaric.tiltmaze.Dialog.BPDialog2;
import com.azaric.tiltmaze.Dialog.BackPressedDialog;

public class GameActivity extends Activity
        implements
        SensorEventListener {

    boolean[] played=new boolean[3];
    Intent services;
    Controller controller;
    Model model;


    SensorManager sensorManager;
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //create model and controller
        controller = new Controller();
        model = new Model();
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
            controller.loadDrawing(controller.nameOfDrawingToLoad, getApplicationContext());
            Log.d("LOAD DRAWING", "Name of drawing: " + controller.nameOfDrawingToLoad);
        }


        model.findNearestCircle(100,100); //TODO: JUST DEBUG for moving the ball! DELETE LATER!
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
    public void onBackPressed() {
        if(controller.nameOfDrawingToLoad!=null)
        {
            DialogFragment backPressedDialog = new BPDialog2();
            backPressedDialog.show(getFragmentManager(), "BackPressedDialogWithName");
        }
        else
        {
            DialogFragment backPressedDialog = new BackPressedDialog();
            backPressedDialog.show(getFragmentManager(), "BackPressedDialogTadWithoutName");
        }
        //super.onBackPressed();
    }

    public Controller getController() { return controller; }
}
