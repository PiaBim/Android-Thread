package com.example.a0807;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class MyHomeCCtv extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    Thread threadSView;
    boolean threadRunning = true;
    public MyHomeCCtv(Context context, AttributeSet attrs){
        super(context,attrs);
        getHolder().addCallback(this);
        threadSView = new Thread(this);

    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        threadSView.start();        // 시작
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        threadRunning=false;        // 스탑
        try{
            threadSView.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final int maxImageSize = 1000000;
        byte[] arr = new byte[maxImageSize];
        try {
            URL url = new URL("http://220.233.144.165:8888/mjpg/video.mjpg");
            HttpURLConnection con = (HttpURLConnection) url.openConnection(); // 주소 연결
            InputStream in = con.getInputStream(); // 통신 시작
            while (threadRunning) {
                int i = 0;
                for(;i<1000;i++) {
                    int b = in.read();
                    if (b == 0xff) {
                        int b2 = in.read();
                        if (b2 == 0xd8) break;
                    }
                }
                    if (i > 999) {
                        Log.e("MyHomeCCTV", "Bad");
                        continue;
                    }
                    arr[0] = (byte) 0xff;
                    arr[1] = (byte) 0xd8;
                    i = 2;

                    for(;i<maxImageSize;i++) {
                        int b=in.read();
                        arr[i] = (byte) b;
                        if(b==0xff) {
                            i++;
                            int b2 = in.read();
                            arr[i] = (byte) b2;
                            if (b2 == 0xd9) {
                                break;
                            }
                        }
                    }

                    i++;
                    int nBytes=i;
                    Log.e("MyHomeCCTV", "got an image, "+nBytes+" bytes!");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, nBytes);
                    bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);

                    SurfaceHolder holder = getHolder();
                    Canvas canvas = null;
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(Color.TRANSPARENT);
                        canvas.drawBitmap(bitmap, 0, 0, null);
                        holder.unlockCanvasAndPost(canvas);


                }
            }
        }catch (Exception e){
            Log.e("MyHomeCCtv","Error:"+e.toString());
        }
    }
}

public class MyCCtv extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homecctv);
    }
}
