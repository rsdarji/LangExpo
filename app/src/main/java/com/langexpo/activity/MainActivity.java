    package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.admin.activity.Home;
import com.langexpo.utility.Constant;
import com.langexpo.utility.Session;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

    public class MainActivity extends AppCompatActivity {
    EditText uname,pass;
    Button login,new_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname=(EditText)findViewById(R.id.edittext_username);
        pass=(EditText)findViewById(R.id.edittext_password);
        login=(Button) findViewById(R.id.button_login);
        new_user=(Button) findViewById(R.id.button_newuser);
        pass.setEnabled(false);
        login.setEnabled(false);
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
                    login.setEnabled(false);
                }else{
                    login.setEnabled(true);
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

        public void login(View view) {
            String username = uname.getText().toString();
            String password = pass.getText().toString();
            Toast toast;
            if(username.isEmpty()| password.isEmpty()){
                if(username.isEmpty()){
                    uname.requestFocus();
                    uname.setText("");
                    toast = Toast.makeText(getApplicationContext(), "Please enter correct Username", Toast.LENGTH_LONG);
                }else{
                    pass.requestFocus();
                    pass.setText("");
                    toast = Toast.makeText(getApplicationContext(), "Password cannot be empty or contains only whitespace(s)", Toast.LENGTH_LONG);
                }
                toast.show();
                return;
            }

            //new Login(MainActivity.this, username, password).execute();

            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);

            /*Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);*/

        }


        private class Login extends AsyncTask<Void, Void, String> {
            private ProgressDialog progressBar;
            private String username;
            private String password;

            public Login(MainActivity activity, String username, String password){
                progressBar = new ProgressDialog(activity);
                this.username = username;
                this.password = password;
            }

            protected void onPreExecute(){
                progressBar.setMessage("Loading...");
                progressBar.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                URL url = null;
                BufferedReader reader = null;
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    url = new URL("http://"+ Constant.WEB_SERVICE_HOST +":"+Constant.WEB_SERVICE_PORT+"/WebApplication1/webresources/mobile/login&"+username+"&"+password);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    // uncomment this if you want to write output to this url

                    connection.setDoInput(true);
                    connection.setInstanceFollowRedirects( false );

                    // give it 15 seconds to respond
                    connection.setReadTimeout(15*1000);
                    connection.connect();
                    // read the output from the server
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    System.out.println("url: "+stringBuilder.toString());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    try {
                        throw e;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                finally {
                    if (reader != null) {
                        try{
                            reader.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return stringBuilder.toString();
            }
            @Override

            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                System.out.println(result);
                try {
                    JSONObject loginResponse = new JSONObject(result);
                    if(loginResponse.length()!=0 &&
                            loginResponse.get("status").toString().equalsIgnoreCase("ok") &&
                            !loginResponse.get("user_id").toString().equalsIgnoreCase("")) {
                        Session session = new Session(getApplicationContext());
                        session.set("user_id",loginResponse.get("user_id").toString());
                        session.set("email",loginResponse.get("email").toString());
                        session.set("first_name",loginResponse.get("first_name").toString());
                        session.set("last_name",loginResponse.get("last_name").toString());


                        /*TextView t = (TextView) findViewById(R.id.nav_user_name);
                        TextView email = (TextView) findViewById(R.id.nav_user_email);
                        t.setText(loginResponse.get("first_name").toString()+ " "+loginResponse.get("last_name").toString());
                        email.setText(loginResponse.get("email").toString());*/

                        Log.d("Userid: ", session.get("user_id"));
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
                        progressBar.dismiss();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),"Please enter correct credentials",Toast.LENGTH_LONG);
                        uname.setText("");
                        uname.requestFocus();
                        pass.setText("");
                        progressBar.dismiss();
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
