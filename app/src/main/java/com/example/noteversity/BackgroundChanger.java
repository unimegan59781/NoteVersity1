package com.example.noteversity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class BackgroundChanger extends AppCompatActivity {

    Button redButton, creamButton;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_changer);

        redButton = findViewById(R.id.redButton);
        creamButton = findViewById(R.id.creamButton);
        layout = findViewById(R.id.linearlayout);

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundResource(R.drawable.blue_background);

            }
        });

        creamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundResource(R.drawable.cream_background);

            }
        });
    }
}