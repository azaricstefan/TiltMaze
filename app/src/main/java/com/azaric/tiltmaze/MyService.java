package com.azaric.tiltmaze;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{
    public static final String ACTION_PLAY = "service.ACTION_PLAY";
    public static final String ACTION_PAUSE = "service.ACTION_PAUSE";


    MediaPlayer[] mediaPlayer=new MediaPlayer[3];
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer[0]= MediaPlayer.create(this, R.raw.sudar);
        mediaPlayer[1]= MediaPlayer.create(this, R.raw.pobeda);
        mediaPlayer[2]= MediaPlayer.create(this, R.raw.izgubio);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            int i = intent.getExtras().getInt("naziv");
            if (intent.getAction().equals(ACTION_PLAY))
                mediaPlayer[i].start();
            else if (intent.getAction().equals(ACTION_PAUSE))
                mediaPlayer[i].pause();
        }
        return START_STICKY;
    }

    public void onDestroy() {
        for(int i=0; i<3; i++) {
            if (mediaPlayer[i].isPlaying()) {
                mediaPlayer[i].stop();
            }
            mediaPlayer[i].release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }
}
