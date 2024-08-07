package com.example.a0807;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

class Ball {
    int x,y;
    int radius=50;
    int dx,dy;
    int w,h;
    Paint paint = new Paint();
    Random random = new Random();
    public Ball(int x,int y,int w,int h){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        dx=random.nextInt(10)-5;
        dy=random.nextInt(10)-5;
        paint.setColor(Color.WHITE);
    }
    public void draw(Canvas canvas){
     canvas.drawCircle(x,y,radius,paint);
    }
    public void update(){
        x+=dx;
        y+=dy;
        if(x+radius>w||x-radius<0){
            dx=-dx;
        }
        if(y+radius>h||y-radius<0){
            dy=-dy;
        }
    }
}

class MyView extends View {
    ArrayList<Ball> points = new ArrayList<>();

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.YELLOW);
        /*new Thread(() -> {
            while (true) {
                try {
                    for (Ball b : points) {
                        b.update();
                        invalidate();
                    }
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }).start();*/
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(Ball b:points) {
                    b.update();
                }
                    invalidate();
                    handler.postDelayed(this,10);
                }
            },30);
        }



    @Override
    protected void onDraw(@Nullable Canvas canvas){
        super.onDraw(canvas);
        for (Ball b:points){
            b.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            points.add(new Ball(x,y,getWidth(),getHeight()));
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }
}

class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    ArrayList<Ball> points = new ArrayList<>();
    Thread ThreadSView;
    boolean TreadRunning =true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            synchronized (points) {
                points.add(new Ball(x, y, getWidth(), getHeight()));
            }
                return true;

        }
        return super.onTouchEvent(event);
    }

    public MySurfaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        ThreadSView = new Thread(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ThreadSView.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surface의 크기 또는 형식이 변경되었을 때 수행할 작업
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        TreadRunning = false;
        while (true) {
            try {
                ThreadSView.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        SurfaceHolder holder = getHolder();
        while (TreadRunning) {
            Canvas canvas = null;
            canvas = holder.lockCanvas();
            if(canvas!=null){
                canvas.drawColor(Color.CYAN);
                synchronized (points) {
                    for (Ball b : points) {
                        b.update();
                        b.draw(canvas);
                    }
                }
                holder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}