package com.example.parkingmanagementsystem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagementsystem.R;
import com.example.parkingmanagementsystem.classes.Parking_status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder>{

    static Context context;
    ArrayList<String> list;

    public RankingAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView slot_id ;
        TextView status ;

        ImageView sts_symbol ;



        public ViewHolder(View itemView) {
            super(itemView);

            slot_id = (TextView) itemView.findViewById(R.id.slot_id);
            status = (TextView)  itemView.findViewById(R.id.status);
//            sts_symbol =  itemView.findViewById(R.id.sts_symbol);



        }
    }

    @NonNull
    @Override
    public RankingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.parking_templet,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final RankingAdapter.ViewHolder holder, int position) {

        try {
            JSONObject jsonObject=new JSONObject(list.get(position));

            holder.slot_id.setText(jsonObject.getString("slot_id"));
            holder.status.setText(jsonObject.getString("status"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



}


