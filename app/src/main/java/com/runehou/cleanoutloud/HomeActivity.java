package com.runehou.cleanoutloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button eventsBtn, gamesBtn, mapBtn, topScoreBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        eventsBtn = (Button) findViewById(R.id.eventsBtn);
        eventsBtn.setOnClickListener(this);
        mapBtn = (Button) findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(this);
        mapBtn.setText("Kort (ikke impl)");

        gamesBtn = (Button) findViewById(R.id.recycleBtn);
        gamesBtn.setOnClickListener(this);
        gamesBtn.setText("Quiz (ikke impl)");

        topScoreBtn = (Button) findViewById(R.id.topScoreBtn);
        topScoreBtn.setOnClickListener(this);

        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == eventsBtn) {
            startActivity(new Intent(getApplicationContext(), WallActivity.class));
        } else if (view == mapBtn) {
            startActivity(new Intent(getApplicationContext(), Map.class));
        } else if (view == gamesBtn) {
            startActivity(new Intent(getApplicationContext(), QuizActivity.class));
        } else if (view == topScoreBtn) {
            startActivity(new Intent(getApplicationContext(), GarbageActivity.class));
        } else if (view == logoutBtn) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}