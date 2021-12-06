package com.example.etanker_supplier.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etanker_supplier.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder  {
    public TextView reviewText;
    public TextView reviewerName,deliveredDate;
    public RatingBar rating;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        reviewText=itemView.findViewById(R.id.content);
        reviewerName= itemView.findViewById(R.id.nameInReviewView);
        deliveredDate=itemView.findViewById(R.id.dateInReview);
        rating=itemView.findViewById(R.id.ratingBar2);
        rating.setNumStars(5);
        rating.setStepSize((float) 0.5);


    }
}
