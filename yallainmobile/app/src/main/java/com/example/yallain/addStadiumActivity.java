package com.example.yallain;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.yallain.databinding.ActivityAddStadiumBinding;
import com.example.yallain.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class addStadiumActivity extends AppCompatActivity {

    Uri selectedImage;
    ActivityAddStadiumBinding binding;
    FirebaseStorage storage;
    FirebaseFirestore db;
    String selectedType;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    selectedImage=uri;
                    binding.stadiumPhoto.setImageURI(selectedImage);
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStadiumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectedType=getIntent().getStringExtra("type");
        storage=FirebaseStorage.getInstance();
        db=FirebaseFirestore.getInstance();

        //handling Choosing Photo
        binding.choosePhoto.setOnClickListener((clicked)->{
            //choose the photo for stadium
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        //handling submit Stadium
        binding.submitStadium.setOnClickListener((clicked)->{
            if(selectedImage==null){
                Toast.makeText(this, "Upload a picture for the Stadium", Toast.LENGTH_SHORT).show();
            }else if (binding.stadiumName.getText().toString().equals("")
                    || binding.stadiumPrice.getText().toString().equals("")){
                Toast.makeText(this, "all Fields Are Mandatory", Toast.LENGTH_SHORT).show();
            }else{
                //upload image and save stadium Data
                String StadiumName=binding.stadiumName.getText().toString();
                double price=Double.parseDouble(binding.stadiumPrice.getText().toString());
                String photo=ImageUtils.encodeToBase64(ImageUtils.getBitmapFromUri(addStadiumActivity.this,selectedImage,200,100));
                saveData(StadiumName,price,photo);
            }
        });

        //Handling Bottom Navigation Bar
        binding.homeButton.setOnClickListener((e)->{
            startActivity(new Intent(addStadiumActivity.this, MainActivity.class));
        });
        binding.profileButton.setOnClickListener((e)->{
            startActivity(new Intent(addStadiumActivity.this, ProfileActivity.class));
        });
    }
    void saveData(String stadiumName,double stadiumPrice,String photo){
        Map<String,Object>data=new HashMap<>();
        data.put("stadiumName",stadiumName);
        data.put("stadiumPrice",stadiumPrice);
        data.put("photo",photo);
        db.collection(selectedType).add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Data saved to Database -> proceed to home
                        Toast.makeText(getApplicationContext(), "Stadium Adding Success", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(addStadiumActivity.this, StadiumsActivity.class);
                        intent.putExtra("type",selectedType);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addStadiumActivity.this, "something went wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}