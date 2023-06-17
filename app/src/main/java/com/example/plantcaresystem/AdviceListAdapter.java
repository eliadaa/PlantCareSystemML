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

    @NonNull
    @Override
    public AdviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_advice, parent, false);
        AdviceListViewHolder viewHolder = new AdviceListViewHolder(view);
        return viewHolder;
    }

    public AdviceListAdapter(List<AdviceListModel> newList){
        this.adviceListInfo = newList;
    }
    @Override
    public void onBindViewHolder(@NonNull AdviceListViewHolder holder, int position) {
        final AdviceListModel newModel = adviceListInfo.get(position);
        holder.setAdvice(newModel.getTitle(), newModel.getAdvice());
    }

    @Override
    public int getItemCount() {
        return adviceListInfo.size();
    }
}
