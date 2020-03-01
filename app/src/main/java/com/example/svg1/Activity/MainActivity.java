package com.example.svg1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.svg1.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button graphbutton;
    private Button mapbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphbutton = findViewById(R.id.graph);
        mapbutton = findViewById(R.id.map);
        graphbutton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.graph:
                Intent intent=new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
                break;
            case R.id.map:

                break;
        }
    }
}
