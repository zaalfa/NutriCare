package com.example.FinalProject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.FinalProject.Makanan;
import com.example.FinalProject.R;

import java.util.List;

public class AdapterKonsumsi extends RecyclerView.Adapter<AdapterKonsumsi.ViewHolder> {

    private List<Makanan> makananList;

    public AdapterKonsumsi(List<Makanan> makananList) {
        this.makananList = makananList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Makanan makanan = makananList.get(position);

        holder.tvNama.setText(makanan.getNama());
        holder.tvKalori.setText("Kalori: " + makanan.getKalori());
        holder.tvBerat.setText("Berat: " + makanan.getBerat());
    }

    @Override
    public int getItemCount() {
        return makananList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama;
        public TextView tvKalori;
        public TextView tvBerat;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_name);
            tvKalori = itemView.findViewById(R.id.kalori);
            tvBerat = itemView.findViewById(R.id.berat);
        }
    }

    public void setData(List<Makanan> newData) {
        makananList.clear();
        makananList.addAll(newData);
        notifyDataSetChanged();
    }
}

