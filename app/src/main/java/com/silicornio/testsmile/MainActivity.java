package com.silicornio.testsmile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeterSmileRate psr = findViewById(R.id.psr);
        psr.setListener(new PeterSmileRate.PeterSmileRatingListener() {
            @Override
            public void onRatingChange(PeterSmileRate peterSmileRating, int rate) {
                Log.d("SMILE", "Rating: " + rate);
            }
        });
    }
}
