package com.example.a0807;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class CircularShape extends AppCompatActivity {
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    int i = 0;

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.circularshape);
        mProgress = (ProgressBar) findViewById(R.id.progress_Bar);
        mProgress.setMax(100);
        mProgress.setProgress(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mProgressStatus < 100){
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                    }
                    mProgressStatus = i++;

                    mProgress.post(new Runnable(){
                        public void run(){
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }
}
