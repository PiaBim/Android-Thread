package com.example.a0807;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThreadBasic1 extends AppCompatActivity {
    Thread w;
    boolean running = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threadbasic1);
    }
    @Override
    protected void onStart() {
        super.onStart();
        w= new Thread(new Runnable() {
            public void run(){
                int i=0;
                for(i=0; i<20 && running;i++) {
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    Log.v ("THREAD","time="+i);
                }
            }
        });
        running=true;
        w.start();
    }
    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }
}
