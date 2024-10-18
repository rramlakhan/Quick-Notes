package com.example.quicknotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText etTextEmailAddress = findViewById(R.id.etTextEmailAddressSignUp);
        EditText etTextPassword = findViewById(R.id.etTextPasswordSignUp);
        EditText etTextConfirmPassword = findViewById(R.id.etTextConfirmPasswordSignUp);
        progressBar = findViewById(R.id.progressBarSignUp);
        Button btnSignUp = findViewById(R.id.btnSignUpSignUp);
        TextView tvSignIn = findViewById(R.id.tvSignInSignUp);

        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        });

        btnSignUp.setOnClickListener(v -> {
            String email = etTextEmailAddress.getText().toString();
            String password = etTextPassword.getText().toString();
            String confirmPassword = etTextConfirmPassword.getText().toString();

            if (email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                etTextEmailAddress.setError("This field can't be empty");
                etTextPassword.setError("This field can't be empty");
                etTextConfirmPassword.setError("This field can't be empty");
            }
            if (email.isEmpty()) {
                etTextEmailAddress.setError("This field can't be empty");
            }
            if (password.isEmpty()) {
                etTextPassword.setError("This field can't be empty");
            }
            if (confirmPassword.isEmpty()) {
                etTextConfirmPassword.setError("This field can't be empty");
            }

            if (!email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                if (confirmPassword.equals(password)) {
                    createUser(email, password);
                } else {
                    Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}