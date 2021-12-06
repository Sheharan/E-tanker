package com.example.etanker_supplier.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etanker_supplier.Interface.ItemClickListener;
import com.example.etanker_supplier.R;

public class TankerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tankerNumber;
    public TextView tankerDriverName;
    public TextView editTanker;

    private ItemClickListener itemClickListener;
    public TankerViewHolder(@NonNull View itemView) {
        super(itemView);
        tankerDriverName=itemView.findViewById(R.id.tankerDriverName1);
        tankerNumber=itemView.findViewById(R.id.tankerNumber);
        editTanker=itemView.findViewById(R.id.editTanker);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

}
