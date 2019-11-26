package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.langexpo.R;
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

public class SetNewPassword extends AppCompatActivity {

    Toolbar myToolbar;
    EditText passwordET, confirmPasswordET;
    Button updatePasswordBT;
    String email, password, confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        myToolbar = (Toolbar) findViewById(R.id.set_new_password_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        passwordET = (EditText) findViewById(R.id.edittext_password);
        confirmPasswordET = (EditText) findViewById(R.id.edittext_confirm_password);
        updatePasswordBT = (Button) findViewById(R.id.button_update_password);
        getIncomingIntent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra(Constant.User.EMAIL)){
            email = getIntent().getStringExtra(Constant.User.EMAIL);
        }
    }

    public void updatePassword(View view) {
        password = passwordET.getText().toString();
        confirmPassword = confirmPasswordET.getText().toString();

        if(password.equalsIgnoreCase("")){
            LangExpoAlertDialog langExpoAlertDialog =
                    new LangExpoAlertDialog(SetNewPassword.this, SetNewPassword.this);
            langExpoAlertDialog.alertDialog("Error", "Please enter password.");
            passwordET.requestFocus();
            return;
        }
        if(confirmPassword.equalsIgnoreCase("")){
            LangExpoAlertDialog langExpoAlertDialog =
                    new LangExpoAlertDialog(SetNewPassword.this, SetNewPassword.this);
            langExpoAlertDialog.alertDialog("Error", "Please enter confirm password.");
            confirmPasswordET.requestFocus();
            return;
        }
        if(!password.equalsIgnoreCase(confirmPassword)){
            LangExpoAlertDialog langExpoAlertDialog =
                    new LangExpoAlertDialog(SetNewPassword.this, SetNewPassword.this);
            langExpoAlertDialog.alertDialog("Error", "Password and Confirm password should be same.");
            passwordET.setText("");
            confirmPasswordET.setText("");
            passwordET.requestFocus();
            return;
        }

        new UpdatePasswordAsyncTask(SetNewPassword.this, email, password).execute();

    }
    // start feedback assync task
    private class UpdatePasswordAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String email;
        private String password;

        public UpdatePasswordAsyncTask(Activity activity, String email, String password){
            progressBar = new ProgressDialog(activity);
            this.email = email;
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

            String methodName = "updatePassword";
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

                String urlParameters = "email="+email+"&password="+password;

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
                            new LangExpoAlertDialog(SetNewPassword.this, SetNewPassword.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(SetNewPassword.this, SetNewPassword.this);
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
            System.out.println("updatePassword: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    Toast toast = Toast
                            .makeText(SetNewPassword.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent =
                            new Intent(SetNewPassword.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast
                            .makeText(SetNewPassword.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
