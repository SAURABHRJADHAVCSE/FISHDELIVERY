package com.example.fishdelivery.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fishdelivery.Admin.Chicken.ChickenAdd;
import com.example.fishdelivery.Admin.Fish.FishAdd;
import com.example.fishdelivery.Admin.Mutton.MuttonAdd;
import com.example.fishdelivery.R;

public class AddItemDashboard extends AppCompatActivity {

    LinearLayout addFish, addChicken, addMutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_dashboard);

        addFish = findViewById(R.id.AddFish);
        addChicken = findViewById(R.id.AddChicken);
        addMutton = findViewById(R.id.AddMutton);

        addFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddItemDashboard.this, FishAdd.class));
            }
        });

        addChicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddItemDashboard.this, ChickenAdd.class));
            }
        });

        addMutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddItemDashboard.this, MuttonAdd.class));
            }
        });
    }
}