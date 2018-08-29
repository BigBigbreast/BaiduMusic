package com.example.administrator.baidumusic.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.administrator.baidumusic.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class PlaymusicService extends Service {
    private static  MediaPlayer mediaPlayer= new MediaPlayer();
    private Timer timer;
    private TimerTask task;

    public PlaymusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public void play1(){
        Log.e("Music:","Music start");
        try {
            mediaPlayer.reset();
            //Set the play music
            mediaPlayer.setDataSource("/mnt/sdcard/Star.mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
            update();
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public void pause1(){
        Log.e("Music:","Music pause");
        mediaPlayer.pause();
    }

    public void restart1()
    {
        Log.e("Music:","Music restart");
        mediaPlayer.start();
    }
    public void update(){
        //Get the music's time
        final int length=mediaPlayer.getDuration();
        //Create a timer and create a tast
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                int currenttime=mediaPlayer.getCurrentPosition();
                Bundle bundle=new Bundle();
                bundle.putInt("length",length);
                bundle.putInt("currenttime",currenttime);
                Message message=Message.obtain();
                message.setData(bundle);
                MainActivity.mHandler.sendMessage(message);
            }
        };
        //Start the task and execute every one second
        timer.schedule(task,300,1000);

        //When the music has done cancel the timer and task
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                timer.cancel();
                task.cancel();
            }
        });
    }

    public void seekto(int position){
        mediaPlayer.seekTo(position);
    }
    private class  MyBinder extends Binder implements com.example.administrator.baidumusic.Service.IBinder{

        @Override
        public void play() {
            play1();
        }

        @Override
        public void pause() {
            pause1();
        }

        @Override
        public void restart() {
            restart1();
        }

        @Override
        public void callseekto(int position) {
            seekto(position);
        }
     }

}
