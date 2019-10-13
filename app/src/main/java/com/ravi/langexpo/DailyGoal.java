package com.ravi.langexpo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DailyGoal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_goal);
        }
    public void continue2(View view) {
        Intent intent=new Intent(this,Levels.class);
        startActivity(intent);

    }

}
