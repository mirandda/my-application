package com.projekti.mcommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projekti.mcommerce.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText , addressEditText, cityEditText;
      private  Button confirmOrderBtn;
     // private  String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        //totalAmount=getIntent().getStringExtra("Total Price");

        confirmOrderBtn=(Button)findViewById(R.id.confirm_final_order_btn);
        nameEditText=(EditText)findViewById(R.id.shippment_name);
        phoneEditText=(EditText)findViewById(R.id.shippment_phone);
        addressEditText=(EditText)findViewById(R.id.shippment_address);
        cityEditText=(EditText)findViewById(R.id.shippment_city);
//me konfirmu kur te klikojme butonin confirmOrderBtn
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });





    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this,"Please provide your full name.", Toast.LENGTH_SHORT).show();
        }
      else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this,"Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
      else  if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this,"Please provide your city.", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this,"Please provide your address.", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        String saveCurrentDate, saveCurrentTime;
        //merr daten aktuale
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate =new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        //merr kohen aktuale
        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        //KRIJOJME NE DB NI CHILD "ORDERS" KU RUHEN TE DHENAT
        final DatabaseReference  ordersRef=  FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String, Object> ordersMap= new HashMap<>();
        //ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {

                //Nese tasku osht i suksesshem atehere e zbrasim cart, dhe ne db fshihet user view e cila gjendet ne cart list
                FirebaseDatabase.getInstance().getReference().child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ConfirmFinalOrderActivity.this, "your final order has been placed successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                            //flags tregojn qe useri nuk mundet mu kthyte qiky aktivitet prap
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }


                    }
                });





            }
            }
        });


    }
}
