package com.runehou.cleanoutloud;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button eventsBtn, gamesBtn, mapBtn, topScoreBtn, logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String token = getIntent().getStringExtra("TOKEN");
        Log.d("Token", token);

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

        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logoutBtn.setText("Log ud");
        logoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == eventsBtn) {
            startActivity(new Intent(getApplicationContext(), Events.class));
        } else if (view == mapBtn) {
            startActivity(new Intent(getApplicationContext(), Map.class));
        } else if (view == gamesBtn) {
            startActivity(new Intent(getApplicationContext(), QuizActivity.class));
        } else if (view == topScoreBtn) {
            startActivity(new Intent(getApplicationContext(), RecyclingListActivity.class));
        } else if (view == logoutBtn) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
