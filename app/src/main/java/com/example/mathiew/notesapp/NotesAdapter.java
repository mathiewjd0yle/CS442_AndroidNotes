package com.example.mathiew.notesapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mathiew.notesapp.R;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private static final String TAG = "EmployeesAdapter";
    private final List<Note> notesList;
    private final MainActivity mainAct;

    NotesAdapter(List<Note> nList, MainActivity ma) {
        this.notesList = nList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_recycler, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Employee " + position);

        Note employee = notesList.get(position);

        holder.name.setText(employee.getName());
        holder.department.setText(employee.getDepartment());
        holder.dateTime.setText(new Date().toString());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}