package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.admin.activity.Home;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ForgotPassword extends AppCompatActivity {
    EditText email;
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



    }
    public void reset_password(View view){
        String forgot_user_email=email.getText().toString();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void verifyEmail(View view) {
        if(email.getText().toString().equalsIgnoreCase("")){
            LangExpoAlertDialog langExpoAlertDialog =
                    new LangExpoAlertDialog(ForgotPassword.this, ForgotPassword.this);
            langExpoAlertDialog.alertDialog("Error", "Please enter your email address.");
            email.requestFocus();
            return;
        }
        new VerifyEmailForgotPasswordAsyncTask(ForgotPassword.this, email.getText().toString()).execute();
    }


    // start feedback assync task
    private class VerifyEmailForgotPasswordAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String email;

        public VerifyEmailForgotPasswordAsyncTask(Activity activity, String email){
            progressBar = new ProgressDialog(activity);
            this.email = email;

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

            String methodName = "verifyEmailForgotPassword";
            stringBuilder.append(Constant.PROTOCOL);
            stringBuilder.append(Constant.COLON);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.WEB_SERVICE_HOST);
            stringBuilder.append(Constant.COLON);
            stringBuilder.append(Constant.WEB_SERVICE_PORT);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.CONTEXT_PATH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.APPLICATION_PATH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.CLASS_PATH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(methodName);

            try {

                String urlParameters = "email="+email;

                byte[] postData       = urlParameters.getBytes();
                int    postDataLength = postData.length;
                url = new URL(stringBuilder.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                // uncomment this if you want to write output to this url
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects( false );
                connection.setRequestProperty( "charset", "utf-8");
                connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setUseCaches( false );
                // give it 15 seconds to respond
                connection.setReadTimeout(45*1000);

                try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                    wr.write( postData );
                }

                connection.connect();

                // read the output from the server
                stringBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println("response: "+stringBuilder.toString());
            }
            catch (Exception e) {
                if(e instanceof ConnectException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(ForgotPassword.this, ForgotPassword.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(ForgotPassword.this, ForgotPassword.this);
                    alertDialog.alertDialog("Time out", "Please try again.");
                }

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
            System.out.println("addUpdateFeedback: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    /*Toast toast = Toast
                            .makeText(ForgotPassword.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);*/
                    Intent intent =
                            new Intent(ForgotPassword.this, SetNewPassword.class);
                    intent.putExtra(Constant.User.EMAIL,loginResponse.getString("email"));
                    startActivity(intent);
                    progressBar.dismiss();
                    /*toast.show();*/
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast
                            .makeText(ForgotPassword.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
