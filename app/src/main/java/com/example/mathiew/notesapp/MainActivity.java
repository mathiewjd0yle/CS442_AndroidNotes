package com.example.mathiew.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mathiew.notesapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private final String TAG = getClass().getSimpleName();
    private final ArrayList<Note> noteList = new ArrayList<>();
    private NotesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        mAdapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::getEmployeeDataResult);
        this.setTitle("Android Notes" +  " (" + noteList.size() + ")");
        loadFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.to_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.to_info_page:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.add_note:
                Intent intent2 = new Intent(this, EditActivity.class);
                activityResultLauncher.launch(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getEmployeeDataResult(ActivityResult activityResult) {
        Log.d(TAG, "getEmployeeDataResult: ");
        if (activityResult.getResultCode() == RESULT_OK) {
            Intent data = activityResult.getData();
            if (data == null)
                return;
            if (data.hasExtra("NEW_EMPLOYEE")) {
                Note newEmp = (Note) data.getSerializableExtra("NEW_EMPLOYEE");
                noteList.add(newEmp);
                saveNote();
                mAdapter.notifyItemInserted(noteList.size());
            } else if (data.hasExtra("UPDATE_EMPLOYEE")) {
                Note editEmp = (Note) data.getSerializableExtra("UPDATE_EMPLOYEE");
                int pos = data.getIntExtra("UPDATE_POS", 0);

                Note toUpdate = noteList.get(pos);
                toUpdate.setName(editEmp.getName());
                toUpdate.setDepartment(editEmp.getDepartment());
                mAdapter.notifyItemChanged(pos);
                saveNote();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note e = noteList.get(pos);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("EDIT_EMPLOYEE", e);
        intent.putExtra("EDIT_POS", pos);

        activityResultLauncher.launch(intent);
        this.setTitle("Android Notes" +  " (" + noteList.size() + ")");
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note e = noteList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        this.setTitle("Android Notes" +  " (" + noteList.size() + ")");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteList.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                saveNote();
            }
        });
        builder.setNegativeButton("NO", (dialogInterface, i) -> {
        });
        builder.setTitle("Delete Note?");
        builder.setMessage("Delete note '" + e.getName() + "'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private void saveNote() { // this works, but my load method is not yet working
        String output = toJSON(noteList);
        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(output);
            printWriter.close();
            fos.close();

            Log.d(TAG, "saveNote:\n" + output);
            Toast.makeText(this, getString(R.string.note) , Toast.LENGTH_SHORT).show();
            this.setTitle("Android Notes" +  " (" + noteList.size() + ")");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private Note loadFile() {
        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("name");
                String body = jsonObject.getString("description");
                Note n = new Note(title, body);
                noteList.add(n);
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "no file found.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @NonNull
    public String toJSON(ArrayList<Note> noteList) {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");

            jsonWriter.beginArray();
            for (Note n : noteList) {
                jsonWriter.beginObject();

                jsonWriter.name("name").value(n.getName());
                jsonWriter.name("description").value(n.getDepartment());

                jsonWriter.endObject();
            }
            jsonWriter.endArray();

            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPause(){
        saveNote();
        super.onPause();
    }
}