package com.example.etanker_supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.etanker_supplier.Model.Reviews;
import com.example.etanker_supplier.Utils.Common;
import com.example.etanker_supplier.ViewHolder.ReviewViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class FeedbackList extends AppCompatActivity {

    TextView topic;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    RecyclerView reviewsList;
    FirestoreRecyclerAdapter<Reviews, ReviewViewHolder> adapter;
    String UserId="",tankerNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        fAuth=FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        reviewsList=findViewById(R.id.reviewsList);
        topic=findViewById(R.id.topicInFeedback);
        UserId= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        reviewsList.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviewsList.setLayoutManager(layoutManager);

        if (getIntent()!=null){
            tankerNum=getIntent().getStringExtra("tankersNum");
        }
        fstore.collection(Common.Feedback).document(UserId).collection(tankerNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            if (Objects.requireNonNull(task.getResult()).size()>0){
                                listFeedback();
                            }

                        }
                    }
                });

    }

    private void listFeedback(){
        Query query=fstore.collection(Common.Feedback).document(UserId)
                .collection(tankerNum).orderBy("deliveredDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Reviews> options=new FirestoreRecyclerOptions.Builder<Reviews>()
                .setQuery(query,Reviews.class).build();

        adapter=new FirestoreRecyclerAdapter<Reviews, ReviewViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i, @NonNull Reviews reviews) {
                reviewViewHolder.reviewerName.setText(reviews.getFeedbackBy());
                reviewViewHolder.reviewText.setText(reviews.getFeedback());
                reviewViewHolder.rating.setRating(Float.parseFloat(reviews.getRating()));
                reviewViewHolder.deliveredDate.setText(reviews.getDeliveredDate());
                topic.setText("Feedback");

            }

            @NonNull
            @Override
            public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.review_view,parent,false);

                return new ReviewViewHolder(itemView);
            }
        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        reviewsList.setAdapter(adapter);
    }
}