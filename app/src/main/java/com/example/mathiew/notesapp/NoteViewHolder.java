package com.example.mathiew.notesapp;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mathiew.notesapp.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView department;
    TextView dateTime;

    NoteViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.note_title);
        department = view.findViewById(R.id.note_body);
        dateTime = view.findViewById(R.id.dateTime);
    }

}
