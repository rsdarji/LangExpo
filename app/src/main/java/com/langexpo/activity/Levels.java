package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.langexpo.R;

public class Levels extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
    }
    public void back(View view)
    {
        Intent intent=new Intent (this,DailyGoal.class);
        startActivity(intent);
    }

}
