package com.yellow5a5.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yellow5a5.crashanalysis.activity.UmbrellaCorporationActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                throw new NumberFormatException();
                Intent intent = new Intent(SecondActivity.this, UmbrellaCorporationActivity.class);
                startActivity(intent);
            }
        });
    }
}
