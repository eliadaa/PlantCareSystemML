package com.example.plantcaresystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdviceListAdapter extends RecyclerView.Adapter<AdviceListViewHolder> {

    private List<AdviceListModel> adviceListInfo;

    private Context context;

    public AdviceListAdapter(List<AdviceListModel> adviceListInfo) {
        this.adviceListInfo = adviceListInfo;
    }

    @NonNull
    @Override
    public AdviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_advice, parent, false);
        return new AdviceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdviceListViewHolder holder, int position) {
        AdviceListModel advice = adviceListInfo.get(position);
        holder.setAdvice(advice.getTitle(), advice.getAdvice());
    }

    @Override
    public int getItemCount() {
        return adviceListInfo.size();
    }
}
