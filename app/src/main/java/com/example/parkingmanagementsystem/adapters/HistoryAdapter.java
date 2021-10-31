package com.example.parkingmanagementsystem.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagementsystem.Location;
import com.example.parkingmanagementsystem.databinding.BookedSlotTempletBinding;
import com.example.parkingmanagementsystem.databinding.HistorytempletBinding;
import com.example.parkingmanagementsystem.model.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    List<History> data;

    public HistoryAdapter(List<History> data) {
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        HistorytempletBinding historytempletBinding;
        public MyViewHolder(HistorytempletBinding historytempletBinding) {
            super(historytempletBinding.getRoot());
            this.historytempletBinding=historytempletBinding;

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        HistorytempletBinding historytempletBinding=HistorytempletBinding.inflate(layoutInflater,parent,false);
        return new MyViewHolder(historytempletBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        History history=data.get(position);
        holder.historytempletBinding.setHistorydata(history);
        holder.historytempletBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
