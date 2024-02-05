package com.example.salesapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.salesapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    EditText name, address, city, postalCode, phoneNumber;
    //Toolbar toolbar;
    Button addAddressBtn;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);



        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address_lane);
        city = findViewById(R.id.ad_city);
        postalCode = findViewById(R.id.ad_postal_code);
        phoneNumber = findViewById(R.id.ad_phone_number);
        addAddressBtn = findViewById(R.id.ad_address_btn);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userPostalCode = postalCode.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();

                String final_address = "";

                if (!userName.isEmpty()){
                    final_address += userName + ", ";
                }
                if (!userCity.isEmpty()){
                    final_address += userCity + ", ";
                }
                if (!userAddress.isEmpty()){
                    final_address += userAddress + ", ";
                }
                if (!userPostalCode.isEmpty()){
                    final_address += userPostalCode + ", ";
                }
                if (!userPhoneNumber.isEmpty()){
                    final_address += userPhoneNumber + ", ";
                }
                if (!userName.isEmpty() && !userCity.isEmpty() && !userAddress.isEmpty() && !userPostalCode.isEmpty() && !userPhoneNumber.isEmpty()){
                    Map<String,String> map = new HashMap<>();
                    map.put("userAddress", final_address);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AddAddressActivity.this,"Address Added", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddAddressActivity.this, DetailedActivity.class));
                                        finish();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(AddAddressActivity.this, "Kindly Fill ALl The Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}