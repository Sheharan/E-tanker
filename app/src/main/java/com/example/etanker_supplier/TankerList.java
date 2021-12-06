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
import com.example.etanker_supplier.Model.Tanker;
import com.example.etanker_supplier.Utils.Common;
import com.example.etanker_supplier.ViewHolder.SupplierViewHolder;
import com.example.etanker_supplier.ViewHolder.TankerViewHolder;
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

public class TankerList extends AppCompatActivity {
    FirebaseFirestore fstore;
    CollectionReference supplierStore;
    FirebaseAuth fAuth;
    RecyclerView tankerList;
    FirestoreRecyclerAdapter<Tanker, TankerViewHolder> adapter1;
    TextView topic;
    String UserID="";
    Intent intent;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanker_list);

        topic=findViewById(R.id.tankerList);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        UserID= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        supplierStore=fstore.collection(Common.Suppliers);

        tankerList=findViewById(R.id.tankerListView);
        tankerList.hasFixedSize();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        tankerList.setLayoutManager(layoutManager);


        intent=this.getIntent();
        value=intent.getIntExtra("activity_id",0);

        supplierStore.document(UserID).collection(Common.Tanker).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if(Objects.requireNonNull(task.getResult()).size()>0){
                        ListTanker();
                    }else{
                        Toast.makeText(TankerList.this, "No Data available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void ListTanker(){
        Query query=supplierStore.document(UserID).collection(Common.Tanker);

        FirestoreRecyclerOptions<Tanker> options=new FirestoreRecyclerOptions.Builder<Tanker>()
                .setQuery(query,Tanker.class).build();

        adapter1=new FirestoreRecyclerAdapter<Tanker, TankerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TankerViewHolder tankerViewHolder, int i, @NonNull final Tanker tanker) {
                tankerViewHolder.tankerNumber.setText(tanker.getTankerNumber());
                tankerViewHolder.tankerDriverName.setText(tanker.getDriverName());
                topic.setText("Tanker List");
                tankerViewHolder.editTanker.setVisibility(View.INVISIBLE);
                tankerViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if(value==1001){
                            Intent orderList=new Intent(TankerList.this,SupplierTankerOrderList.class);
                            orderList.putExtra("tankersNum",tanker.getTankerNumber());
                            startActivity(orderList);
                        }else if (value==1002){
                            Intent feedback=new Intent(TankerList.this,FeedbackList.class);
                            feedback.putExtra("tankersNum",tanker.getTankerNumber());
                            startActivity(feedback);
                        }



                    }
                });

            }

            @NonNull
            @Override
            public TankerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tanker_view, parent, false);
                return new TankerViewHolder(item);


            }
        };
        adapter1.notifyDataSetChanged();
        adapter1.startListening();
        tankerList.setAdapter(adapter1);

    }

}