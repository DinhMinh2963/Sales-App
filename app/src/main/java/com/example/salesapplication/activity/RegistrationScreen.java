package com.example.salesapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salesapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationScreen extends AppCompatActivity {

    EditText name, email, password, phone;
    Button signUp_btn;
    TextView signIn_tv;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        //getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.edt_name);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        phone = findViewById(R.id.edt_phone);
        signUp_btn = findViewById(R.id.btn_singup);
        signIn_tv = findViewById(R.id.tv_signin);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(RegistrationScreen.this,MainActivity.class));
                createUser();
            }
        });

        signIn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationScreen.this, LoginScreen.class));
            }
        });

    }

    private void createUser() {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userPhone = phone.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPhone)) {
            Toast.makeText(this, "Enter Phone!", Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(RegistrationScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationScreen.this, "Successfully Register ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationScreen.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegistrationScreen.this, "Register Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}