package com.example.yallain;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;

    private ImageView homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.usernameTextView);
        homeButton = findViewById(R.id.homeButton);
        usernameTextView.setText(Preferences.GetUserName(this));
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        homeButton.setOnClickListener(v -> {

            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        Button rateUsButton = findViewById(R.id.rateUsButton);
        rateUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRateUsPopup(v);
            }
        });

        Button viewHistoryButton = findViewById(R.id.viewHistoryButton);
        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });



    }


    private void showRateUsPopup(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_rate_us, null);


        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        ImageView closePopup = popupView.findViewById(R.id.closePopup);


        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        Button submitButton = popupView.findViewById(R.id.submitButton);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast message thanking the user
                Toast.makeText(getApplicationContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            }
        });


        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

}
