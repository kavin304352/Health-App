package com.example.hacktj2k19;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private TextView help;
    private int height;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageButton = (ImageButton)findViewById(R.id.image_button);
        help = (TextView)findViewById(R.id.title_text_view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        mImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    final float evX = event.getX();
                    final float evY = (int) event.getY();
                    double newWidth = evX  / width;
                    double newHeight = 1 - evY / height;
                    String bodyPart = "none";
                    if(newWidth > 0.34 && newWidth < 0.64 && newHeight < 0.58 && ((4.598 * newWidth - 1.689) < newHeight || (-4.819 * newWidth + 2.958) < newHeight)) {
                        bodyPart = "legs";
                        Intent i = new Intent(MainActivity.this, LegActivity.class);
                        startActivity(i);
                    } else if(newWidth > 0.43 && newWidth < 0.56 && newHeight > 0.88) {
                        bodyPart = "head";
                        Intent i = new Intent(MainActivity.this, HeadActivity.class);
                        startActivity(i);
                    } else if(newHeight > 0.54 && newHeight < 0.88 && (((2.389 * newWidth - 0.172) < newHeight && (1.778 * newWidth + 0.244) > newHeight) || ((-2.091 * newWidth + 2.045) < newHeight && (-1.6 * newWidth + 1.896) > newHeight))) {
                        bodyPart = "arms";
                        Intent i = new Intent(MainActivity.this, ArmActivity.class);
                        startActivity(i);
                    } else if(newWidth > 0.36 && newWidth < 0.63 && (5.333 * newWidth - 1.32) > newHeight && (-8.5000 * newWidth + 3.66) < newHeight && newHeight < 0.88) {
                        bodyPart = "torso";
                        Intent i = new Intent(MainActivity.this, TorsoActivity.class);
                        startActivity(i);
                    }

                    if(bodyPart.equals("none")) {
                        Toast.makeText(MainActivity.this,
                                "Please by more clear",
                                Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
        });
    }
}
