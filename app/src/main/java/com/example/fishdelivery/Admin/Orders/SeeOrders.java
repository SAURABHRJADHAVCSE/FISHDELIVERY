package com.example.fishdelivery.Admin.Orders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishdelivery.R;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class SeeOrders extends AppCompatActivity {

    private TextInputLayout Name, Username, Email, PhoneNo, editTextAddress;

    ImageView imageView;

    TextView getFoodName, getFoodPrice, getQuantity, getTotalPrice;

    Button track;

    String getLatitude, getLongitude, getAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_orders);

        Name = findViewById(R.id.ProfileName);
        Username = findViewById(R.id.ProfileUsername);
        Email = findViewById(R.id.ProfileEmail);
        PhoneNo = findViewById(R.id.ProfilePhoneN0);
        editTextAddress = findViewById(R.id.editTextAddress);

        imageView = findViewById(R.id.FoodImageMyOrders);
        track = findViewById(R.id.Track);

        getFoodName = findViewById(R.id.FoodNameMyOrders);
        getFoodPrice = findViewById(R.id.FoodPriceMyOrders);
        getQuantity = findViewById(R.id.FoodOrderQuantity);
        getTotalPrice = findViewById(R.id.FoodTotalPrice);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String foodName = extras.getString("foodName");
            String foodPrice = extras.getString("foodPrice");
            String quantity = extras.getString("quantity");
            String totalPrice = extras.getString("totalPrice");
            String fullName = extras.getString("fullName");
            String username = extras.getString("username");
            String email = extras.getString("email");
            String phoneNo = extras.getString("phoneNo");
            String address = extras.getString("address");
            String image = extras.getString("image");
            String latitude = extras.getString("latitude");
            String longitude = extras.getString("longitude");

            // Set the data to the TextInputLayouts and TextViews
            Name.getEditText().setText(fullName);
            Username.getEditText().setText(username);
            Email.getEditText().setText(email);
            PhoneNo.getEditText().setText(phoneNo);
            editTextAddress.getEditText().setText(address);
            getFoodName.setText(foodName);
            getFoodPrice.setText(foodPrice);
            getQuantity.setText(quantity);
            getTotalPrice.setText(totalPrice);

            getLatitude = latitude;
            getLongitude = longitude;
            getAddress = address;

            Picasso.get().load(image).into(imageView);
        }

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the address information to the TrackLocationActivity
                Intent intent = new Intent(SeeOrders.this, AdminTrackUser.class);
                intent.putExtra("latitude", getLatitude); // Set the latitude of the address
                intent.putExtra("longitude", getLongitude); // Set the longitude of the address
                intent.putExtra("address", getAddress); // Set the longitude of the address
                startActivity(intent);
            }
        });
    }
}