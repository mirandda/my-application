package com.projekti.mcommerce;
//i shfaqim ato qe jon ne cartItemsLayout
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projekti.mcommerce.Model.Cart;
import com.projekti.mcommerce.Prevalent.Prevalent;
import com.projekti.mcommerce.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;
   // private  int overTotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=(RecyclerView)findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcessBtn = (Button)findViewById(R.id.next_process_btn);
    //    txtTotalAmount=(TextView)findViewById(R.id.total_price);
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //txtTotalAmonut.setText("Total price ="(String.valueOf(overTotalPrice)9
                Intent intent=new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
               // intent.putExtra("Total price", String.valueOf(overTotalPrice));
                startActivity(intent);
                 finish();
            }
        });



    }
    //mi shfaq items qe jon ne cart list
    //Referohet tek databaza ku jn te rujtne produktet qe i ka vendos ne cart useri
    @Override
    protected  void  onStart()
    {

        super.onStart();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class)
                        .build();

    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
            =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
            holder.txtProductQuantity.setText("Quantity = "+model.getQuantity());
            holder.txtProductPrice.setText("Price = "+model.getPrice()+"$");
            holder.txtProductName.setText(model.getPname());


        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
            CartViewHolder  holder=new CartViewHolder(view);
            return holder;
        }
    };
    recyclerView.setAdapter(adapter);
    adapter.startListening();
}
}
