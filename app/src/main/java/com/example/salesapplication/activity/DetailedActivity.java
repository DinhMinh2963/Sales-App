package com.example.salesapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.salesapplication.R;
import com.example.salesapplication.model.NewProductModel;
import com.example.salesapplication.model.PopularProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView name, description, price, quantity;
    Button addToCart, buyNow;
    ImageView addItem, removeItem;

    int totalQuantity = 1;
    int totalPrice = 0;

    // New Products
    NewProductModel newProductModel = null;

    // Popular Products
    PopularProductModel popularProductModel = null;

    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if(obj instanceof NewProductModel){
            newProductModel = (NewProductModel) obj;
        } else if(obj instanceof PopularProductModel){
            popularProductModel = (PopularProductModel)  obj;
        }

        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        quantity = findViewById(R.id.quantity);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        //New Product
        if(newProductModel != null){
            Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detailedImg);
            name.setText(newProductModel.getName());
            description.setText(newProductModel.getDescription());
            price.setText(newProductModel.getPrice());

            totalPrice = Integer.parseInt(newProductModel.getPrice()) * totalQuantity;
        }

        //Popular Product
        if(popularProductModel != null){
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detailedImg);
            name.setText(popularProductModel.getName());
            description.setText(popularProductModel.getDescription());
            price.setText(popularProductModel.getPrice());

            totalPrice = Integer.parseInt(popularProductModel.getPrice()) * totalQuantity;
        }

        //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, AddressActivity.class);

                if (newProductModel != null){
                    intent.putExtra("item", newProductModel);
                }
                if (popularProductModel != null){
                    intent.putExtra("item", popularProductModel);
                }
                startActivity(intent);
            }
        });

        //Add To Cart
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductModel != null){
                        totalPrice = Integer.parseInt(newProductModel.getPrice()) * totalQuantity;
                    }
                    if (popularProductModel != null){
                        totalPrice = Integer.parseInt(popularProductModel.getPrice()) * totalQuantity;
                    }
                }
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
    }

    private void addToCart() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH: mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}