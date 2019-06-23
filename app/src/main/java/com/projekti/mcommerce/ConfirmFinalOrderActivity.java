package com.projekti.mcommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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






    }
}
