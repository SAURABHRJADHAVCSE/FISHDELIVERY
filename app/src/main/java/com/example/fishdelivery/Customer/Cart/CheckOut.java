package com.example.fishdelivery.Customer.Cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishdelivery.Customer.CustomerAddress.GetAddress;
import com.example.fishdelivery.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class CheckOut extends AppCompatActivity {

    private TextInputLayout inputEditText, getRs;

    TextView foodTitle, foodPrice;

    Button proceed;
    ImageView imageView;

    TextInputEditText rsTxt;

    private Button incrementButton;
    private Button decrementButton;
    private double quantity = 0;
    private double incrementValue = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        inputEditText = findViewById(R.id.EnterInGr);
        getRs = findViewById(R.id.resultTextView);
        rsTxt = findViewById(R.id.rsText);

        proceed = findViewById(R.id.Proceed);

        foodTitle = findViewById(R.id.FoodNameMyOrders);
        foodPrice = findViewById(R.id.FoodPriceMyOrders);
        imageView = findViewById(R.id.FoodImageMyOrders);

        incrementButton = findViewById(R.id.incrementButton);
        decrementButton = findViewById(R.id.decrementButton);


        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity += incrementValue;
                updateQuantity();
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity >= incrementValue) {
                    quantity -= incrementValue;
                    updateQuantity();
                }
            }
        });

        // Retrieve the passed data from the Intent
        Intent intent = getIntent();
        String foodTitlePass = intent.getStringExtra("foodTitle");
        String foodPricePass = intent.getStringExtra("foodPrice");
        String imageUrlPass = intent.getStringExtra("imageUrl");

        String storeTempPrice = foodPricePass;

        Picasso.get().load(imageUrlPass).into(imageView);
        foodTitle.setText(foodTitlePass);
        foodPrice.setText(foodPricePass);

        rsTxt.setFocusable(false);
        rsTxt.setClickable(false);

        inputEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateResult();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodTitleValue = foodTitle.getText().toString();
                String resultValue = getRs.getEditText().getText().toString();
                String quantity = inputEditText.getEditText().getText().toString();

                Intent intent = new Intent(CheckOut.this, GetAddress.class);
                intent.putExtra("foodTitle", foodTitleValue);
                intent.putExtra("result", resultValue);
                intent.putExtra("imageUrl", imageUrlPass);
                intent.putExtra("quantity", quantity);
                intent.putExtra("tempPrice", storeTempPrice);
                startActivity(intent);
            }
        });

    }

    private void updateQuantity() {
        inputEditText.getEditText().setText(String.valueOf(quantity));
    }

    private void calculateResult() {
        String inputText = inputEditText.getEditText().getText().toString();

        if (inputText.isEmpty()) {
            getRs.getEditText().setText("Enter In Grams");
            return;
        }

        double inputKg = Double.parseDouble(inputText) / 1000;
        double foodPriceValue = Double.parseDouble(foodPrice.getText().toString());
        double result = inputKg * foodPriceValue;

        getRs.getEditText().setText(String.format("%.2f", result));

    }
}