package com.example.fishdelivery.Admin.Orders;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishdelivery.Customer.Cart.MyCart;
import com.example.fishdelivery.Customer.CustomerAddress.UserOrders;
import com.example.fishdelivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TotalOrdersAdapter extends RecyclerView.Adapter<TotalOrdersAdapter.OrderViewHolder> {

    private List<UserOrders> orderList;

    public TotalOrdersAdapter(List<UserOrders> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_orders_items_loader, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        UserOrders order = orderList.get(position);
        holder.bind(order);

        holder.seeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the position of the clicked item
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    UserOrders clickedOrder = orderList.get(position);

                    // Pass the data to the new activity
                    Intent intent = new Intent(view.getContext(), SeeOrders.class);
                    intent.putExtra("foodName", clickedOrder.getOrderName());
                    intent.putExtra("foodPrice", clickedOrder.getTempPrice());
                    intent.putExtra("quantity", clickedOrder.getOrderQuantity());
                    intent.putExtra("totalPrice", clickedOrder.getOrderPrice());
                    intent.putExtra("fullName", clickedOrder.getName());
                    intent.putExtra("username", clickedOrder.getUsername());
                    intent.putExtra("email", clickedOrder.getEmail());
                    intent.putExtra("phoneNo", clickedOrder.getPhoneNo());
                    intent.putExtra("address", clickedOrder.getAddress());
                    intent.putExtra("image", clickedOrder.getOrderImageUrl());
                    intent.putExtra("latitude", clickedOrder.getLatitude());
                    intent.putExtra("longitude", clickedOrder.getLongitude());



                    view.getContext().startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderDate;
        private TextView orderTime;
        private ImageView orderImageHolder;

        private Button seeDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderTime = itemView.findViewById(R.id.orderTime);
            orderImageHolder = itemView.findViewById(R.id.OrderImageHolder);
            seeDetails = itemView.findViewById(R.id.seeDetails);

        }

        public void bind(UserOrders order) {
            orderDate.setText(order.getOrderDate());
            orderTime.setText(order.getOrderTime());
            Picasso.get().load(order.getOrderImageUrl()).into(orderImageHolder);

        }
    }
}
