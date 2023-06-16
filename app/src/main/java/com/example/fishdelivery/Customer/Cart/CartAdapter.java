package com.example.fishdelivery.Customer.Cart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishdelivery.Admin.Fish.FishModel;
import com.example.fishdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private final ArrayList<FishModel> cartItemList;
    private final Context context;

    public CartAdapter(ArrayList<FishModel> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_cart, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        FishModel cartItem = cartItemList.get(position);

        holder.foodTitle.setText(cartItem.getFishItemTextUrl());
        holder.foodPrice.setText(cartItem.getFishPriceTextUrl());

        Picasso.get().load(cartItem.getFishImageUrl()).into(holder.imageView);

        holder.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FishModel selectedCartItem = cartItemList.get(position);

                // Create an Intent to start the CheckOut activity
                Intent intent = new Intent(context, CheckOut.class);

                // Pass the data as extras in the Intent
                intent.putExtra("foodTitle", selectedCartItem.getFishItemTextUrl());
                intent.putExtra("foodPrice", selectedCartItem.getFishPriceTextUrl());
                intent.putExtra("imageUrl", selectedCartItem.getFishImageUrl());

                context.startActivity(intent);



            }
        });

        holder.cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = FirebaseAuth.getInstance().getUid();


                DatabaseReference myOrdersRef = FirebaseDatabase.getInstance().getReference("MyOrders").child(userId);
                myOrdersRef.orderByChild("name").equalTo(cartItem.getFishItemTextUrl()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            String key = ds.getKey();

                            DatabaseReference totalOrdersRef = FirebaseDatabase.getInstance().getReference("TotalOrders").child(key);
                            myOrdersRef.child(key).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                totalOrdersRef.removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(context, "Order Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView foodTitle, foodPrice;
        Button cancelOrderBtn, checkOut;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodTitle = itemView.findViewById(R.id.FoodNameMyOrders);
            foodPrice = itemView.findViewById(R.id.FoodPriceMyOrders);
            cancelOrderBtn = itemView.findViewById(R.id.CancelOrderNowBtnMyOrders);
            imageView = itemView.findViewById(R.id.FoodImageMyOrders);
            checkOut = itemView.findViewById(R.id.CheckOut);
        }
    }
}