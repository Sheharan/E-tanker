package com.example.etanker_supplier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class registerActivity extends AppCompatActivity {

    private EditText supplierName,CompanyName;
    private EditText supplierEmail;
    private EditText supplierPassword;
    private EditText supplierPhone;
    private EditText supplierRegistrationNumber,supplierLocationAddress;

    private  FirebaseAuth fAuth;
    FirebaseFirestore firestoreDB;
    String userID;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CompanyName=findViewById(R.id.registeredCompanyName);
        supplierEmail=findViewById(R.id.supplier_email);
        supplierPassword=findViewById(R.id.supplier_password);
        Button supplierRegister = findViewById(R.id.supplier_register);
        supplierName=findViewById(R.id.supplier_name);
        supplierPhone=findViewById(R.id.supplier_phone);
        supplierRegistrationNumber=findViewById(R.id.supplierRegistrationNumber);
        supplierLocationAddress=findViewById(R.id.supplierLocationAddress);

        progressDialog=new ProgressDialog(this);

        fAuth=FirebaseAuth.getInstance();

        firestoreDB=FirebaseFirestore.getInstance();


        supplierRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regEmail=supplierEmail.getText().toString().trim();
                String regPass=supplierPassword.getText().toString().trim();
                String regName=supplierName.getText().toString().trim();
                String regPhone=supplierPhone.getText().toString().trim();
                String regNum=supplierRegistrationNumber.getText().toString().trim();
                String regAddress=supplierLocationAddress.getText().toString().trim();
                String regCompanyName=CompanyName.getText().toString().trim();


                if(TextUtils.isEmpty(regEmail)||TextUtils.isEmpty(regPass) || TextUtils.isEmpty(regName) || TextUtils.isEmpty(regPhone) ||
                        TextUtils.isEmpty(regNum)||TextUtils.isEmpty(regAddress)||TextUtils.isEmpty(regCompanyName)){
                    Toast.makeText(registerActivity.this, "Please enter all the details ", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                    registerUser(regEmail,regPass,regName,regPhone,regNum,regAddress,regCompanyName);
                }
            }
        });


    }

    private void registerUser(final String regEmail, String regPass, final String regName, final String regPhone, final String regNum,final String regAddress,final String regCompanyName) {
        fAuth.createUserWithEmailAndPassword(regEmail,regPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String,Object> userInfo=new HashMap<>();
                    userInfo.put("company_name",regCompanyName);
                    userInfo.put("name",regName);
                    userInfo.put("email",regEmail);
                    userInfo.put("phone_number",regPhone);
                    userInfo.put("registrationNumber",regNum);
                    userInfo.put("location",regAddress);
                    userID= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    userInfo.put("ID",userID);
                    DocumentReference documentReference=firestoreDB.collection("suppliers").document(userID);

                    documentReference.set(userInfo);

                    Toast.makeText(registerActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),loginActivity.class));
                    finish();
                }else {
                    Toast.makeText(registerActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }



}

