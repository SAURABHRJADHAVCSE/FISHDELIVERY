package com.example.fishdelivery.Customer.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.fishdelivery.Admin.Fish.FishAdd;
import com.example.fishdelivery.Admin.Fish.FishModel;
import com.example.fishdelivery.Customer.CustomerDashboard;
import com.example.fishdelivery.Customer.Fish.FishAdapter;
import com.example.fishdelivery.Customer.Fish.SeeFish;
import com.example.fishdelivery.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCart extends AppCompatActivity {

    ImageView backToSDash;
    RecyclerView recyclerView;

    private CartAdapter adapter;
    private ArrayList<FishModel> cartItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItemList = new ArrayList<>();
        adapter = new CartAdapter(cartItemList, this);
        recyclerView.setAdapter(adapter);

        // Retrieve the cart data from Firebase Realtime Database
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("MyOrders").child(userId);
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartItemList.clear();
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String image = cartSnapshot.child("image").getValue(String.class);
                    String name = cartSnapshot.child("name").getValue(String.class);
                    String price = cartSnapshot.child("price").getValue(String.class);

                    FishModel cartItem = new FishModel(image, name, price);
                    cartItemList.add(cartItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during data retrieval
            }
        });
    }
}
