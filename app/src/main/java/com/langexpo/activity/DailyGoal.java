package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.langexpo.R;


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
    public void back(View view)
    {
        Intent intent=new Intent (this,SelectLaunguage.class);
        startActivity(intent);
    }


}
