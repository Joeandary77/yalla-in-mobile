package com.example.yallain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yallain.databinding.ActivityHistoryBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding binding;

    private ArrayAdapter<String> historyAdapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        ArrayList<String> rentalHistory = new ArrayList<>();
        historyAdapter = new ArrayAdapter<>(this, R.layout.custom_item, rentalHistory);
        binding.listViewHistory.setAdapter(historyAdapter);

        if(Preferences.GetUserType(this)==UserModel.ADMIN_USER){
            db.collection(firebaseUtils.USERS_COLLECTION)
                    .get()
                    .addOnCompleteListener((task -> {
                        if (task.isSuccessful()) {
                            Log.d("log here",(task.getResult().size())+" size ");
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                db.collection("RentHistory")
                                        .document(snapshot.get("email").toString())
                                        .collection("RentTransactions").get().addOnCompleteListener((task2 -> {
                                            if (task2.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task2.getResult()) {
                                                    // Concatenate rent transaction information into a string
                                                    String data =snapshot.get("fullName").toString()+ " have rented " + document.getString("stadiumName") +
                                                            " from " + document.getString("startTime") + " to " +
                                                            document.getString("endTime");
                                                    rentalHistory.add(data);
                                                    Log.e("nana",rentalHistory.size()+"nana");
                                                }

                                                historyAdapter.notifyDataSetChanged();
                                            } else {
                                                Log.e("log here",task2.getException().getMessage());
                                                Toast.makeText(HistoryActivity.this, task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }));
                            }
                        } else {
                            Toast.makeText(HistoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }));

        }else {
            db.collection("RentHistory")
                    .document(Preferences.GetUserEmail(HistoryActivity.this))
                    .collection("RentTransactions").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Concatenate rent transaction information into a string
                                String data = "You have rented " + document.getString("stadiumName") +
                                        " from " + document.getString("startTime") + " to " +
                                        document.getString("endTime");
                                rentalHistory.add(data);
                            }
                            // Pass the rent transactions to the listener
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            // Notify listener about failure
                            Toast.makeText(this, "Failed to retrieve rent history", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        binding.homeButton.setOnClickListener(v -> {

            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        binding.profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

}
