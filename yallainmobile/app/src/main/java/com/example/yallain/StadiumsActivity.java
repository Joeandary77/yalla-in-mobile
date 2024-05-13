package com.example.yallain;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.yallain.databinding.ActivityStadiumsBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StadiumsActivity extends AppCompatActivity {
    ActivityStadiumsBinding binding;
    FirebaseFirestore db;
    String selectedType="Football";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStadiumsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getIntent().getStringExtra("type")!=null){
            selectedType=getIntent().getStringExtra("type");
        }
        //for admin
        if(Preferences.GetUserType(StadiumsActivity.this)==UserModel.ADMIN_USER){
            binding.addStadium.setVisibility(View.VISIBLE);
            binding.addStadium.setOnClickListener((clicked)->{
                //navigate to add stadium Activity
                Intent intent= new Intent(StadiumsActivity.this,addStadiumActivity.class);
                intent.putExtra("type",selectedType);
                startActivity(intent);
            });
        }


        binding.sportTitle.setText(selectedType);
        db=FirebaseFirestore.getInstance();
        db.collection(selectedType).get().addOnCompleteListener((task)->{
            if(task.isSuccessful()){
                List<StadiumModel> stadiumList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("stadiumName");
                    String photoUrl = document.getString("photo");
                    double price = document.getDouble("stadiumPrice");
                    StadiumModel stadium = new StadiumModel(name, photoUrl, price);
                    stadiumList.add(stadium);
                }
                // Create and set adapter
                stadiumAdapter adapter = new stadiumAdapter(StadiumsActivity.this, stadiumList);
                GridLayoutManager manager=new GridLayoutManager(StadiumsActivity.this,1);
                binding.recyclerView.setLayoutManager(manager);
                binding.recyclerView.setAdapter(adapter);
            } else{
                Toast.makeText(this, "fetching Data Error", Toast.LENGTH_SHORT).show();
            }
        });
        //Handling Bottom Navigation Bar
        binding.homeButton.setOnClickListener((e)->{
            startActivity(new Intent(StadiumsActivity.this, MainActivity.class));
        });
        binding.profileButton.setOnClickListener((e)->{
            startActivity(new Intent(StadiumsActivity.this, ProfileActivity.class));
        });
    }


}

