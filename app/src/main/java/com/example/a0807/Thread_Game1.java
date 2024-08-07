package com.example.a0807;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Thread_Game1 extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private RelativeLayout layout;
    private int score = 0;
    private Handler handler = new Handler();
    private Random random = new Random();

    private Runnable changePositionRunnable = new Runnable() {
        @Override
        public void run() {
            changeButtonPosition();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_game1);

        button = findViewById(R.id.button);
        layout = findViewById(R.id.layout);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score++;
                textView.setText("점수:" + score);
                handler.removeCallbacks(changePositionRunnable);  // 이전 콜백 제거
                changeButtonPosition();
            }
        });
        handler.postDelayed(changePositionRunnable, 3000);  // 초기 실행
    }

    private void changeButtonPosition() {
        int screenWidth = layout.getWidth();
        int screenHeight = layout.getHeight();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.leftMargin = random.nextInt(screenWidth - button.getWidth());
        params.topMargin = random.nextInt(screenHeight - button.getHeight());

        button.setLayoutParams(params);

        handler.postDelayed(changePositionRunnable, 3000);  // 3초 후에 다시 실행
    }
}
