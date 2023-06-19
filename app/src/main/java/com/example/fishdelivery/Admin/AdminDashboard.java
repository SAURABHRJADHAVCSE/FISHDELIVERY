package com.example.fishdelivery.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fishdelivery.Admin.Orders.TotalOrders;
import com.example.fishdelivery.Customer.CustomerAddress.GetAddress;
import com.example.fishdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class AdminDashboard extends AppCompatActivity {

    LinearLayout addItem, seeOrders;
    Button addTodo, addQuotes;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference tokensRef = database.getReference("tokens");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addItem = findViewById(R.id.AddItems);
        seeOrders = findViewById(R.id.SeeOrders);
        addTodo = findViewById(R.id.Todo);
        addQuotes = findViewById(R.id.AddQuotes);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    if (token != null) {
                        Log.d("FCMToken", "Token: " + token); // Log the FCM token
                    } else {
                        Toast.makeText(AdminDashboard.this, "Failed to get FCM token", Toast.LENGTH_SHORT).show();
                    }

                    token = task.getResult();

                    if (token != null) {
                        tokensRef.child("AdminToken").setValue(token)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("FCMToken", "Token stored in the database");
                                        } else {
                                            Toast.makeText(AdminDashboard.this, "Failed to store FCM token", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(AdminDashboard.this, "Failed to get FCM token", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(AdminDashboard.this, "Failed to get FCM token", Toast.LENGTH_SHORT).show();
                }
            }
        });

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