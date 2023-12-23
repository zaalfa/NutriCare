package com.example.FinalProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Custom extends AppCompatActivity {
    private EditText etNama, etKalori, etBerat;
    private Button buttonSave;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Makanan makanan;
    private String hariTerpilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        hariTerpilih = getIntent().getStringExtra("DAY_OF_WEEK"); // Change to DAY_OF_WEEK
        etNama = findViewById(R.id.et_nama);
        etKalori = findViewById(R.id.et_kalori);
        etBerat = findViewById(R.id.et_berat);
        buttonSave = findViewById(R.id.buttonSave);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        makanan = new Makanan();
        ImageButton backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Custom.this, add_tracking.class);
                // Pass the selected day information to the add_tracking activity
                intent.putExtra("DAY_OF_WEEK", hariTerpilih);
                startActivity(intent);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
    }

    private void simpanData() {
        String hari = hariTerpilih;
        String nama = etNama.getText().toString();
        String kaloriStr = etKalori.getText().toString().trim();
        String beratStr = etBerat.getText().toString().trim();

        if (nama.isEmpty() || kaloriStr.isEmpty() || beratStr.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        int kalori, berat;

        try {
            kalori = Integer.parseInt(kaloriStr);
            berat = Integer.parseInt(beratStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Kalori dan berat harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        if (kalori <= 0 || berat <= 0) {
            Toast.makeText(this, "Kalori dan berat harus lebih dari 0", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userRef = databaseReference.child("Makanan");

        Makanan makanan = new Makanan(nama, kalori, berat);

        userRef.push().setValue(makanan);

        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, add_tracking.class);
        startActivity(intent);

        etNama.setText("");
        etKalori.setText("");
        etBerat.setText("");
    }
}