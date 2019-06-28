package com.projekti.mcommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projekti.mcommerce.Model.Products;
import com.projekti.mcommerce.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView productImage;
    private Button  addToCartButton;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");
        addToCartButton=(Button)findViewById(R.id.pd_add_to_cart_button);
        productImage=(ImageView) findViewById(R.id.product_image_details);
        numberButton=(ElegantNumberButton) findViewById(R.id.number_btn);

        productPrice=(TextView) findViewById(R.id.product_price_details);

        productDescription=(TextView) findViewById(R.id.product_description_details);

        productName=(TextView) findViewById(R.id.product_name_details);
        //******************
        getProductDetails(productID);
        //ja vendos clickun butonit
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();

            }
        });

    }

    private void addingToCartList()
    {
//tregon kohen kur perdoruesi e vendos produktin ne cart
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        //merr daten aktuale
        SimpleDateFormat currentDate =new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        //merr kohen aktuale
        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
// per me i rujt ne databaze e krijojme nje reference
        final    DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        // Permes Hash i ruajm data-te
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        //cartMap.put("discount","");
        //refrenca per karte(cart)
        //kur useri vendos produkte ne cart
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    //nese user suksesshem vendos ne cart produktet ateher kto mundet mi pa admini
                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                            .child("Products").child(productID)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailsActivity.this,"Added to Cart List",Toast.LENGTH_SHORT ).show();
                                Intent intent=new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                    });
                }

            }
        });

    }
    private void getProductDetails(String productID) {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
