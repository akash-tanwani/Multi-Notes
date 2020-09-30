package com.example.multinotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NewNotesAdaptor extends RecyclerView.Adapter<MyViewHolder>{
    private List<NewNotes> notesList;
    private MainActivity mainAct;

    public NewNotesAdaptor(List<NewNotes> nList, MainActivity mainAct) {
        this.notesList = nList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NewNotes n=notesList.get(position);

        holder.name.setText(n.getName());
        holder.dateTime.setText(n.getDateTime());
        String input=n.getDescription();
        if (input.length() > 80) {
            holder.description.setText(input.substring(0,80)+"...");
        }
        else {
            holder.description.setText(input);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
