package com.example.FinalProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Homepage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton profileButton = findViewById(R.id.profile);
        Button seninButton = findViewById(R.id.Senin);
        Button selasaButton = findViewById(R.id.Selasa);
        Button rabuButton = findViewById(R.id.Rabu);
        Button kamisButton = findViewById(R.id.Kamis);
        Button jumatButton = findViewById(R.id.Jumat);
        Button sabtuButton = findViewById(R.id.Sabtu);
        Button mingguButton = findViewById(R.id.Minggu);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Profile.class);
                startActivity(intent);
            }
        });

        // Create a single OnClickListener for all day buttons
        View.OnClickListener dayButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text (day of the week) from the clicked button
                String dayOfWeek = ((Button) v).getText().toString();

                // Start konsumsi_harian activity with the selected day
                Intent intent = new Intent(Homepage.this, konsumsi_harian.class);
                intent.putExtra("DAY_OF_WEEK", dayOfWeek);
                startActivity(intent);
            }
        };

        // Set the same OnClickListener for all day buttons
        seninButton.setOnClickListener(dayButtonClickListener);
        selasaButton.setOnClickListener(dayButtonClickListener);
        rabuButton.setOnClickListener(dayButtonClickListener);
        kamisButton.setOnClickListener(dayButtonClickListener);
        jumatButton.setOnClickListener(dayButtonClickListener);
        sabtuButton.setOnClickListener(dayButtonClickListener);
        mingguButton.setOnClickListener(dayButtonClickListener);
    }
}
