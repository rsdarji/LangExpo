package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.admin.activity.AddLanguage;
import com.langexpo.admin.activity.Home;
import com.langexpo.admin.activity.LanguageList;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;
import com.langexpo.utility.UploadImageToCloud;

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

public class Feedback extends AppCompatActivity {
    Toolbar myToolbar;
    private RatingBar ratingBar;

    private TextView tvRateCount,tvRateMessage;
    private EditText name,email,yr_msg, phoneET;
    String user_name,user_email,user_msg,user_rating;
    long phone;
    private float ratedValue,user_rating_value;
    long userId = Long.parseLong(Session.get(Constant.User.USER_ID));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        myToolbar = (Toolbar) findViewById(R.id.feedback_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        name =(EditText)findViewById(R.id.editText_name);
        email =(EditText)findViewById(R.id.editText_email_feedback);
        yr_msg =(EditText)findViewById(R.id.editText_message);
        phoneET = (EditText)findViewById(R.id.editText_phone);

        tvRateCount = (TextView) findViewById(R.id.textView_yr_ratings);

        tvRateMessage = (TextView) findViewById(R.id.textView_rating_msg);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override

            public void onRatingChanged(RatingBar ratingBar, float rating,

                                        boolean fromUser) {

                ratedValue = ratingBar.getRating();

                tvRateCount.setText("Your Rating : "

                        + ratedValue + "/5.");

                if(ratedValue<1){

                    tvRateMessage.setText("ohh ho...");

                }else if(ratedValue<2){

                    tvRateMessage.setText("Ok.");

                }else if(ratedValue<3){

                    tvRateMessage.setText("Not bad.");

                }else if(ratedValue<4){

                    tvRateMessage.setText("Nice");

                }else if(ratedValue<5){

                    tvRateMessage.setText("Very Nice");

                }else if(ratedValue==5){

                    tvRateMessage.setText("Excellent..!!!");

                }

            }

        });


    }

    public void submit(View view){
        user_name=name.getText().toString();
        user_email=email.getText().toString();
        user_msg=yr_msg.getText().toString();
        user_rating_value=ratingBar.getRating();
        String.valueOf(user_rating_value);

        if(user_name.equalsIgnoreCase("")){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Feedback.this, Feedback.this);
            alertDialog.alertDialog("Error", "Please enter name.");
            name.requestFocus();
            return;
        }
        if(user_email.equalsIgnoreCase("") || !user_email.contains("@")){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Feedback.this, Feedback.this);
            alertDialog.alertDialog("Error", "Please enter correct email.");
            email.requestFocus();
            return;
        }
        if(phoneET.getText().toString().equalsIgnoreCase("") ||
            phoneET.getText().length()<10){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Feedback.this, Feedback.this);
            alertDialog.alertDialog("Error", "Phone number should not empty or less than 10 digit.");
            email.requestFocus();
            return;
        }
        if(user_msg.equalsIgnoreCase("")){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Feedback.this, Feedback.this);
            alertDialog.alertDialog("Error", "Please enter message.");
            yr_msg.requestFocus();
            return;
        }
        if(ratedValue==0){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Feedback.this, Feedback.this);
            alertDialog.alertDialog("Error", "Please select rating.");
            ratingBar.requestFocus();
            return;
        }

        new FeedbackAsyncTask(Feedback.this, userId,
                String.valueOf(ratedValue), user_msg).execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    // start feedback assync task
    private class FeedbackAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long userId;
        private String rating;
        private String msg;

        public FeedbackAsyncTask(Activity activity,long userId,String rating,String msg){
            progressBar = new ProgressDialog(activity);
            this.userId = userId;
            this.rating=rating;
            this.msg=msg;
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

            String methodName = "addUpdateFeedback";
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

                String urlParameters = "userId="+userId+"&rating="+rating+"&message="+msg;

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
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Feedback.this, Feedback.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Feedback.this, Feedback.this);
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

                    Toast toast = Toast.makeText(Feedback.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(Feedback.this, Home.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast.makeText(Feedback.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // end feedback assync task
}
