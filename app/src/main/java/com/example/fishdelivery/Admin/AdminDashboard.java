package com.example.fishdelivery.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.fishdelivery.Admin.Orders.TotalOrders;
import com.example.fishdelivery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboard extends AppCompatActivity {

    LinearLayout addItem, seeOrders;
    Button addTodo, addQuotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addItem = findViewById(R.id.AddItems);
        seeOrders = findViewById(R.id.SeeOrders);
        addTodo = findViewById(R.id.Todo);
        addQuotes = findViewById(R.id.AddQuotes);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, AddItemDashboard.class));
            }
        });

        seeOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, TotalOrders.class));
            }
        });

    }
}