package com.bazhanpavel.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (TextView) findViewById(R.id.startButton);

        View.OnClickListener oclStartButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Difficult.class);
                startActivity(intent);
            }
        };

        startButton.setOnClickListener(oclStartButton);
    }
}
