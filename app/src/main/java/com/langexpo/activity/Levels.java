package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.langexpo.R;

import java.util.ArrayList;

public class Levels extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        getIncomingIntent();
    }
    public void back(View view)
    {
        Intent intent=new Intent (this,DailyGoal.class);
        startActivity(intent);
    }
    public void register(View view)
    {
        Intent intent=new Intent (this,CreateProfile.class);
        startActivity(intent);
    }

    private void getIncomingIntent() {
        /*if (getIntent().hasExtra("userSelectedGoals")) {
            ArrayList<String> test = getIntent().getStringArrayListExtra("userSelectedGoals");
            for(String s : test){
                System.out.println("s : "+s);
            }
        }*/
    }

}
