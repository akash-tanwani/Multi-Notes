package com.example.multinotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.multinotes.R.id.title;

public class EditActivity extends AppCompatActivity {

    private static EditText name;
    private EditText description;
    private NewNotes newNotes = new NewNotes();
    private NewNotes n;
    private String t_name="",t_description="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = findViewById(R.id.title);
        name.setTextIsSelectable(true);
        description = findViewById(R.id.description);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setTextIsSelectable(true);

        Intent data = getIntent();
        if (data.hasExtra("notes"))
        {
            n = (NewNotes) data.getSerializableExtra("notes");
            if (n != null)
            {
                t_name=n.getName();
                t_description=n.getDescription();
                name.setText(n.getName());
                description.setText(n.getDescription());
            }
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveItem:
                Toast.makeText(this, "Save Notes", Toast.LENGTH_SHORT).show();
                saveit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if((name.getText().toString()).equals(t_name) && (description.getText().toString()).equals(t_description)){
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    saveit();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

            builder.setMessage("Save note \' "+name.getText().toString()+" \'?");
            builder.setTitle("save Notes");

            AlertDialog dialog = builder.create();
            dialog.show();


            //super.onBackPressed();
        }
    }

    public void saveit()
    {
        if((name.getText().toString()).equals("") || (description.getText().toString()).equals("") || (name.getText().toString()).trim().length()==0){
            Toast.makeText(this,"notes without title or description can't be saved",Toast.LENGTH_SHORT).show();
                finish();
        }
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat ("E MMM dd',' YYYY hh:mm:ss a ");

        NewNotes n=new NewNotes();
        n.setName(name.getText().toString());
        n.setDateTime(dateFormat.format(d));
        n.setDescription(description.getText().toString());

        Intent data = new Intent();
        data.putExtra("notes",n);
        setResult(RESULT_OK,data);
        Toast.makeText(this,"Notes Saved",Toast.LENGTH_SHORT).show();
        finish();
    }


}