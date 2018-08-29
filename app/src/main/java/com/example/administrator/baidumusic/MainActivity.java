package com.example.administrator.baidumusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.example.administrator.baidumusic.Service.PlaymusicService;

public class MainActivity extends AppCompatActivity {
    /*
    * Open services in a mixed manner
    *   If you use the mixed way to start a service
    *   you need order these steps
    *   1. StartService
    *   2. BindService
    *   3. UnbindService
    *   4. Stopservice
    *
    *
    * */

    private com.example.administrator.baidumusic.Service.IBinder mIBinder;
    private Myconn conn;
    private static SeekBar seekBar;
    public static  Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //Get the data that translated from service
           Bundle bundle=msg.getData();
           int length=bundle.getInt("length");
           int currenttime=bundle.getInt("currenttime");
           seekBar.setMax(length);
           seekBar.setProgress(currenttime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. Start Service by startService
        Intent intent=new Intent(this, PlaymusicService.class);
        startService(intent);

        //2. Start Service by bindService
        conn=new Myconn();
        bindService(intent,conn,BIND_AUTO_CREATE);

        seekBar=findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int setposition=seekBar.getProgress();
                mIBinder.callseekto(setposition);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    public void play(View view) {
        mIBinder.play();
    }
    public void pause(View view){

        mIBinder.pause();
    }
    public void restart(View view) {

        mIBinder.restart();
    }


    private class Myconn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIBinder= (com.example.administrator.baidumusic.Service.IBinder) iBinder;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }



}
