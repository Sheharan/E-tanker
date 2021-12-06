package com.example.etanker_supplier;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etanker_supplier.Model.OrderInfo;
import com.example.etanker_supplier.Model.Supplier;
import com.example.etanker_supplier.Model.Tanker;
import com.example.etanker_supplier.Utils.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import info.hoang8f.widget.FButton;

public class EditSupplierDetails extends AppCompatActivity {
    EditText companyName,phone,location,ownerName;
    TextView email;
    FButton update;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_supplier_details);

        companyName=findViewById(R.id.companyNameInEdit);
        phone=findViewById(R.id.editPhoneInSupplier);
        location=findViewById(R.id.LocationInEditSupplier);
        ownerName=findViewById(R.id.nameInSupplierEdit);
        email=findViewById(R.id.emailInEditSupplier);
        update=findViewById(R.id.updateInSupplierEdit);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        loadDetails();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(EditSupplierDetails.this);
                alertDialog.setTitle("UPDATE?").setIcon(R.drawable.ic_baseline_edit1_24)
                        .setMessage("Are you sure about changing the details ? ")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDetails();
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

    private void loadDetails() {

        fStore.collection(Common.Suppliers).document(UserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Supplier supp= Objects.requireNonNull(task.getResult()).toObject(Supplier.class);
                assert supp != null;
                companyName.setText(supp.getCompany_name());
                phone.setText(supp.getPhone_number());
                location.setText(supp.getLocation());
                ownerName.setText(supp.getName());
                email.setText(supp.getEmail());

            }
        });


    }

    private void updateDetails() {
        final DocumentReference documentReference=fStore.collection(Common.Suppliers).document(UserId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Supplier supplier= Objects.requireNonNull(task.getResult()).toObject(Supplier.class);



                assert supplier != null;
                if(!supplier.getCompany_name().contentEquals(companyName.getText().toString().trim())){
                    documentReference.update("company_name",companyName.getText().toString().trim());
                    updateCompanyNameInOtherPlace(documentReference);
                }

                if(!supplier.getName().contentEquals(ownerName.getText().toString().trim())){
                    documentReference.update("name",ownerName.getText().toString().trim());
                }

                if(!supplier.getPhone_number().contentEquals(phone.getText().toString().trim())){
                    documentReference.update("phone_number",phone.getText().toString().trim());
                }

                if(!supplier.getLocation().contentEquals(location.getText().toString().trim())){
                    documentReference.update("location",location.getText().toString().trim());
                }

                startActivity(new Intent(EditSupplierDetails.this,SupplierHome.class));
                finish();

            }
        });



    }


    private void updateCompanyNameInOtherPlace(final DocumentReference documentReference){
        documentReference.collection(Common.Tanker).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc: Objects.requireNonNull(task.getResult())){
                    final Tanker tanker=doc.toObject(Tanker.class);
                    assert tanker != null;

                    documentReference.collection(Common.OrderDetails).document(tanker.getTankerNumber())
                            .collection(Common.QueueOrder).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc11: Objects.requireNonNull(task.getResult())){
                                String id=doc11.getId();
                                OrderInfo orderData=doc11.toObject(OrderInfo.class);
                                documentReference.collection(Common.OrderDetails).document(tanker.getTankerNumber())
                                        .collection(Common.QueueOrder).document(id).update("supplierCompanyName",companyName.getText().toString().trim());


                                fStore.collection(Common.Consumers).whereEqualTo("email",orderData.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        final String customer_id= Objects.requireNonNull(task.getResult()).getDocuments().get(0).getId();
                                        fStore.collection(Common.Consumers).document(customer_id).collection(Common.QueueOrder)
                                                .whereEqualTo("supplierID",UserId)
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for(DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                                                    String ID=document.getId();
                                                    fStore.collection(Common.Consumers).document(customer_id).collection(Common.QueueOrder)
                                                            .document(ID).update("supplierCompanyName",companyName.getText().toString().trim());
                                                }
                                            }
                                        });
                                    }
                                });
                                
                                

                            }
                        }
                    });


                }
            }
        });


    }


}