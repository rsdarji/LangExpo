package com.langexpo.admin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.langexpo.R;
import com.langexpo.utility.Constant;
import com.langexpo.utility.Session;
import com.langexpo.utility.UploadImageToCloud;
import com.langexpo.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddQuestionType extends AppCompatActivity {

    long questionTypeId;
    Toolbar myToolbar;
    Switch multipleAnswer, questionAudio, optionAudio, questionImage, optionImage;
    EditText questionTypeName, totalOptions, totalDisplayOptions;
    Button addQuestionTypeBT;
    String questionTypeNameValue;
    int totalOptionsValue, totalDisplayOptionsValue;
    boolean multipleAnswerValue, questionAudioValue, optionAudioValue, questionImageValue, optionImageValue, updateQuestionType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_type);

        myToolbar = (Toolbar) findViewById(R.id.admin_add_question_type_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addQuestionTypeBT = (Button) findViewById(R.id.button_add_question_type);
        questionTypeName = (EditText) findViewById(R.id.question_type_name);
        totalOptions = (EditText) findViewById(R.id.question_type_total_option_count);
        totalDisplayOptions = (EditText) findViewById(R.id.question_type_display_option_count);
        multipleAnswer = (Switch) findViewById(R.id.question_type_multiple_answer_switch);
        questionAudio = (Switch) findViewById(R.id.question_type_question_audio_switch);
        optionAudio = (Switch) findViewById(R.id.question_type_option_audio_switch);
        questionImage = (Switch) findViewById(R.id.question_type_question_image_switch);
        optionImage = (Switch) findViewById(R.id.question_type_option_image_switch);

        getIncomingIntent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("questionTypeId") &&
                getIntent().hasExtra("questionTypeName") &&
                getIntent().hasExtra("totalOptions") &&
                getIntent().hasExtra("totalDisplayOptions") &&
                getIntent().hasExtra("multipleAnswer") &&
                getIntent().hasExtra("questionAudio") &&
                getIntent().hasExtra("optionAudio") &&
                getIntent().hasExtra("questionImage") &&
                getIntent().hasExtra("optionImage")) {
            updateQuestionType = true;
            myToolbar.setTitle("Update Question Type");
            addQuestionTypeBT.setText("Update Question Type");
            questionTypeId = getIntent().getLongExtra("questionTypeId", 0);
            questionTypeNameValue = getIntent().getStringExtra("questionTypeName");
            totalOptionsValue = getIntent().getIntExtra("totalOptions", 0);
            totalDisplayOptionsValue = getIntent().getIntExtra("totalDisplayOptions", 0);
            multipleAnswerValue = getIntent().getBooleanExtra("multipleAnswer", false);
            questionAudioValue = getIntent().getBooleanExtra("questionAudio", false);
            optionAudioValue = getIntent().getBooleanExtra("optionAudio", false);
            questionImageValue = getIntent().getBooleanExtra("questionImage", false);
            optionImageValue = getIntent().getBooleanExtra("optionImage", false);

            setQuestionTypeDetail(questionTypeId, questionTypeNameValue, totalOptionsValue,
                    totalDisplayOptionsValue, multipleAnswerValue, questionAudioValue,
                    optionAudioValue, questionImageValue, optionImageValue);
        }
    }

    private void setQuestionTypeDetail(long questionTypeId, String questionTypeNameValue,
               int totalOptionsValue, int totalDisplayOptionsValue,
               boolean multipleAnswerValue, boolean questionAudioValue,
               boolean optionAudioValue, boolean questionImageValue,
               boolean optionImageValue){

        questionTypeName.setText(questionTypeNameValue);
        totalOptions.setText(String.valueOf(totalOptionsValue));
        totalDisplayOptions.setText(String.valueOf(totalDisplayOptionsValue));
        multipleAnswer.setChecked(multipleAnswerValue);
        questionAudio.setChecked(questionAudioValue);
        optionAudio.setChecked(optionAudioValue);
        questionImage.setChecked(questionImageValue);
        optionImage.setChecked(optionImageValue);

    }

    public void addQuestionType(View view) {


        questionTypeNameValue = questionTypeName.getText().toString();
        totalOptionsValue = Utility.getValue(totalOptions.getText().toString(),0);
        totalDisplayOptionsValue = Utility.getValue(totalDisplayOptions.getText().toString(),0);
        multipleAnswerValue = multipleAnswer.isChecked();
        questionAudioValue = questionAudio.isChecked();
        optionAudioValue = optionAudio.isChecked();
        questionImageValue = questionImage.isChecked();
        optionImageValue = optionImage.isChecked();
        //optionAudio.isChecked();
        //Toast.makeText(AddQuestionType.this,String.valueOf(optionAudio.isChecked()), Toast.LENGTH_SHORT).show();


        //Validation - start
        if (questionTypeNameValue.equalsIgnoreCase("")) {
            questionTypeName.setError("Please enter name");
            questionTypeName.requestFocus();
            return;
        }
        if (totalOptionsValue == 0) {
            totalOptions.setError("Please enter the option's count");
            totalOptions.requestFocus();
            return;
        }
        if (totalDisplayOptionsValue == 0) {
            totalDisplayOptions.setError("Please enter the display option's count");
            totalDisplayOptions.requestFocus();
            return;
        }
        //validation - end


        new AddQuestionTypeAsyncTask(AddQuestionType.this, questionTypeId,
                questionTypeNameValue, totalOptionsValue, totalDisplayOptionsValue,
                multipleAnswerValue, questionAudioValue, optionAudioValue,
                questionImageValue, optionImageValue).execute();
    }

    public void deleteQuestionTypeToDB(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setTitle("Delete Question Type?")
                .setMessage("Do you really want to delete this Question Type?")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Toast.makeText(AddLanguage.this, "Yaay", Toast.LENGTH_SHORT).show();
                        new DeleteQuestionTypeAsyncTask(AddQuestionType.this, questionTypeId).execute();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Toast.makeText(AddLanguage.this, "no", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }

    private class DeleteQuestionTypeAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long questionTypeId;

        public DeleteQuestionTypeAsyncTask(Activity activity, long questionTypeId){
            progressBar = new ProgressDialog(activity);
            this.questionTypeId = questionTypeId;
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

            String methodName = "deleteQuestionType";
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

                String urlParameters  = "questionTypeId="+questionTypeId;


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
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    Toast toast = Toast.makeText(AddQuestionType.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(AddQuestionType.this, QuestionTypeList.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast.makeText(AddQuestionType.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddQuestionTypeAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long questionTypeId;
        private String questionTypeNameValue;
        private int totalOptionsValue;
        private int totalDisplayOptionsValue;
        private boolean multipleAnswerValue;
        private boolean questionAudioValue;
        private boolean optionAudioValue;
        private boolean questionImageValue;
        private boolean optionImageValue;

        public AddQuestionTypeAsyncTask(Activity activity, long questionTypeId,
                String questionTypeNameValue, int totalOptionsValue, int totalDisplayOptionsValue,
                boolean multipleAnswerValue, boolean questionAudioValue, boolean optionAudioValue,
                boolean questionImageValue, boolean optionImageValue){
            progressBar = new ProgressDialog(activity);
            this.questionTypeId = questionTypeId;
            this.questionTypeNameValue = questionTypeNameValue;
            this.totalOptionsValue = totalOptionsValue;
            this.totalDisplayOptionsValue = totalDisplayOptionsValue;
            this.multipleAnswerValue = multipleAnswerValue;
            this.questionAudioValue = questionAudioValue;
            this.optionAudioValue = optionAudioValue;
            this.questionImageValue = questionImageValue;
            this.optionImageValue = optionImageValue;

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

            String methodName = "addUpdateQuestionType1";
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
                String urlParameters  = "questionTypeId="+questionTypeId+"&questionTypeNameValue="+questionTypeNameValue+
                        "&totalOptionsValue="+totalOptionsValue+"&totalDisplayOptionsValue="+totalDisplayOptionsValue+
                        "&multipleAnswerValue="+multipleAnswerValue+"&questionAudioValue="+questionAudioValue+
                        "&optionAudioValue="+optionAudioValue+"&questionImageValue="+questionImageValue+
                        "&optionImageValue="+optionImageValue;

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
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    Toast toast = Toast.makeText(AddQuestionType.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(AddQuestionType.this, QuestionTypeList.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast.makeText(AddQuestionType.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
