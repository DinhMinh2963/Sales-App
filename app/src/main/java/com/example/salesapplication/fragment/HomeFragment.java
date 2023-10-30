package com.example.salesapplication.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.salesapplication.R;
import com.example.salesapplication.adapter.CategoryAdapter;
import com.example.salesapplication.adapter.NewProductAdapter;
import com.example.salesapplication.adapter.PopularProductAdapter;
import com.example.salesapplication.model.CategoryModel;
import com.example.salesapplication.model.NewProductModel;
import com.example.salesapplication.model.PopularProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    RecyclerView catRecyclerview, newProductRecyclerview, popularProductRecyclerview;
    //Category recyclerview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //New Product Recyclerview
    NewProductAdapter newProductAdapter;
    List<NewProductModel> newProductModelList;

    //Popular Product Recyclerview
    PopularProductAdapter popularProductAdapter;
    List<PopularProductModel> popularProductModelList;


    //FireStore
    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new ProgressDialog(getActivity());
        catRecyclerview = root.findViewById(R.id.rec_category);
        newProductRecyclerview = root.findViewById(R.id.new_product_rec);
        popularProductRecyclerview = root.findViewById(R.id.popular_product_rec);

        db = FirebaseFirestore.getInstance();


        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);

        progressDialog.setTitle("Welcome To My Sales Application ");
        progressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, (CharSequence) "Please wait...", (Message) null);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Category
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(),categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){

                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();

                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }else{
                            Toast.makeText(getActivity(),"" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //New Product
        newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        newProductModelList = new ArrayList<>();
        newProductAdapter = new NewProductAdapter(getContext(),newProductModelList);
        newProductRecyclerview.setAdapter(newProductAdapter);

        db.collection("NewProduct")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){

                                NewProductModel newProductModel = document.toObject(NewProductModel.class);
                                newProductModelList.add(newProductModel);
                                newProductAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(getActivity(),"" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Popular Product
        popularProductRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        popularProductModelList = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getContext(),popularProductModelList);
        popularProductRecyclerview.setAdapter(popularProductAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                PopularProductModel popularProductModel = document.toObject(PopularProductModel.class);
                                popularProductModelList.add(popularProductModel);
                                popularProductAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(getActivity(),"" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }
}