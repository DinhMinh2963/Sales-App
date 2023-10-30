package com.example.salesapplication.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginScreen extends AppCompatActivity {

    EditText email, password;
    Button signIn_bnt;
    TextView signUp_tv;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        signIn_bnt = findViewById(R.id.btn_signin);
        signUp_tv = findViewById(R.id.tv_signup);

        signIn_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this, RegistrationScreen.class));
            }
        });

    }

    private void loginUser() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginScreen.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginScreen.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

//    public void signin(View view) {
//        String userEmail = email.getText().toString();
//        String userPassword = password.getText().toString();
//
//        auth.signInWithEmailAndPassword(userEmail, userPassword)
//                .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(LoginScreen.this,MainActivity.class));
//                        }else{
//                            Toast.makeText(LoginScreen.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    public void signup(View view) {
//        startActivity(new Intent(LoginScreen.this, RegistrationScreen.class));
//    }
//}