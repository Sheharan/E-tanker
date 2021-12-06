package com.example.etanker_supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etanker_supplier.Interface.ItemClickListener;
import com.example.etanker_supplier.Model.OrderInfo;
import com.example.etanker_supplier.Utils.Common;
import com.example.etanker_supplier.ViewHolder.SupplierViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class SupplierSectionOrderList extends AppCompatActivity {
    FirebaseFirestore fstore;
    CollectionReference supplierStore;
    FirebaseAuth fAuth;
    RecyclerView tankerOrderList;
    FirestoreRecyclerAdapter<OrderInfo, SupplierViewHolder> adapter1;
    TextView topic;
    String UserID="",tankerNum="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_section_order_list);

        topic=findViewById(R.id.DetailsInfoInSupplier);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        UserID= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        supplierStore=fstore.collection(Common.Suppliers);

        tankerOrderList=findViewById(R.id.CustomerOrderDetailsInSupplier);
        tankerOrderList.hasFixedSize();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        tankerOrderList.setLayoutManager(layoutManager);


        if(getIntent()!=null){
            tankerNum=getIntent().getStringExtra("tankersNum");
        }

        supplierStore.document(UserID).collection(Common.OrderDetails)
                .document(tankerNum).collection(Common.QueueOrder)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if(Objects.requireNonNull(task.getResult()).size()>0){
                        ListOrderedTanker();

                    }else{
                        Toast.makeText(SupplierSectionOrderList.this, "No Data available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void ListOrderedTanker(){
        Query query=supplierStore.document(UserID).collection(Common.OrderDetails)
                .document(tankerNum).collection(Common.QueueOrder);

        FirestoreRecyclerOptions<OrderInfo> option1 = new FirestoreRecyclerOptions.Builder<OrderInfo>()
                .setQuery(query, OrderInfo.class).build();
        adapter1 = new FirestoreRecyclerAdapter<OrderInfo, SupplierViewHolder>(option1) {
            @Override
            protected void onBindViewHolder(@NonNull SupplierViewHolder supplierViewHolder, int i, @NonNull final OrderInfo orderInfo) {
                supplierViewHolder.supplierName.setText(orderInfo.getName());
                supplierViewHolder.supplierPhone.setText(orderInfo.getPhone());
                topic.setText("Current Place Order");
                supplierViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent orderDetails=new Intent(SupplierSectionOrderList.this,SupplierTankerOrderList.class);
                        orderDetails.putExtra("tankerNumber",orderInfo.getTankerNumber());
                        startActivity(orderDetails);


                    }
                });

            }

            @NonNull
            @Override
            public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.supplier_view, parent, false);
                return new SupplierViewHolder(item);
            }
        };
        adapter1.notifyDataSetChanged();
        adapter1.startListening();
        tankerOrderList.setAdapter(adapter1);


    }
}