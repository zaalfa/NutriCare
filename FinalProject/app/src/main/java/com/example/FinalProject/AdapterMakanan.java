package com.example.FinalProject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class AdapterMakanan extends FirebaseRecyclerAdapter<Makanan, AdapterMakanan.ViewHolder> {

    private OnItemClickListener listener;
    private String selectedDay;

    public AdapterMakanan(@NonNull FirebaseRecyclerOptions<Makanan> options, String selectedDay) {
        super(options);
        this.selectedDay = selectedDay;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Makanan model) {
        String k = String.valueOf(model.getKalori());
        String b = String.valueOf(model.getBerat());

        holder.Nama.setText("Nama: " + model.getNama());
        holder.Kalori.setText("Kalori: " + k);
        holder.Makanan.setText("Berat: " + b);

        holder.Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onEditClick(adapterPosition);

                    // Assuming you have access to the Makanan item at this position
                    Makanan makanan = getItem(adapterPosition);

                    // Perform the database operation to push data from "makanan" to "hari"
                    pushDataToHari(holder.itemView.getContext(), makanan);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tambah, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Nama;
        public TextView Kalori;
        public TextView Makanan;
        public ImageButton Tambah;
        public Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            Nama = itemView.findViewById(R.id.tv_name);
            Kalori = itemView.findViewById(R.id.tv_keterangan);
            Makanan = itemView.findViewById(R.id.tv_keterangan2);
            Tambah = itemView.findViewById(R.id.btn_add2);

            Tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    Log.d("AdapterMakanan", "onClick called at position: " + adapterPosition);
                    if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onEditClick(adapterPosition);

                        // Assuming you have access to the Makanan item at this position
                        Makanan makanan = getItem(adapterPosition);

                        // Perform the database operation to push data from "makanan" to "hari"
                        pushDataToHari(itemView.getContext(), makanan);
                    }
                }
            });

        }
    }

    private void pushDataToHari(Context context, Makanan makanan) {
        Log.d("DEBUG", "Pushing data to Hari: " + makanan.getNama());

        DatabaseReference hariReference = FirebaseDatabase.getInstance().getReference().child("Hari").child(selectedDay).child("Makanan");

        // Push the Makanan data to the "id_makanan" child under the specific day
        hariReference.push().setValue(makanan)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Data successfully pushed, navigate to konsumsi_harian activity
                        navigateToKonsumsiHarian(context);
                    } else {
                        // Handle the case when data push fails
                        Toast.makeText(context, "Failed to push data to Hari: " + task.getException(), Toast.LENGTH_SHORT).show();
                        task.getException().printStackTrace(); // Tambahkan ini untuk melihat jejak tumpukan (stack trace) kesalahan
                    }
                });
    }

    private void navigateToKonsumsiHarian(Context context) {
        Intent intent = new Intent(context, konsumsi_harian.class);
        intent.putExtra("DAY_OF_WEEK", selectedDay);
        context.startActivity(intent);
    }

    public interface OnItemClickListener {
        void onEditClick(int position);
    }
}
