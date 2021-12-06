package com.example.etanker_supplier;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etanker_supplier.Model.OrderInfo;
import com.example.etanker_supplier.Utils.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import info.hoang8f.widget.FButton;

public class tankerDetailsSupplier extends AppCompatActivity {

    TextView tankerNumber,driverContact,customerAddress,customerName,customerPhone,customerEmail;
    TextView paymentStatus;
    FButton customerLocation,finalizeDeal;
    ImageView deleteOrder;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String UserId="",TankerNum="";
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date="",orderedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanker_details_supplier);

        calendar=Calendar.getInstance();
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        tankerNumber=findViewById(R.id.tankerOrderNumber);
        driverContact=findViewById(R.id.tankerDriverContact);
        customerName=findViewById(R.id.OrderCustomerName);
        customerAddress=findViewById(R.id.OrderedCustomerAddress);
        customerEmail=findViewById(R.id.OrderCustomerEmail);
        customerPhone=findViewById(R.id.OrderCustomerContact);
        paymentStatus=findViewById(R.id.paymentStatus);
        customerLocation=findViewById(R.id.customerMapLocation);
        finalizeDeal=findViewById(R.id.FinalizeOrder);
        deleteOrder=findViewById(R.id.deleteOrder);

        final int value=getIntent().getIntExtra("separatingCode",0);

        if(getIntent()!=null){
            tankerNumber.setText(getIntent().getStringExtra("tankerNumber"));
            //driverContact.setText(getIntent().getStringExtra("driverContact"));

            if (value==4001)
                orderedDate=getIntent().getStringExtra("orderedDate");

        }

        TankerNum=tankerNumber.getText().toString();
        if (value==4001) {
            getTankerDetails1();
        }else{
            getTankerDetails();
        }


        customerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (value ==4001){
                    fstore.collection(Common.Suppliers).document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid()).collection("orderDetails")
                            .document(TankerNum).collection(Common.QueueOrder).whereEqualTo("orderedDate",orderedDate)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if(Objects.requireNonNull(task.getResult()).size()>0) {
                                    OrderInfo orderdetails = task.getResult().getDocuments().get(0).toObject(OrderInfo.class);
                                    assert orderdetails != null;
                                    String latitude= Objects.requireNonNull(orderdetails.getLatitude());
                                    String longitude= Objects.requireNonNull(orderdetails.getLongitude());
                                    Intent customerMap=new Intent(tankerDetailsSupplier.this,CustomerMapLocation.class);
                                    customerMap.putExtra("latitude",latitude);
                                    customerMap.putExtra("longitude",longitude);
                                    startActivity(customerMap);
                                }else {
                                    Toast.makeText(tankerDetailsSupplier.this, "no data to show in the map", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }else {
                    fstore.collection(Common.Suppliers).document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid()).collection("orderDetails")
                            .document(TankerNum).collection(Common.QueueOrder).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if(Objects.requireNonNull(task.getResult()).size()>0) {
                                    OrderInfo orderdetails = task.getResult().getDocuments().get(0).toObject(OrderInfo.class);
                                    assert orderdetails != null;
                                    String latitude= Objects.requireNonNull(orderdetails.getLatitude());
                                    String longitude= Objects.requireNonNull(orderdetails.getLongitude());
                                    Intent customerMap=new Intent(tankerDetailsSupplier.this,CustomerMapLocation.class);
                                    customerMap.putExtra("latitude",latitude);
                                    customerMap.putExtra("longitude",longitude);
                                    startActivity(customerMap);
                                }else {
                                    Toast.makeText(tankerDetailsSupplier.this, "no data to show in the map", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }


            }
        });

        finalizeDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fstore.collection(Common.Suppliers).document(UserId).collection(Common.OrderDetails).document(TankerNum).
                        collection(Common.QueueOrder).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (value==4001){
                            int count=0;
                            for(DocumentSnapshot doc: Objects.requireNonNull(task.getResult())){
                                OrderInfo orderdata=doc.toObject(OrderInfo.class);
                                count++;
                                assert orderdata != null;
                                if (orderdata.getOrderedDate().contentEquals(orderedDate))
                                    break;
                            }

                            if(count<5){
                                orderWithDataInLine();
                            }else {
                                warningMessage();
                            }

                        }else {
                            if (task.isSuccessful()){
                                if(Objects.requireNonNull(task.getResult()).size()>0){

                                    OrderWithData();
                                }else {

                                    OrderWithOutData();
                                }
                            }
                        }


                    }
                });

            }
        });

        deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(tankerDetailsSupplier.this);
                alertDialog.setTitle("Cancel Order").setIcon(R.drawable.ic_baseline_delete_24)
                        .setMessage("The order must be cancelled only after mutual understanding of the supplier and customer." +
                                "Please enter the verification number.");

                final EditText verifyNum1=new EditText(tankerDetailsSupplier.this);
                LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

                verifyNum1.setLayoutParams(ip);
                alertDialog.setView(verifyNum1);

                alertDialog.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(verifyNum1.getText().toString().isEmpty()){
                                    Toast.makeText(tankerDetailsSupplier.this, "Sorry Verification number is must to delete order", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                final CollectionReference collection=fstore.collection(Common.Suppliers)
                                        .document(UserId).collection(Common.OrderDetails);

                                collection.document(TankerNum).collection(Common.QueueOrder).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            String verificationNumber=verifyNum1.getText().toString().trim();
                                            String doc_id="",user_email="",date11="";

                                            for(DocumentSnapshot doc: Objects.requireNonNull(task.getResult())){
                                                OrderInfo orderInfo=doc.toObject(OrderInfo.class);
                                                assert orderInfo != null;
                                                String verNum=orderInfo.getVerificationNumber();
                                                if (verNum.matches(verificationNumber)) {
                                                    user_email = orderInfo.getEmail();
                                                    doc_id = doc.getId();
                                                    Toast.makeText(tankerDetailsSupplier.this, doc_id, Toast.LENGTH_SHORT).show();
                                                    date11 = orderInfo.getOrderedDate();
                                                }
                                            }

                                            if(!doc_id.isEmpty()){
                                                collection.document(TankerNum).collection(Common.QueueOrder).document(doc_id).delete();
                                                final String finalDate1 = date11;
                                                fstore.collection(Common.Consumers).whereEqualTo("email",user_email)
                                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        String customer_Id=queryDocumentSnapshots.getDocuments().get(0).getId();
                                                        fstore.collection(Common.Consumers).document(customer_Id).collection(Common.QueueOrder)
                                                                .document(finalDate1 +TankerNum).delete();

                                                        startActivity(new Intent(tankerDetailsSupplier.this,SupplierHome.class));
                                                        finish();
                                                    }
                                                });


                                            }

                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });


    }

    private void warningMessage(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(tankerDetailsSupplier.this);
        alertDialog.setTitle("Error").setIcon(R.drawable.ic_baseline_error_24)
                .setMessage("Please finalize the previous order List before finalizing this one.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    private void OrderWithOutData(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(tankerDetailsSupplier.this);
        alertDialog.setTitle("There is no Order Available to finalize")
                .setIcon(R.drawable.tankerpic)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    private void OrderWithData(){
        fstore.collection(Common.Suppliers).document(UserId).collection(Common.OrderDetails)
                .document(TankerNum).collection(Common.QueueOrder).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    OrderInfo orderInfo1= Objects.requireNonNull(task.getResult()).getDocuments().get(0).toObject(OrderInfo.class);
                    final AlertDialog.Builder alertDialog=new AlertDialog.Builder(tankerDetailsSupplier.this);
                    alertDialog.setTitle("Have you delivered water to "+orderInfo1.getName())
                            .setMessage("Verification Number")
                            .setIcon(R.drawable.tankerpic);
                    final EditText verifyNum=new EditText(tankerDetailsSupplier.this);
                    LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );

                    verifyNum.setLayoutParams(ip);
                    alertDialog.setView(verifyNum);

                    alertDialog.setPositiveButton("Already", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(verifyNum.getText().toString().isEmpty()){
                                Toast.makeText(tankerDetailsSupplier.this, "Sorry Verification number is must to finalize order", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final CollectionReference collection=fstore.collection(Common.Suppliers)
                                    .document(UserId).collection(Common.OrderDetails);

                            collection.document(TankerNum).collection(Common.QueueOrder).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    String verificationNumber=verifyNum.getText().toString().trim();


                                    final String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                                    Log.d("My Activity",id);
                                    final OrderInfo orderInfo11=queryDocumentSnapshots.getDocuments().get(0).toObject(OrderInfo.class);



                                    assert orderInfo11 != null;

                                    if (orderInfo11.getVerificationNumber().contentEquals(verificationNumber)){

                                        fstore.collection(Common.Consumers).whereEqualTo("email",orderInfo11.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @SuppressLint("SimpleDateFormat")
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for(QueryDocumentSnapshot doc11:queryDocumentSnapshots){
                                                    String customerID=doc11.getId();
                                                    Log.d("My Activity",customerID);
                                                    Log.d("My Activity","checkpoint"+orderInfo11.getEmail());
                                                    dateFormat=new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a ");
                                                    date=dateFormat.format(calendar.getTime());

                                                    Map<String,Object> info=new HashMap<>();
                                                    info.put("deliveredDate",date);

                                                    collection.document(TankerNum).collection(Common.DeliveredRecords).document(customerID).collection(orderInfo11.getName()).document(date).set(orderInfo11);
                                                    fstore.collection(Common.Consumers).document(customerID).collection(Common.DeliveredRecords).document(date).set(orderInfo11);

                                                    collection.document(TankerNum).collection(Common.DeliveredRecords).document(customerID).collection(orderInfo11.getName()).document(date).set(info,SetOptions.merge());
                                                    fstore.collection(Common.Consumers).document(customerID).collection(Common.DeliveredRecords).document(date).set(info,SetOptions.merge());

                                                    collection.document(TankerNum).collection(Common.QueueOrder).document(id).delete();
                                                    fstore.collection(Common.Consumers).document(customerID).collection(Common.QueueOrder).document(orderInfo11.getOrderedDate()+TankerNum).delete();


                                                    startActivity(new Intent(tankerDetailsSupplier.this,SupplierHome.class));
                                                    finish();

                                                }
                                            }
                                        });

                                    }else {
                                        Toast.makeText(tankerDetailsSupplier.this, "Verfication Number did not match Please make sure you entered the correct one obtained from Customer", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                            dialog.dismiss();
                        }
                    }).setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            }
        });
    }


    private void orderWithDataInLine(){
        fstore.collection(Common.Suppliers).document(UserId).collection(Common.OrderDetails)
                .document(TankerNum).collection(Common.QueueOrder).whereEqualTo("orderedDate",orderedDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    OrderInfo orderInfo1= Objects.requireNonNull(task.getResult()).getDocuments().get(0).toObject(OrderInfo.class);
                    final AlertDialog.Builder alertDialog=new AlertDialog.Builder(tankerDetailsSupplier.this);
                    alertDialog.setTitle("Have you delivered water to "+orderInfo1.getName())
                            .setMessage("Verification Number")
                            .setIcon(R.drawable.tankerpic);
                    final EditText verifyNum=new EditText(tankerDetailsSupplier.this);
                    LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );

                    verifyNum.setLayoutParams(ip);
                    alertDialog.setView(verifyNum);

                    alertDialog.setPositiveButton("Already", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(verifyNum.getText().toString().isEmpty()){
                                Toast.makeText(tankerDetailsSupplier.this, "Sorry Verification number is must to finalize order", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final CollectionReference collection=fstore.collection(Common.Suppliers)
                                    .document(UserId).collection(Common.OrderDetails);

                            collection.document(TankerNum).collection(Common.QueueOrder).whereEqualTo("orderedDate",orderedDate).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    String verificationNumber=verifyNum.getText().toString().trim();

                                    final String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                                    
                                    final OrderInfo orderInfo11=queryDocumentSnapshots.getDocuments().get(0).toObject(OrderInfo.class);



                                    assert orderInfo11 != null;

                                    if (orderInfo11.getVerificationNumber().contentEquals(verificationNumber)){
                                        Toast.makeText(tankerDetailsSupplier.this, "You are on right path", Toast.LENGTH_SHORT).show();

                                        fstore.collection(Common.Consumers).whereEqualTo("email",orderInfo11.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @SuppressLint("SimpleDateFormat")
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for(QueryDocumentSnapshot doc11:queryDocumentSnapshots){
                                                    String customerID=doc11.getId();
                                                    dateFormat=new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a ");
                                                    date=dateFormat.format(calendar.getTime());

                                                    Map<String,Object> info=new HashMap<>();
                                                    info.put("deliveredDate",date);

                                                    collection.document(TankerNum).collection(Common.DeliveredRecords).document(customerID).collection(orderInfo11.getName()).document(date).set(orderInfo11);
                                                    fstore.collection(Common.Consumers).document(customerID).collection(Common.DeliveredRecords).document(date).set(orderInfo11);

                                                    collection.document(TankerNum).collection(Common.DeliveredRecords).document(customerID).collection(orderInfo11.getName()).document(date).set(info,SetOptions.merge());
                                                    fstore.collection(Common.Consumers).document(customerID).collection(Common.DeliveredRecords).document(date).set(info,SetOptions.merge());

                                                    collection.document(TankerNum).collection(Common.QueueOrder).document(id).delete();
                                                    fstore.collection(Common.Consumers).document(customerID).collection(Common.QueueOrder).document(orderInfo11.getOrderedDate()+TankerNum).delete();


                                                    startActivity(new Intent(tankerDetailsSupplier.this,SupplierHome.class));
                                                    finish();

                                                }
                                            }
                                        });

                                    }else {
                                        Toast.makeText(tankerDetailsSupplier.this, "Verfication Number did not match Please make sure you entered the correct one obtained from Customer", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                            Toast.makeText(tankerDetailsSupplier.this, "Thank you", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            }
        });

    }

    private void getTankerDetails() {
        fstore.collection(Common.Suppliers).document(fAuth.getCurrentUser().getUid())
                .collection("orderDetails")
                .document(TankerNum).collection(Common.QueueOrder)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   if(Objects.requireNonNull(task.getResult()).size()>0) {
                       OrderInfo orderdetails=task.getResult().getDocuments().get(0).toObject(OrderInfo.class);
                       customerName.setText(orderdetails.getName());
                       customerEmail.setText(orderdetails.getEmail());
                       customerPhone.setText(orderdetails.getPhone());
                       customerAddress.setText(orderdetails.getAddress());

                       if(orderdetails.getPaymentToken()==null){
                           paymentStatus.setText("Not Finalize");
                       }else{
                           paymentStatus.setText("Already done");
                       }


                   }
               }
            }
        });

    }


    private void getTankerDetails1() {
        fstore.collection(Common.Suppliers).document(fAuth.getCurrentUser().getUid())
                .collection("orderDetails")
                .document(TankerNum).collection(Common.QueueOrder).whereEqualTo("orderedDate",orderedDate)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(Objects.requireNonNull(task.getResult()).size()>0) {
                        OrderInfo orderdetails=task.getResult().getDocuments().get(0).toObject(OrderInfo.class);
                        customerName.setText(orderdetails.getName());
                        customerEmail.setText(orderdetails.getEmail());
                        customerPhone.setText(orderdetails.getPhone());
                        customerAddress.setText(orderdetails.getAddress());

                        if(orderdetails.getPaymentToken()==null){
                            paymentStatus.setText("Not Finalize");
                        }else{
                            paymentStatus.setText("Already done");
                        }
                    }
                }
            }
        });

    }
}
