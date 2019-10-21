    package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.langexpo.R;

    public class MainActivity extends AppCompatActivity {
    EditText uname,pass;
    Button log,new_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname=(EditText)findViewById(R.id.edittext_username);
        pass=(EditText)findViewById(R.id.edittext_password);
        log=(Button) findViewById(R.id.button_login);
        new_user=(Button) findViewById(R.id.button_newuser);
        pass.setEnabled(false);
        log.setEnabled(false);
        uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    pass.setEnabled(false);
                }else{
                    pass.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    log.setEnabled(false);
                }else{
                    log.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

        public void newuser(View view) {
            Intent intent= new Intent(this,SelectLaunguage.class);
            startActivity(intent);

        }

        public void forgot_password(View view) {
            Intent intent= new Intent(this,ForgotPassword.class);
            startActivity(intent);


        }
    }
