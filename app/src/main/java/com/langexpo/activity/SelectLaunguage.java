package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.langexpo.R;

public class SelectLaunguage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_launguage);

    }

    public void continue1(View view) {
        Intent intent= new Intent(this,DailyGoal.class);
        startActivity(intent);

    }

   }
