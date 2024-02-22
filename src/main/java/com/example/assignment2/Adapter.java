package com.example.assignment2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    Context context;
    private ArrayList<Note> notes;

    OnItemClickListener onItemClickListener;


    public interface  OnItemClickListener{
        void onClick(Note note);


    }

    public void setOnitemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public Adapter(Context context, ArrayList<Note> notes) {
            this.context = context;
            this.notes = notes;


    }


    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.rvadapter,parent,false);

       return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
              holder.title.setText(notes.get(position).getTitle());
              holder.notess.setText(notes.get(position).getNote());
              holder.date.setText(notes.get(position).getDate());
              holder.time.setText(notes.get(position).getTime());
              holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(notes.get(position)));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public static class MyViewHolder extends  RecyclerView.ViewHolder{


        TextView title,notess, date ,time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            notess = itemView.findViewById(R.id.noteTxt);
            date = itemView.findViewById(R.id.DateTxt);
            time = itemView.findViewById(R.id.TimeTxt);

        }
    }







}
