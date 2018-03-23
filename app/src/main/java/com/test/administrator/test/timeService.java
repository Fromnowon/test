package com.test.administrator.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * Created by Administrator on 2018/3/23.
 */

public class timeService extends Service {
    static boolean Run = true;
    static Thread MyThread;
    static int time_study = 10;
    static int time_play = 2700;
    static String action;

    @Override
    public IBinder onBind(Intent intent) {
        //服务在成功绑定的时候会调用onBind方法，返回一个IBinder对象
        return new MyBinder();
    }

    private class MyBinder extends Binder {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Run = true;
        action = intent.getStringExtra("type");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {

        MyThread = new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                while (Run) {
                    switch (action) {
                        case "study":
                            time_study--;
                            intent.putExtra("time", time_study);
                            intent.setAction("com.test.activity.Time");
                            sendBroadcast(intent);
                            if (time_study == 0) onDestroy();
                            break;
                        case "play":
                            time_play--;
                            intent.putExtra("time", time_play);
                            intent.setAction("com.test.activity.Time");
                            sendBroadcast(intent);
                            if (time_play == 0) onDestroy();
                            break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        MyThread.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Run = false;
        time_play = 2700;
        time_study = 3600;
        try {
            MyThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyThread = null;
        super.onDestroy();
    }
}
