package com.azaric.tiltmaze;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.azaric.tiltmaze.DB.DbOperationsHelper;
import com.azaric.tiltmaze.Dialog.GameNameDialog;

import java.io.File;

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
    private boolean firstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startTime = System.currentTimeMillis();

        //create model and controller
        controller = new Controller(this);
        model = new Polygon(this,getApplicationContext());
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
        //get info about polygon and load it
        Intent intent = getIntent();
        controller.nameOfPolygonToLoad = intent.getStringExtra(MainActivity.NAME_OF_POLYGON);
        if (controller.nameOfPolygonToLoad != null) {
            model.loadPolygonFromFile(controller.nameOfPolygonToLoad,this);
            long num = model.getGameTime();
            setGameTime(num);
        } else
            controller.loadPolygon("", getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sensorManager.unregisterListener(this);
        if(!model.isGameOver()) {
            getController().getModel().setGameTime((System.currentTimeMillis() - startTime) / 1000);
            getController().getModel().savePolygon("TEMP:" + controller.getNameOfPolygonToLoad() + ":tmp", this);
        }
    }

    public void recreate(){
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        startTime = System.currentTimeMillis();

        //create model and controller
        controller = new Controller(this);
        model = new Polygon(this,getApplicationContext());
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

        long num = model.getGameTime();
        setGameTime(num);
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        if(!firstLaunch) {
            File[] files = getApplicationContext().getExternalFilesDir(null).listFiles();
            for(int i = 0; i < files.length; i++){
                if(files[i].getName().equals("TEMP:" + controller.getNameOfPolygonToLoad() + ":tmp")) {
                    //onCreate(null);
                    recreate();
                    getController().getModel().loadPolygonFromFile(files[i].getName(), this);
                    long num = getController().getModel().getGameTime();
                    setGameTime(num);
                    startTime = System.currentTimeMillis();
                    //num *= 1000;
                    //setStartTime(num);
                    getController().getImageView().invalidate();
                    //deleteTmpPolygon(); //DANGER DEBUG
                    Log.d("LOAD/DELETE TMP", "TEMP:" + controller.getNameOfPolygonToLoad() + ":tmp");
                }
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firstLaunch = false;
        sensorManager.unregisterListener(this);
        if(!model.isGameOver()) {
            getController().getModel().setGameTime((System.currentTimeMillis() - startTime) / 1000);
            getController().getModel().savePolygon("TEMP:" + controller.getNameOfPolygonToLoad() + ":tmp", this);

        }
    }

    public void deleteTmpPolygon(){
        File[] files = getApplicationContext().getExternalFilesDir(null).listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i].getName().equals("TEMP:" + controller.getNameOfPolygonToLoad() + ":tmp")) {
                files[i].delete();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //SENSOR METHODS
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        long time = event.timestamp;

        controller.addNewAccelerometerValues(x, y, time);
        getController().getModel().setGameTime((System.currentTimeMillis() - startTime) / 1000);
        boolean gameOver = model.isGameOver();
        if(gameOver && firstTime){
            if(model.isWin()) {
                firstTime = false;
                firstLaunch = false;
                endTime = System.currentTimeMillis();
                score = getGameTime();
                Toast t;
                t = Toast.makeText(this, R.string.game_won,Toast.LENGTH_SHORT);
                t.show();
                //otvori dialog
                DialogFragment nameDialog = new GameNameDialog();
                nameDialog.show(getFragmentManager(), "nameDialog");
            } else {
                firstLaunch = false;
                Toast t;
                t = Toast.makeText(getApplicationContext(), R.string.game_lost,Toast.LENGTH_SHORT);
                t.show();
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        deleteTmpPolygon();
                        Log.d("PUCA", "OVDE");
                        return null;
                    }
                }.execute();
                deleteTmpPolygon();
//                if(controller.nameOfPolygonToLoad.contains("TEMP")) {
//                    File[] files = getApplicationContext().getExternalFilesDir(null).listFiles();
//                    for(int i = 0; i < files.length; i++){
//                        if(files[i].getName().equals(controller.nameOfPolygonToLoad))
//                            files[i].delete();
//                    }
//                }
                finish();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


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

    public double getCurrentGameTime() {
        return gameTime + (((float)(System.currentTimeMillis() - startTime)) / 1000f);
    }

    public void setGameTime(float gameTime) {
        this.gameTime = gameTime;
    }
}
