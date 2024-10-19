package com.example.quicknotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("notes");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText etTextHeading = findViewById(R.id.etTextHeadingNote);
        EditText etTextContent = findViewById(R.id.etTextContentNote);
        Button btnSave = findViewById(R.id.btnSaveNote);

        btnSave.setOnClickListener(v -> {
            String heading = etTextHeading.getText().toString();
            String content = etTextContent.getText().toString();

            if (heading.isEmpty() && content.isEmpty()) {
                Toast.makeText(this, "Write heading and content", Toast.LENGTH_SHORT).show();
            }

            if (!heading.isEmpty() && !content.isEmpty()) {
                String id = String.valueOf(System.currentTimeMillis());
                String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                Note note = new Note(id, heading, content, userId);
                saveNote(note);
            }
        });
    }

    private void saveNote(Note note) {
        databaseReference.child(note.getId()).setValue(note)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}