package com.example.delicious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ListDish extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dish);
        Intent intent = getIntent();
        Toast.makeText(this, ""+intent.getStringExtra("dish"), Toast.LENGTH_SHORT).show();
    }
}