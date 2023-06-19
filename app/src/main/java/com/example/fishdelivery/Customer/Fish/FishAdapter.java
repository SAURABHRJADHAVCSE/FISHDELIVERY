package com.example.fishdelivery.Customer.Fish;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishdelivery.Admin.Fish.FishModel;
import com.example.fishdelivery.Customer.Cart.MyCart;
import com.example.fishdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class FishAdapter extends RecyclerView.Adapter<FishAdapter.MyViewHolder> {

    private final ArrayList<FishModel> mFishList;

    private final Context context;

    public FishAdapter(ArrayList<FishModel> mFishList, Context context) {
        this.mFishList = mFishList;
        this.context = context;
    }

    @NonNull
    @Override
    public FishAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fish_item_loader, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FishAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(mFishList.get(position).getFishImageUrl()).into(holder.fishImageHolder);

        FishModel fishModel = mFishList.get(position);
        holder.fishItemName.setText(fishModel.getFishItemTextUrl());
        holder.fishItemPrice.setText(fishModel.getFishPriceTextUrl());

        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = FirebaseDatabase.getInstance().getReference("MyOrders").push().getKey();

                Log.d(TAG, "FishImageUrl: " + key);

                String userId = FirebaseAuth.getInstance().getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Hash Map to store the values
                        HashMap orderDetails = new HashMap();

                        orderDetails.put("image",fishModel.getFishImageUrl());
                        orderDetails.put("name",fishModel.getFishItemTextUrl());
                        orderDetails.put("price",fishModel.getFishPriceTextUrl());

                        Toast.makeText(v.getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();

                   /*     if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("TotalOrders").child(key)
                                    .setValue(orderDetails)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
                        }

                    */


                        FirebaseDatabase.getInstance().getReference("MyOrders").child(userId).child(key)
                                .setValue(orderDetails)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(context, MyCart.class);
                                        context.startActivity(intent);
                                    }
                                });
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
        return mFishList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView fishItemName, fishItemPrice;
        ImageView fishImageHolder, order;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fishImageHolder = itemView.findViewById(R.id.FishImageHolder);
            fishItemName = itemView.findViewById(R.id.FishItemName);
            fishItemPrice = itemView.findViewById(R.id.FishItemPrice);
            order = itemView.findViewById(R.id.orderFish);
        }
    }
}
