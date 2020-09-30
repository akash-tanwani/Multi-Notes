package com.example.multinotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    private final ArrayList<NewNotes> arrayList= new ArrayList<>();
    private NewNotesAdaptor newNotesAdaptor;
    private RecyclerView recyclerView;
    private static final int OTHER_CODE = 123,EDIT_CODE=456;
    private int pos,pos1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);

        newNotesAdaptor=new NewNotesAdaptor(arrayList,this);

        recyclerView.setAdapter(newNotesAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addItem:
                Toast.makeText(this,"Add Notes", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,EditActivity.class);
                startActivityForResult(intent,OTHER_CODE);
                break;
            case R.id.aboutItem:
                Toast.makeText(this, "About App", Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(this,AboutActivity.class);
                startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == OTHER_CODE) || (requestCode == EDIT_CODE)){
            if (resultCode == RESULT_OK) {
                if (requestCode == EDIT_CODE){
                    arrayList.remove(pos);
                    newNotesAdaptor.notifyDataSetChanged();
                }
                if (data.hasExtra("notes")) {
                    NewNotes n = (NewNotes) data.getSerializableExtra("notes");
                    if (n != null) {
                        arrayList.add(0, n);
                        this.setTitle("Multi Notes("+arrayList.size()+")");
                        newNotesAdaptor.notifyDataSetChanged();
                    }
                }

            } else {
                if (requestCode == OTHER_CODE){
                Toast.makeText(this, "Notes without title or description", Toast.LENGTH_SHORT).show();}
            }

        }
        else {
            Toast.makeText(this, "Unexpected code received: " + requestCode, Toast.LENGTH_SHORT).show();
        }
    }


    private void loadFile() {
        NewNotes newNotes = new NewNotes();
        try {
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.file_name));

            JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            reader.beginArray();
            while (reader.hasNext())
            {
                NewNotes notes=new NewNotes();
                reader.beginObject();

                while(reader.hasNext())
                    {
                    String name = reader.nextName();
                    switch (name)
                    {
                        case "name":
                            notes.setName(reader.nextString());
                            break;
                        case "description":
                            notes.setDescription(reader.nextString());
                            break;
                        case "date":
                            notes.setDateTime(reader.nextString());
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
                arrayList.add(notes);
            } reader.endArray();
            this.setTitle("Multi Notes("+arrayList.size()+")");

        }
        catch (FileNotFoundException ex)
        {
            Toast.makeText(this, "No Data Saved so far.",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        saveProduct();
        super.onPause();
    }

    private void saveProduct() {
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginArray();
            for (NewNotes notes : arrayList) {
                writer.beginObject();
                writer.name("name").value(notes.getName());
                writer.name("description").value(notes.getDescription());
                writer.name("date").value(notes.getDateTime());
                writer.endObject();
            }
                writer.endArray();
                writer.close();
            //Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        pos = recyclerView.getChildLayoutPosition(view);
        NewNotes m = arrayList.get(pos);

        Intent data=new Intent(this,EditActivity.class);
        data.putExtra("notes",m);

        startActivityForResult(data,EDIT_CODE);

    }

    @Override
    public boolean onLongClick(final View view) {
        pos1 = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                arrayList.remove(pos1);
                setTitle("Multi Notes("+arrayList.size()+")");
                newNotesAdaptor.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        NewNotes m = arrayList.get(pos1);
        builder.setMessage("Delete note \'"+m.getName()+"\'?");
        builder.setTitle("Delete Note");

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }
}