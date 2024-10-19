package com.example.quicknotes;

import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("notes");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    NoteAdapter noteAdapter;
    ProgressBar progressBar;
    TextView tvNoNotes;
    ArrayList<Note> noteList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView ivSignOut = findViewById(R.id.ivSignOut);
        progressBar = findViewById(R.id.progressBarMain);
        tvNoNotes = findViewById(R.id.tvNoNotes);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMain);
        Button btnCreate = findViewById(R.id.btnCreateMain);

        getData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        noteAdapter = new NoteAdapter(noteList, MainActivity.this);
        recyclerView.setAdapter(noteAdapter);

        noteAdapter.setOnClickListener((position, note) -> {
            Intent intent = new Intent(getApplicationContext(), NoteViewActivity.class);
            intent.putExtra("heading", note.getHeading());
            intent.putExtra("content", note.getContent());
            startActivity(intent);
        });

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            startActivity(intent);
        });

        ivSignOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item: snapshot.getChildren()) {
                        if (Objects.requireNonNull(item.child("userId").getValue()).toString().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail())) {
                            String id = Objects.requireNonNull(item.child("id").getValue()).toString();
                            String heading = Objects.requireNonNull(item.child("heading").getValue()).toString();
                            String content = Objects.requireNonNull(item.child("content").getValue()).toString();
                            String userId = Objects.requireNonNull(item.child("userId").getValue()).toString();
                            noteList.add(new Note(id, heading, content, userId));
                            noteAdapter.notifyItemInserted(noteList.size());
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    tvNoNotes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}