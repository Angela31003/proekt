package com.example.proekt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    EditText editUsername, editPassword, editFirstname, editLastname, editEmail;
    Button btnSignUp;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editFirstname = findViewById(R.id.editFirstname);
        editLastname = findViewById(R.id.editLastname);
        editEmail = findViewById(R.id.editEmail);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(view -> {
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            String firstname = editFirstname.getText().toString();
            String lastname = editLastname.getText().toString();
            String email = editEmail.getText().toString();

            if (dbHelper.registerUser(username, password, firstname, lastname, email)) {
                Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}