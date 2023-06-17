package com.example.plantcaresystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdviceListViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_title, tv_advice;

    public AdviceListViewHolder(@NonNull View itemView) {
        super(itemView);
        init();
    }

    private void init(){
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_advice = itemView.findViewById(R.id.tv_advice);
    }

    public void setAdvice(String title, String advice){
        tv_title.setText(title);
        tv_advice.setText(advice);
    }
}
