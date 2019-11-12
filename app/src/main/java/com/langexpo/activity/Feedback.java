package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.langexpo.R;

public class Feedback extends AppCompatActivity {
    Toolbar myToolbar;
    private RatingBar ratingBar;

    private TextView tvRateCount,tvRateMessage;
    private EditText name,email,yr_msg;

    private float ratedValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        myToolbar = (Toolbar) findViewById(R.id.feedback_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        name =(EditText)findViewById(R.id.editText_name);
        email =(EditText)findViewById(R.id.editText_email_feedback);
        yr_msg =(EditText)findViewById(R.id.editText_message);

        tvRateCount = (TextView) findViewById(R.id.textView_yr_ratings);

        tvRateMessage = (TextView) findViewById(R.id.textView_rating_msg);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override

            public void onRatingChanged(RatingBar ratingBar, float rating,

                                        boolean fromUser) {

                ratedValue = ratingBar.getRating();

                tvRateCount.setText("Your Rating : "

                        + ratedValue + "/5.");

                if(ratedValue<1){

                    tvRateMessage.setText("ohh ho...");

                }else if(ratedValue<2){

                    tvRateMessage.setText("Ok.");

                }else if(ratedValue<3){

                    tvRateMessage.setText("Not bad.");

                }else if(ratedValue<4){

                    tvRateMessage.setText("Nice");

                }else if(ratedValue<5){

                    tvRateMessage.setText("Very Nice");

                }else if(ratedValue==5){

                    tvRateMessage.setText("Thank you..!!!");

                }

            }

        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
