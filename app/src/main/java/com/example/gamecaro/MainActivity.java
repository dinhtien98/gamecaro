package com.example.gamecaro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewpvp,imageViewpvc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewpvp = findViewById(R.id.imageViewpvp);
        imageViewpvc = findViewById(R.id.imageViewpvc);
        imageViewpvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, pvp.class);
                startActivity(intent);
            }
        });
        imageViewpvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, pvc.class);
                startActivity(intent);
            }
        });
    }
}