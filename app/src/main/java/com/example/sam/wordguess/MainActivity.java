package com.example.sam.wordguess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView, textView2, next1, next2;
    private EditText input;
    private Button predict;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private List<String> trigramList;
    private List<String> freqList;
    private List<String> probabilityList;

    private String currentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        next1 = findViewById(R.id.next1);
        next2 = findViewById(R.id.next2);
        input = findViewById(R.id.input);
        predict = findViewById(R.id.predict);

        trigramList = new ArrayList<String>();
        freqList = new ArrayList<String>();
        probabilityList = new ArrayList<String>();

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentText = input.getText().toString();
                for (String trigram: trigramList) {
                    if (trigram.contains(currentText) && trigram.substring(0, trigram.indexOf(" ")).equals(currentText)) {
                        next1.setText(trigram.substring(trigram.indexOf(" ")+1, trigram.indexOf(" ", trigram.indexOf(" ")+1)));
                        next2.setText(trigram.substring(trigram.indexOf(" ", trigram.indexOf(" ")+1), trigram.length()));
                    }
                }
            }
        });

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.child("0").child("data").getChildren()) {
                    String trigram = snapshot.child("Field1").getValue() + "";
                    String freq = snapshot.child("Field3").getValue() + "";
                    String probability = snapshot.child("Field5").getValue() + "";

                    trigramList.add(trigram);
                    freqList.add(freq);
                    probabilityList.add(probability);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
