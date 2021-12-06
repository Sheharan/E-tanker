package com.example.etanker_supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class tankerRegistration extends AppCompatActivity {

    EditText tankerNo;
    EditText price;
    EditText tankerDriverName;
    EditText tankerDriverPhoneNumber;
    EditText tankerWaterSource;
    EditText tankerCapacity;
    Button btnRegister;
    EditText stickerColor;
    String uid;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanker_registration);

        firebaseAuth=FirebaseAuth.getInstance();

        tankerNo=(EditText)findViewById(R.id.tankerNo);
        price=(EditText)findViewById(R.id.tankerPrice);
        tankerDriverName=(EditText)findViewById(R.id.tankerDriverName);
        tankerDriverPhoneNumber=(EditText)findViewById(R.id.tankerDriverNumber);
        tankerWaterSource=(EditText)findViewById(R.id.tankerWaterSource);
        tankerCapacity=(EditText)findViewById(R.id.tankerCapacity);
        stickerColor=(EditText)findViewById(R.id.stickerColor);
        btnRegister=(Button)findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tankerNumber=tankerNo.getText().toString().trim();
                String price1=price.getText().toString().trim();
                String driverName=tankerDriverName.getText().toString().trim();
                String driverContact=tankerDriverPhoneNumber.getText().toString().trim();
                String waterSource=tankerWaterSource.getText().toString().trim();
                String capacity=tankerCapacity.getText().toString().trim();
                String color=stickerColor.getText().toString().trim();

                if(tankerNumber.isEmpty() || price1.isEmpty() || driverContact.isEmpty() || driverName.isEmpty() || waterSource.isEmpty() ||
                        capacity.isEmpty() || color.isEmpty()){
                    Toast.makeText(tankerRegistration.this, "Please enter all the details...", Toast.LENGTH_SHORT).show();
                }else{
                    if(color.toLowerCase().equals("green") || color.toLowerCase().equals("yellow") || color.toLowerCase().equals("blue")){
                        registerTanker(tankerNumber,price1,waterSource,capacity,driverName,driverContact,color);
                    }else{
                        Toast.makeText(tankerRegistration.this, "Please enter proper sticker color..", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    private void registerTanker(String tankerNumber, String price1, String waterSource, String capacity, String driverName, String driverContact, String color) {
//
        try{
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
            Map<String,Object> map=new HashMap<>();
            map.put("tankerNumber",tankerNumber);
            map.put("price",price1);
            map.put("source",waterSource);
            map.put("stickerColor",color);
            map.put("literCapacity",capacity);
            map.put("driverName",driverName);
            map.put("driverContact",driverContact);
            map.put("ownerEmail", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
            map.put("supplierID",uid);

            FirebaseFirestore.getInstance().collection("suppliers").document(uid).collection("tankers").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(tankerRegistration.this, "Registration Successful...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),SupplierHome.class));
                        finish();
                    }else{
                        Toast.makeText(tankerRegistration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

    }
}