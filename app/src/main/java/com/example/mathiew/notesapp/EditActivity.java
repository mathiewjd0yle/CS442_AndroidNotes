package com.example.mathiew.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mathiew.notesapp.R;

public class EditActivity extends AppCompatActivity {

    private EditText name;
    private EditText dept;

    public Note editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = findViewById(R.id.edit_title);
        dept = findViewById(R.id.edit_body);

        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_EMPLOYEE")) {
            editNote = (Note) intent.getSerializableExtra("EDIT_EMPLOYEE");
            name.setText(editNote.getName());
            dept.setText(editNote.getDepartment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editnote_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                if(name.getText().toString().isEmpty()){
                    Toast.makeText(this, "No title. Your note will not save.", Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }
                else{
                    String nameText = name.getText().toString();
                    String deptText = dept.getText().toString();

                    Note e = new Note(nameText, deptText);

                    String key = "NEW_EMPLOYEE";

                    Intent intent = getIntent();
                    if (intent.hasExtra("EDIT_EMPLOYEE")) {
                        key = "UPDATE_EMPLOYEE";
                    }

                    Intent data = new Intent();
                    data.putExtra(key, e);
                    if (intent.hasExtra("EDIT_POS")) {
                        int pos = intent.getIntExtra("EDIT_POS", 0);
                        data.putExtra("UPDATE_POS", pos);
                    }
                    setResult(RESULT_OK, data);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void doSave(View v) {
        String nameText = name.getText().toString();
        String deptText = dept.getText().toString();

        Note e = new Note(nameText, deptText);

        String key = "NEW_EMPLOYEE";

        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_EMPLOYEE")) {
            key = "UPDATE_EMPLOYEE";
        }

        Intent data = new Intent();
        data.putExtra(key, e);
        if (intent.hasExtra("EDIT_POS")) {
            int pos = intent.getIntExtra("EDIT_POS", 0);
            data.putExtra("UPDATE_POS", pos);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        String nameText = name.getText().toString().trim();
        String deptText = dept.getText().toString().trim();

        if (editNote != null) {
            if (editNote.getName().equals(nameText) &&
                    editNote.getDepartment().equals(deptText)) {
                super.onBackPressed();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doSave(null);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditActivity.super.onBackPressed();
            }
        });
        builder.setTitle("Unsaved Changes");
        builder.setMessage("Do you want to save your changes before exiting?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}