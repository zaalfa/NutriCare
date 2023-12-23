package com.example.FinalProject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registrasi extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPass;
    private TextView btnMasuk;
    private Button btnDaftar;
    private FirebaseAuth mAuth;
    private static final String TAG = "RegistrasiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        etEmail = (EditText)findViewById(R.id.txEmail);
        etPass = (EditText)findViewById(R.id.txPass);
        btnMasuk = (TextView) findViewById(R.id.txMasuk);
        btnDaftar = (Button)findViewById(R.id.btRegister);

        mAuth = FirebaseAuth.getInstance();

        btnMasuk.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser(); updateUI(currentUser);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txMasuk) {
            Intent loginIntent = new Intent(Registrasi.this, Login.class);
            startActivity(loginIntent);
        } else if (view.getId() == R.id.btRegister) {
            signUp(etEmail.getText().toString(), etPass.getText().toString());
        }
    }

    public void signUp(String email,String password){
        if (!validateForm()){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new
                        OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult>task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    Toast.makeText(Registrasi.this, user.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a messageto the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Registrasi.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required");
            result = false;
        } else {
            etEmail.setError(null);
        }
        if (TextUtils.isEmpty(etPass.getText().toString())) {
            etPass.setError("Required");
            result = false;
        } else {
            etPass.setError(null);
        }
        return result;
    }
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Registrasi.this, Homepage.class);
            startActivity(intent);
        } else {
            Toast.makeText(Registrasi.this,"Log In First", Toast.LENGTH_SHORT).show();
        }
    }
}