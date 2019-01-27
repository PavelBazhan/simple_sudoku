package com.bazhanpavel.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Difficult extends AppCompatActivity {

    View easyButton;
    View hardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficult);

        easyButton = findViewById(R.id.easyButton);
        View.OnClickListener easyModeStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Difficult.this, GameActivity.class);
                intent.putExtra("DIFF", "easy");
                startActivity(intent);
            }
        };
        easyButton.setOnClickListener(easyModeStart);

        hardButton = findViewById(R.id.hardButton);
        View.OnClickListener hardModeStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Difficult.this, GameActivity.class);
                intent.putExtra("DIFF", "hard");
                startActivity(intent);
            }
        };
        hardButton.setOnClickListener(hardModeStart);
    }
}
