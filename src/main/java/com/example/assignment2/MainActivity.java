package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


        Readnotes();


        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
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
                        if (years > year || months > month || days > day) {
                            Toast.makeText(MainActivity.this, "Dont set a schedule to the past ", Toast.LENGTH_SHORT).show();
                        } else {
                            date.setText(year + "/" + month + "/" + day);
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

                        String AMPM;
                        if (hour == 12) {
                            AMPM = "PM";
                        } else {
                            AMPM = "AM";
                        }
                        int hourday = hours % 12;
                        if (hourday == 12) {
                            hour = 0;
                        }
                        time.setText(hour + ":" + min + AMPM);
                    }
                }, hours, mins, false);
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

    private void Readnotes() {
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

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "DATABASE ERROR ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {



        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        CharSequence [] charSequence = {"Edit","Delete"};
        View view = LayoutInflater.from(this).inflate(R.layout.dialog, null, false);

        dialog.setView(view);

        dialog.setItems(charSequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
        dialog.show();








    }
}