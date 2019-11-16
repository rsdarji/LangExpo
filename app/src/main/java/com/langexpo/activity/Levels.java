package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.model.Level;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;

import java.util.ArrayList;

public class Levels extends AppCompatActivity {

    RadioGroup rg;
    RadioButton selectedRadioButton;
    int selectedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);


        if(!Session.get(Constant.Session.USER_SELECTED_LEVEL).equalsIgnoreCase("")){
            selectedId = Integer.parseInt(Session.get(Constant.Session.USER_SELECTED_LEVEL));
            ((RadioButton)findViewById(selectedId)).setChecked(true);
        }

    }


    public void back(View view)
    {
        Intent intent=new Intent (this,DailyGoal.class);
        startActivity(intent);
    }
    public void levelContinue(View view)
    {
        rg = (RadioGroup) findViewById(R.id.select_level_radio_button_group);
        if(rg.getCheckedRadioButtonId()==-1)
        {
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Levels.this, Levels.this);
            alertDialog.alertDialog("Error", "Please select your level.");
            return;
        }
        else
        {
            selectedId = rg.getCheckedRadioButtonId();
            selectedRadioButton = (RadioButton)findViewById(selectedId);
            Session.set(Constant.Session.USER_SELECTED_LEVEL,String.valueOf(selectedId));
            //Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
        }
        Intent intent=new Intent (this,CreateProfile.class);
        startActivity(intent);
    }
}
