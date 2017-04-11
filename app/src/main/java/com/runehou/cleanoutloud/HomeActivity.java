package com.runehou.cleanoutloud;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button eventsBtn, gamesBtn, mapBtn, topScoreBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        eventsBtn = (Button) findViewById(R.id.eventsBtn);
        eventsBtn.setOnClickListener(this);
        eventsBtn.setText("Events");
        mapBtn = (Button) findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(this);
        mapBtn.setText("Kort");

        gamesBtn = (Button) findViewById(R.id.recycleBtn);
        gamesBtn.setOnClickListener(this);
        gamesBtn.setText("Quiz");

        topScoreBtn = (Button) findViewById(R.id.topScoreBtn);
        topScoreBtn.setText("Top scorer");
        topScoreBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == eventsBtn) {
            startActivity(new Intent(getApplicationContext(), Events.class));
        }
        if (view == mapBtn) {
            startActivity(new Intent(getApplicationContext(), Map.class));
        }
        if (view == gamesBtn) {
            startActivity(new Intent(getApplicationContext(), QuizActivity.class));
        }
        if (view == topScoreBtn) {
            startActivity(new Intent(getApplicationContext(), RecyclingListActivity.class));
        }
    }

    public void playSound(Context context, int soundID) {
        MediaPlayer mp = MediaPlayer.create(context, soundID);
        mp.start();
    }
}
