package com.example.a0807;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class Image_DownLoad extends AppCompatActivity {

    public String URL ="";
    EditText editText;
    ImageView imageView;
    Button button;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_download);

        editText=(EditText) findViewById(R.id.editText);
        imageView=(ImageView) findViewById(R.id.imageView);
        button=(Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL=editText.getText().toString();
                downloadImage(URL);
            }
        });
    }

    private void downloadImage (final String imageUrl){
        mProgressDialog = new ProgressDialog(Image_DownLoad.this);
        mProgressDialog.setTitle("이미지 다운로드 예제");
        mProgressDialog.setMessage("이미지 다운로드 중입니다.");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStream input = new java.net.URL(imageUrl).openStream();
                    final Bitmap bitmp = BitmapFactory.decodeStream(input);

                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmp);
                            mProgressDialog.dismiss();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
           downloadThread.start();
        }
    }

