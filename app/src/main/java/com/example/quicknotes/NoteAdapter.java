package com.example.quicknotes;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final ArrayList<Note> list;
    Context context;
    OnClickListener onClickListener;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("notes");


    public NoteAdapter(ArrayList<Note> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note item = list.get(position);
        holder.tvHeading.setText(item.getHeading());
        holder.tvContent.setText(item.getContent());

        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick(position, item);
            }
        });

        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.layout_edit_note);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        EditText etTextEditHeading = dialog.findViewById(R.id.etTextEditeHeading);
        EditText etTextEditContent = dialog.findViewById(R.id.etTextEditeContent);
        Button btnSave = dialog.findViewById(R.id.btnSaveEdit);

        btnSave.setOnClickListener(v -> {
            String heading = etTextEditHeading.getText().toString();
            String content = etTextEditContent.getText().toString();

            if (!heading.isEmpty() && !content.isEmpty()) {
                databaseReference.child(item.getId()).child("heading").setValue(heading);
                databaseReference.child(item.getId()).child("content").setValue(content);
                notifyItemChanged(holder.getAdapterPosition());
                dialog.dismiss();
            }
        });

        holder.ivEdit.setOnClickListener(v -> {
            etTextEditHeading.setText(item.getHeading());
            etTextEditContent.setText(item.getContent());
            dialog.show();
        });

        holder.ivDelete.setOnClickListener(v -> {
            databaseReference.child(item.getId()).removeValue();
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeading;
        TextView tvContent;
        ImageView ivEdit;
        ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Note note);
    }
}
