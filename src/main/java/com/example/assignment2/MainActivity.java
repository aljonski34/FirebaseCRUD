package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    DatabaseReference databaseReference;
    FloatingActionButton button;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference("notes");
        button = findViewById(R.id.floatingActionButton);

        recyclerView = findViewById(R.id.RView);


        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Note> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    Objects.requireNonNull(note).setKey(dataSnapshot.getKey());
                    arrayList.add(note);

                }
                if (arrayList.isEmpty()) {

                    recyclerView.setVisibility(View.GONE);
                } else {


                    recyclerView.setVisibility(View.VISIBLE);


                }
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                Adapter adapter = new Adapter(MainActivity.this, arrayList);
                recyclerView.setAdapter(adapter);

                adapter.setOnitemClickListener(new Adapter.OnItemClickListener() {
                    @Override
                    public void onClick(Note note) {


                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,null);
                        dialog.setView(view);




                        TextView date,time;

                        TextInputEditText title,notes;

                        title = view.findViewById(R.id.ETtitle);
                        notes = view.findViewById(R.id.ETNotes);
                        date = view.findViewById(R.id.setDate);
                        time = view.findViewById(R.id.setTime);


                        date.setOnClickListener(new View.OnClickListener() {

                            Calendar calendar = Calendar.getInstance();


                            @Override
                            public void onClick(View view) {
                                int years = calendar.get(Calendar.YEAR);
                                int months = calendar.get(Calendar.MONTH);
                                int  days  = calendar.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog dialog1 = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                if(years < year || months < month || days < day){
                                                    date.setText(year + "/" + (month + 1) + "/" +day );
                                                }else{

                                                    Toast.makeText(MainActivity.this, "NO You can set date in the past ", Toast.LENGTH_SHORT).show();

                                                }
                                    }
                                },years,months,days);
                                dialog1.show();

                            

                            }
                        });

                        title.setText(note.getTitle());
                        notes.setText(note.getNote());
                        date.setText(note.getDate());
                        time.setText(note.getTime());
                        time.setOnClickListener(new View.OnClickListener() {
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int mins = calendar.get(Calendar.MINUTE);
                            @Override
                            public void onClick(View view) {

                                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hours, int min) {
                                        time.setText(String.valueOf(hours) + ":"+ String.valueOf(min));
                                    }
                                },hour,mins,true);
                                timePickerDialog.show();
                            }
                        });


                        dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Note note1 = new Note();
                                note1.setTitle(title.getText().toString());
                                note1.setNote(notes.getText().toString());
                                note1.setDate(date.getText().toString());
                                note1.setTime(time.getText().toString());


                                databaseReference.child(note.getKey()).setValue(note1);

                            }
                        });
                        dialog.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseReference.child(note.getKey()).removeValue();
                            }
                        });

                        dialog.show();
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "DATABASE ERROR ", Toast.LENGTH_SHORT).show();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog();

            }
        });


    }

    private void addDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null, false);

        dialog.setTitle("Add Record");
        dialog.setView(view);

        TextInputEditText titletxt, notetxt;
        TextView date, time;

        titletxt = view.findViewById(R.id.ETtitle);
        notetxt = view.findViewById(R.id.ETNotes);

        date = view.findViewById(R.id.setDate);
        time = view.findViewById(R.id.setTime);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                int days = calendar.get(Calendar.DAY_OF_MONTH);
                int months = calendar.get(Calendar.MONTH);
                int years = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        if (years < year || months < month || days < day ) {
                            date.setText(year + "/" + (month + 1) + "/" + day);
                        } else {
                            Toast.makeText(MainActivity.this, "You cant set date in the past ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, years, months, days);
                datePickerDialog.show();
            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int mins = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {




                        time.setText(String.valueOf(hour)+  ":" + String.valueOf(min));


                    }
                }, hours, mins, true);
                timePickerDialog.show();
            }
        });


        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String key = databaseReference.push().getKey();

                String title = titletxt.getText().toString();
                String notes = notetxt.getText().toString();
                String stime = time.getText().toString();
                String sDate = date.getText().toString();


                Note note = new Note();
                note.setTitle(title);
                note.setNote(notes);
                note.setTime(stime);
                note.setDate(sDate);
                databaseReference.child(key).setValue(note);


            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();


    }



}
