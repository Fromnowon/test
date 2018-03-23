package com.test.administrator.test;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public Intent intent;
    private MyReceiver receiver = null;
    ;
    private Vibrator mVibrator;//振动器
    TextView time_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        time_text = findViewById(R.id.time);
        //注册广播接收器
        receiver = new MyReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.test.activity.Time");
        registerReceiver(receiver, filter);

        Button study_btn = findViewById(R.id.study_btn);
        study_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, timeService.class);
                intent.putExtra("type", "study");
                startService(intent);
            }
        });
        Button play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, timeService.class);
                intent.putExtra("type", "play");
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        //程序退出则服务退出
        stopService(intent);
        unregisterReceiver(receiver);
        System.exit(0);
        super.onDestroy();
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int time = bundle.getInt("time");
            //Log.d("tag", time+"");
            //格式化
            int min = (int) Math.floor(time / 60);
            int sec = time % 60;
            String m, s;
            if (min < 10) m = "0" + min;
            else m = min + "";
            if (sec < 10) s = "0" + sec;
            else s = sec + "";
            time_text.setText(m + ":" + s);
            if (time < 300) {
                time_text.setTextColor(Color.RED);
            }
            if (time == 0) {
                mVibrator.vibrate(new long[]{1000, 3000, 1000, 3000}, -1);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("WARNING")
                        .setMessage("Time is OUT!!!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create().show();
            }
        }
    }
}
