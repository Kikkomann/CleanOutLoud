package com.runehou.cleanoutloud;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuizActivity extends Activity implements View.OnClickListener {
    private TextView questionTxt, choiceOneTxt, choiceTwoTxt, choiceThreeTxt, choiceFourTxt, correctMessageTxt, funFactTxt;
    private TextView choices[] = {choiceOneTxt, choiceTwoTxt, choiceThreeTxt, choiceFourTxt};
    private Button answerBtn;
    private Drawable borderOne, borderTwo, borderThree, borderFour;
    private RadioButton choice1, choice2, choice3, choice4;
    private RadioGroup radioGroup;

    boolean out = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTxt = (TextView) findViewById(R.id.question);
        questionTxt.setText("Hvor mange tons co2 kan man spare på at slukke lyset i køkkenet?");
        choiceOneTxt = (TextView) findViewById(R.id.choiceOne);
        choiceOneTxt.setText("10 ton");
        choiceOneTxt.setOnClickListener(this);
        choiceTwoTxt = (TextView) findViewById(R.id.choiceTwo);
        choiceTwoTxt.setText("20 ton");
        choiceTwoTxt.setOnClickListener(this);
        choiceThreeTxt = (TextView) findViewById(R.id.choiceThree);
        choiceThreeTxt.setText("30 ton");
        choiceThreeTxt.setOnClickListener(this);
        choiceFourTxt = (TextView) findViewById(R.id.choiceFour);
        choiceFourTxt.setText("40 ton");
        choiceFourTxt.setOnClickListener(this);
        correctMessageTxt = (TextView) findViewById(R.id.correctMessage);
        funFactTxt = (TextView) findViewById(R.id.funFact);

        borderOne = choiceOneTxt.getBackground();
        borderTwo = choiceTwoTxt.getBackground();
        borderThree = choiceThreeTxt.getBackground();
        borderFour = choiceFourTxt.getBackground();

        choice1 = (RadioButton) findViewById(R.id.choice1);
        choice2 = (RadioButton) findViewById(R.id.choice2);
        choice3 = (RadioButton) findViewById(R.id.choice3);
        choice4 = (RadioButton) findViewById(R.id.choice4);



        answerBtn = (Button) findViewById(R.id.answer);
        answerBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == choiceOneTxt) {
            choiceOneTxt.setSelected(true);
            choiceTwoTxt.setSelected(false);
            choiceThreeTxt.setSelected(false);
            choiceFourTxt.setSelected(false);
        }
        if (view == choiceTwoTxt) {
            choiceOneTxt.setSelected(false);
            choiceTwoTxt.setSelected(true);
            choiceThreeTxt.setSelected(false);
            choiceFourTxt.setSelected(false);
        }
        if (view == choiceThreeTxt) {
            choiceOneTxt.setSelected(false);
            choiceTwoTxt.setSelected(false);
            choiceThreeTxt.setSelected(true);
            choiceFourTxt.setSelected(false);
        }
        if (view == choiceFourTxt) {
            choiceOneTxt.setSelected(false);
            choiceTwoTxt.setSelected(false);
            choiceThreeTxt.setSelected(false);
            choiceFourTxt.setSelected(true);
        }

        borderOne.setColorFilter(Color.WHITE, PorterDuff.Mode.CLEAR);
        borderTwo.setColorFilter(Color.WHITE, PorterDuff.Mode.CLEAR);
        borderThree.setColorFilter(Color.WHITE, PorterDuff.Mode.CLEAR);
        borderFour.setColorFilter(Color.WHITE, PorterDuff.Mode.CLEAR);
        if (choiceTwoTxt.isSelected()) {
            choiceTwoTxt.setSelected(false);
            borderTwo.setColorFilter(Color.WHITE, PorterDuff.Mode.CLEAR);
        }
        if (choiceThreeTxt.isSelected()) {
            choiceThreeTxt.setSelected(false);
            borderThree.setColorFilter(Color.WHITE, PorterDuff.Mode.CLEAR);
        }
        if (choiceFourTxt.isSelected()) {
            choiceFourTxt.setSelected(false);
            borderFour.setColorFilter(Color.WHITE, PorterDuff.Mode.CLEAR);
        }


        if (view == questionTxt) {


        }
        if (view == answerBtn) {
            if (!out) {
                if (choiceTwoTxt.isSelected()) {
                    correctMessageTxt.setText("Det er rigtigt!!");
                } else {
                    correctMessageTxt.setText("Du har ikke valgt det rigtige svar\nDet rigtige svar er 20 ton.");
                }
                funFactTxt.setText("Fun fact:\nVidste du at Roskilde Festival brugte 8 mio. kr. på at rydde op sidste år?");
                choiceOneTxt.setOnClickListener(null);
                choiceTwoTxt.setOnClickListener(null);
                choiceThreeTxt.setOnClickListener(null);
                choiceFourTxt.setOnClickListener(null);
                answerBtn.setText("Tilbage");
                out = true;
            } else {
                finish();
            }

        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

        ((RadioButton) view).toggle();




    }

}
