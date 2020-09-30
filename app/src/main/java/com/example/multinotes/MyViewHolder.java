package com.example.multinotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView dateTime;
    TextView description;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.titl);
        dateTime=itemView.findViewById(R.id.dateTime);
        description=itemView.findViewById(R.id.desc);
    }
}
