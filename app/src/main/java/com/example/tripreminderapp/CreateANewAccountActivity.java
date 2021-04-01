package com.example.tripreminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateANewAccountActivity extends AppCompatActivity {
    EditText mailEditTextForSU, passwordEditTextForSU;
    Button signUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_new_account);
        mailEditTextForSU = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditTextForSU = findViewById(R.id.editTextTextPassword2);
        signUp = findViewById(R.id.signUpButton);
        mAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mailEditTextForSU.getText().toString().trim();
                String password = passwordEditTextForSU.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your E_mail Address", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateANewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(CreateANewAccountActivity.this, "Account Created Successfully",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(CreateANewAccountActivity.this,HomeActivity.class));

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CreateANewAccountActivity.this, "Failed To Create Account! Please Try Again",
                                            Toast.LENGTH_LONG).show();

                                }

                            }
                        });

            }
        });
    }
}