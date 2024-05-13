package com.example.yallain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.yallain.databinding.ActivityMainBinding;

import java.util.Arrays;

import java.util.List;

//TODO : 3
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.football.setOnClickListener((clicked)->{
            launchSportActivity("Football");
        });
        binding.tennis.setOnClickListener((clicked)->{
            launchSportActivity("Tennis");
        });
        binding.paddel.setOnClickListener((clicked)->{
            launchSportActivity("Paddle");
        });
        binding.bascketball.setOnClickListener((clicked)->{
            launchSportActivity("Basketball");
        });
        binding.profileButton.setOnClickListener((e)->{
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
    }

    private void launchSportActivity(String sport) {
        Intent intent = new Intent(MainActivity.this,StadiumsActivity.class);
        intent.putExtra("type",sport);
        startActivity(intent);
    }


}