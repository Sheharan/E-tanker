package com.example.etanker_supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etanker_supplier.Model.Tanker;
import com.example.etanker_supplier.Utils.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class editTankerDetails extends AppCompatActivity {
    TextView tankerNumber,capacity,sticker;
    EditText price,source,dName,dContact;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String UserId="",tankerNum="";
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tanker_details);
        tankerNumber=findViewById(R.id.tankerNoInEdit);
        capacity=findViewById(R.id.tankerCapacityInEdit);
        price=findViewById(R.id.tankerPriceInEdit);
        source=findViewById(R.id.tankerWaterSourceInEdit);
        sticker=findViewById(R.id.stickerColorInEdit);
        dName=findViewById(R.id.tankerDriverNameInEdit);
        dContact=findViewById(R.id.tankerDriverNumberInEdit);
        update=findViewById(R.id.updateInEdit);
        fAuth=FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        if (getIntent()!=null){
            tankerNum=getIntent().getStringExtra("tankerNum");
        }

        fstore.collection(Common.Suppliers).document(UserId).collection(Common.Tanker)
                .whereEqualTo("tankerNumber",tankerNum).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Tanker tanker=queryDocumentSnapshots.getDocuments().get(0).toObject(Tanker.class);
                String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                assert tanker != null;
                tankerNumber.setText(tanker.getTankerNumber());
                capacity.setText(tanker.getLiterCapacity()+ " Liters");
                price.setText(tanker.getPrice());
                source.setText(tanker.getSource());
                sticker.setText(tanker.getStickerColor());
                dName.setText(tanker.getDriverName());
                dContact.setText(tanker.getDriverContact());

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(editTankerDetails.this);
                //assert tanker1 != null;
                alertDialog.setTitle("Update").setMessage("Do you want to update the details of the tanker ");
                alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fstore.collection(Common.Suppliers).document(UserId).collection(Common.Tanker).whereEqualTo("tankerNumber",tankerNum).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Tanker tanker=queryDocumentSnapshots.getDocuments().get(0).toObject(Tanker.class);
                                String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                                assert tanker != null;
                                if (!dName.getText().toString().trim().contentEquals(tanker.getDriverName())){
                                    fstore.collection(Common.Suppliers).document(UserId).collection(Common.Tanker).document(id).update("driverName",dName.getText().toString().trim());
                                }

                                if (!dContact.getText().toString().trim().contentEquals(tanker.getDriverContact())){
                                    fstore.collection(Common.Suppliers).document(UserId).collection(Common.Tanker).document(id).update("driverContact",dContact.getText().toString().trim());
                                }

                                if (!price.getText().toString().trim().contentEquals(tanker.getPrice())){
                                    fstore.collection(Common.Suppliers).document(UserId).collection(Common.Tanker).document(id).update("licenseNo",price.getText().toString().trim());
                                }

                                if (!source.getText().toString().trim().contentEquals(tanker.getSource())){
                                    fstore.collection(Common.Suppliers).document(UserId).collection(Common.Tanker).document(id).update("source",source.getText().toString().trim());
                                }


                                Toast.makeText(editTankerDetails.this, "successfully updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(editTankerDetails.this,SupplierHome.class));
                                finish();

                            }
                        });







                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                alertDialog.show();

            }
        });



    }




}