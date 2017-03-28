package com.runehou.cleanoutloud;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RecycleInfoActivity extends Activity {

    private String description;
    private TextView funFactTxt, numberTxt, descriptionTxt, titleTxt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_info_fragment);

        String infoName = getIntent().getStringExtra("NAME");

        funFactTxt = (TextView) findViewById(R.id.funFact);
        titleTxt = (TextView) findViewById(R.id.title);
        numberTxt = (TextView) findViewById(R.id.number);
        descriptionTxt = (TextView) findViewById(R.id.description);

        if (infoName.equals("Tent")) {
            titleTxt.setText("Tent");
            descriptionTxt.setText("\nEt telt er godt at sove i.\n\n");
            funFactTxt.setText("Når du donerer dit telt bliver det sendt til Burkino Fasso.\n\n");
            numberTxt.setText("Sidste år donerede Roskilde Festival 10.000 telte.");
        } else if (infoName.equals("Pavilion")) {
            titleTxt.setText("Pavilion");
            descriptionTxt.setText("\nEn pavilion fungerer som naturlig solcreme.\n\n");
            funFactTxt.setText("Der er i gennemsnit 1.000 pavilioner på Roskilde Festival hvert år.\n\n");
            numberTxt.setText("Sidste år donerede Roskilde Festival 500 pavilioner");
        }


    }
}
