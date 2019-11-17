package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.langexpo.R;

public class ForgotPassword extends AppCompatActivity {
    EditText email;
    Button fpass;
    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        myToolbar = (Toolbar) findViewById(R.id.forgot_password_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        email=(EditText)findViewById(R.id.edittext_email);
        fpass=(Button) findViewById(R.id.button_forget_password);


    }
    public void reset_password(View view){
        String forgot_user_email=email.getText().toString();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
