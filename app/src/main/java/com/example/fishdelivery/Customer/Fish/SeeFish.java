package com.example.fishdelivery.Customer.Fish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.fishdelivery.Admin.Fish.FishModel;
import com.example.fishdelivery.Customer.CustomerDashboard;
import com.example.fishdelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeeFish extends AppCompatActivity {

    ArrayList<FishModel> fishList;

    FishAdapter fishAdapter;

    ImageView backToSDash;

    // FIREBASE

    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Fish");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_fish);

        backToSDash = findViewById(R.id.BackToSDash);

        backToSDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SeeFish.this, CustomerDashboard.class));
                finish();
            }
        });

        // RECYCLER VIEW

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFish);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fishList = new ArrayList<>();

        fishAdapter = new FishAdapter(fishList, this);
        recyclerView.setAdapter(fishAdapter);


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FishModel fishModel = dataSnapshot.getValue(FishModel.class);
                    fishList.add(fishModel);
                }
                fishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}