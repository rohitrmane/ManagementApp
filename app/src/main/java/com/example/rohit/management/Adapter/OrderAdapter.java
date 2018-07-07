package com.example.rohit.management.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rohit.management.DetailsActivity;
import com.example.rohit.management.NewOrderActivity;
import com.example.rohit.management.OrderModel;
import com.example.rohit.management.R;
import com.example.rohit.management.SaveActivity;
import com.example.rohit.management.ViewAllActivity;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrderModel> orderList;
    public static  String imageString = "";

    public OrderAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_entry_layout, parent, false);
        return new OrderViewHolder(v);

    }

    @Override
    public void onBindViewHolder(OrderAdapter.OrderViewHolder holder, int position) {

        final OrderModel model = orderList.get(position);
        holder.orderNo.setText(model.getOrderNo());
        holder.customerName.setText(model.getCust_name());
        holder.totalAmount.setText(model.getTotalAmount());
        holder.orderDate.setText(model.getOrderDate());
        holder.status.setText(model.getStatus());
        holder.contactNo.setText(model.getContactNo());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SaveActivity.class);
                intent.putExtra("billNo",model.getOrderNo());
                intent.putExtra("customerName",model.getCust_name());
                intent.putExtra("totalAmount",model.getTotalAmount());
                intent.putExtra("orderDate",model.getOrderDate());
                intent.putExtra("advanceAmt",model.getAdvanceAmount());
                intent.putExtra("pendingAmt",model.getPendingAmount());
                intent.putExtra("status",model.getStatus());
                intent.putExtra("contactNo",model.getContactNo());
                intent.putExtra("type",model.getType());
                imageString = model.getImageString();
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView orderNo;
        TextView customerName;
        TextView totalAmount;
        TextView orderDate;
        TextView status;
        TextView contactNo;
        LinearLayout layout;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderNo = (TextView)itemView.findViewById(R.id.order_no);
            customerName = (TextView)itemView.findViewById(R.id.customer_name);
            totalAmount = (TextView)itemView.findViewById(R.id.amount);
            orderDate = (TextView)itemView.findViewById(R.id.order_date);
            status = (TextView)itemView.findViewById(R.id.status);
            contactNo = (TextView)itemView.findViewById(R.id.contactNo);
            layout = (LinearLayout)itemView.findViewById(R.id.layout);


        }
    }
}
