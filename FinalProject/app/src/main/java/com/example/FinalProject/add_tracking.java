package com.example.FinalProject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_tracking extends AppCompatActivity implements AdapterMakanan.OnItemClickListener {

    private AdapterMakanan adapter;
    private String selectedDay;
    private String hariTerpilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracking);

        ImageButton backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add_tracking.this, konsumsi_harian.class);
                // Pass the selected day information to the add_tracking activity
                intent.putExtra("DAY_OF_WEEK", hariTerpilih);
                startActivity(intent);
            }
        });

        // Get the selected day from the Intent
        Intent intent = getIntent();
        if (intent.hasExtra("DAY_OF_WEEK")) {
            selectedDay = intent.getStringExtra("DAY_OF_WEEK");
        }

        RecyclerView recyclerView = findViewById(R.id.add_tracking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference baseReference = FirebaseDatabase.getInstance().getReference().child("Makanan");
        FirebaseRecyclerOptions<Makanan> options = new FirebaseRecyclerOptions.Builder<Makanan>().setQuery(baseReference, Makanan.class).build();

        // Initialize the adapter with selectedDay
        adapter = new AdapterMakanan(options, selectedDay);

        // Set the click listener for the adapter
        adapter.setOnItemClickListener(new AdapterMakanan.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                // Lakukan sesuatu jika diperlukan
                Log.d("DEBUG", "Button clicked at position: " + position);
                // atau
                Toast.makeText(add_tracking.this, "Button clicked at position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        Button tambahButton = findViewById(R.id.btn_tambahkan_custom);
        tambahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity();
            }
        });
    }

    private void AddActivity() {
        // Pass the selected day to the Custom activity
        Intent intent = new Intent(this, Custom.class);
        intent.putExtra("DAY_OF_WEEK", selectedDay);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening only if the adapter is not null
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening only if the adapter is not null
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh RecyclerView only if the adapter is not null
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditClick(int position) {
        // Handle edit click
        Log.d("DEBUG", "Button clicked at position: " + position);
        // atau
        Toast.makeText(this, "Button clicked at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
