package com.example.yallain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.yallain.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
//TODO : person 1
public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private static final String KEY_USER = "USER";
    FirebaseFirestore db ;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize the firebase auth and database
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        //remember email if user logged in before
        if(auth.getCurrentUser()!=null){
            String email=auth.getCurrentUser().getEmail();
            binding.loginEmail.setText(email);
        }

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if(email.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }else if (!firebaseUtils.isValidEmail(email)){
                    Toast.makeText(LoginActivity.this, "Enter a valid Email Address", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener((e)->{
                        if (e.isSuccessful()){
                            db.collection(firebaseUtils.USERS_COLLECTION).where(Filter.equalTo("email",email))
                                    .get()
                                    .addOnCompleteListener((task)->{
                                        if(task.isSuccessful()){
                                            Map<String,Object> data=task.getResult().getDocuments().get(0).getData();
                                            saveUserData(data,email);
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        }else{
                                            Toast.makeText(LoginActivity.this, "User Data Not Found !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else{
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    void saveUserData(Map<String,Object>data,String email){
        Preferences.SaveEmail(LoginActivity.this, email);
        Preferences.SaveName(LoginActivity.this, ((String) data.get("fullName")));
        Preferences.SaveUserType(LoginActivity.this, (Integer.parseInt(""+ data.get("userType"))));
    }

}