package com.gyw.arcprogressdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.arc_progress)
    ArcProgress mArcProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        mHandler.postDelayed(progressTask, 1000);
    }



    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mArcProgress.setProgress(msg.arg1);
            mHandler.postDelayed(progressTask, 1000);
        }
    };

    private int progress = 0;

    private Runnable progressTask = new Runnable() {

        @Override
        public void run() {
            progress += 100;
            mHandler.sendMessage(mHandler.obtainMessage(0, progress, 0));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(progressTask);
        mHandler = null;
    }
}
