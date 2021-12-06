package com.example.etanker_supplier.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etanker_supplier.Interface.ItemClickListener;
import com.example.etanker_supplier.R;

public class SupplierViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView supplierName;
    public TextView supplierPhone;

    private ItemClickListener itemClickListener;

    public SupplierViewHolder(@NonNull View itemView) {
        super(itemView);

        supplierName=itemView.findViewById(R.id.supplierCompanyName);
        supplierPhone= itemView.findViewById(R.id.supplierPhoneNumber);

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
