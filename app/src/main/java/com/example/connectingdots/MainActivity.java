package com.example.connectingdots;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    CanvasActivity canvasActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvasActivity=new CanvasActivity(this,null);
        canvasActivity.setBackgroundColor(Color.WHITE);
        FrameLayout container=findViewById(R.id.main_container);
        container.addView(canvasActivity);
    }
}