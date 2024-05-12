package com.example.yallain;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.yallain.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
//TODO : person 1
public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    private EditText dobInput;
    String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize database and authentication service
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        binding.signupDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =binding.signupFullName.getText().toString();
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();
                String sex =((RadioButton)findViewById(binding.sexRadiogroup.getCheckedRadioButtonId())).getText().toString();

                Log.e("flag here","name : "+name +"email : "+email+" password : "+password+" sex : "+sex);
                if(email.equals("") || password.equals("") || confirmPassword.equals("") || name.equals("") || sex.equals("") || selectedDate.equals("")){
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirmPassword)){
                    Toast.makeText(SignupActivity.this, "Passwords are not the same !", Toast.LENGTH_SHORT).show();
                }else if (!firebaseUtils.isValidEmail(email)){
                    Toast.makeText(SignupActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                }else if (!firebaseUtils.isValidPassword(password)){
                    Toast.makeText(SignupActivity.this, "Password Must be between 8 and 16 characters", Toast.LENGTH_SHORT).show();
                }else{
                    //we have valid data -> proceed to Sign Up
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, saving information to database
                                        UserModel user =new UserModel(name,email,sex,selectedDate,UserModel.USER);
                                        db.collection(firebaseUtils.USERS_COLLECTION).add(firebaseUtils.convertUserModelToMap(user))
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Data saved to Database -> proceed to home
                                                        Preferences.SaveEmail(SignupActivity.this,user.email);
                                                        Preferences.SaveUserType(SignupActivity.this,user.userType);
                                                        Preferences.SaveName(SignupActivity.this,user.fullName);
                                                        startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignupActivity.this, "try With a different Email address", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignupActivity.this, "Authentication failed."+task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
                    binding.signupDob.setText(selectedDate);
                }, selectedYear, selectedMonth, selectedDayOfMonth);

        datePickerDialog.show();
    }

}
