package com.projekti.mcommerce.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.projekti.mcommerce.Interface.ItemClickListner;
import com.projekti.mcommerce.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListner itemClickListner;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);


    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);

    }
     private void  setItemClickListner(ItemClickListner itemClickListner){
        this.itemClickListner= itemClickListner;
     }
}
