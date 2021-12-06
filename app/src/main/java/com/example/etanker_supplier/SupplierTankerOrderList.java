package com.example.etanker_supplier;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etanker_supplier.Interface.ItemClickListener;
import com.example.etanker_supplier.Model.OrderInfo;
import com.example.etanker_supplier.Utils.Common;
import com.example.etanker_supplier.ViewHolder.SupplierViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class SupplierTankerOrderList extends AppCompatActivity {
    FirebaseFirestore fstore;
    CollectionReference supplierStore;
    FirebaseAuth fAuth;
    RecyclerView customerOrderList;
    FirestoreRecyclerAdapter<OrderInfo, SupplierViewHolder> adapter1;
    TextView topic;
    String UserID="",tankerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_tanker_order_list);


        topic=findViewById(R.id.DetailsInfoInTankerList);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        UserID= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        supplierStore=fstore.collection(Common.Suppliers);

        customerOrderList=findViewById(R.id.CustomerOrderDetailsInTankerList);
        customerOrderList.hasFixedSize();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        customerOrderList.setLayoutManager(layoutManager);

        if (getIntent()!=null){
            tankerNum=getIntent().getStringExtra("tankersNum");
        }

        loadTankerOrderList(tankerNum);
    }

    private void loadTankerOrderList(String tankerNum){
        Query query=supplierStore.document(UserID).collection(Common.OrderDetails)
                .document(tankerNum).collection(Common.QueueOrder);

        FirestoreRecyclerOptions<OrderInfo> option1 = new FirestoreRecyclerOptions.Builder<OrderInfo>()
                .setQuery(query, OrderInfo.class).build();
        adapter1 = new FirestoreRecyclerAdapter<OrderInfo, SupplierViewHolder>(option1) {
            @Override
            protected void onBindViewHolder(@NonNull SupplierViewHolder supplierViewHolder, int i, @NonNull final OrderInfo orderInfo) {
                supplierViewHolder.supplierName.setText(orderInfo.getName());
                supplierViewHolder.supplierPhone.setText(orderInfo.getOrderedDate());
                topic.setText("Current Place Order");
                supplierViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent tankerDetails=new Intent(SupplierTankerOrderList.this,tankerDetailsSupplier.class);
                        tankerDetails.putExtra("tankerNumber",orderInfo.getTankerNumber());
                        tankerDetails.putExtra("separatingCode",4001);
                        tankerDetails.putExtra("orderedDate",orderInfo.getOrderedDate());
                        startActivity(tankerDetails);
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
        customerOrderList.setAdapter(adapter1);
    }
}