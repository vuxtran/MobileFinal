package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
Button btnGetStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title bar and set the activity to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the content view to your layout
        setContentView(R.layout.activity_main);

        // Initialize the views and event handlers
        addViews();
        addEvents();

        setupImageSlider();
    }

    private void addViews() {
        // Initialize your Button
        btnGetStart = findViewById(R.id.btnGetStart);
    }

    private void addEvents() {
        // Set the onClick listener for the button
        btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the screen on button click
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupImageSlider() {
        // Initialize the ImageSlider and add images
        ImageSlider imageSlider = findViewById(R.id.img_AutoBanner);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.mipmap.img_home_page_3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.mipmap.img_nuochoa, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.mipmap.img_home_page_2, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }
}