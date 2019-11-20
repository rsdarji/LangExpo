package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.admin.activity.AddLanguage;
import com.langexpo.admin.activity.AddLevel;
import com.langexpo.admin.activity.LevelList;
import com.langexpo.customfunction.CustomRadioGroupView;
import com.langexpo.fragments.FragmentFillingTheBlanks;
import com.langexpo.fragments.FragmentMultipleImageOption;
import com.langexpo.fragments.FragmentMultipleOption;
import com.langexpo.fragments.FragmentUserHome;
import com.langexpo.model.Lecture;
import com.langexpo.model.QuestionModel;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;
import com.langexpo.utility.Utility;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DisplayLevelQuestions extends AppCompatActivity {


    Toolbar myToolbar;
    private ProgressBar questionProgressBar;
    long levelId;
    public List<QuestionModel> questionList;
    String checkedRadioButtonText = "";
    int totalQuestions=0;
    public static int correctCount=0;
    public static int incorrectCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_level_questions);

        myToolbar = (Toolbar) findViewById(R.id.question_container_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        questionProgressBar = (ProgressBar) findViewById(R.id.question_progress);
        questionProgressBar.setProgress(0);
        getIncomingIntent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getIncomingIntent() {
        if (getIntent().hasExtra("levelId") &&
                getIntent().hasExtra("levelName") &&
                getIntent().hasExtra("userLevel") &&
                getIntent().hasExtra("languageId") &&
                getIntent().hasExtra("languageName") &&
                getIntent().hasExtra("sequenceNumber")) {
            /*updateLevel = true;
            myToolbar.setTitle("Update Level");
            addLevelBT.setText("Update");*/
            levelId = getIntent().getLongExtra("levelId", 0);
            /*levelNameValue = getIntent().getStringExtra("levelName");
            userLevelValue = getIntent().getStringExtra("userLevel");
            languageId = getIntent().getLongExtra("languageId",0);
            languageSpinnerValue = getIntent().getStringExtra("languageName");
            sequenceNumberValue = getIntent().getIntExtra("sequenceNumber",0);*/

            //setLevelDetail(levelId, levelNameValue, userLevelValue, languageSpinnerValue, sequenceNumberValue);

            if(levelId!=0){
                new gelAllQuestionByCourseLevel(DisplayLevelQuestions.this, levelId).execute();
            }


        }
    }

    private class gelAllQuestionByCourseLevel extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long levelId;


        public gelAllQuestionByCourseLevel(Activity activity, long levelId){
            progressBar = new ProgressDialog(activity);
            this.levelId = levelId;

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

            String methodName = "featchAllQuestionsByCourseLevel";
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
                String urlParameters  = "levelId="+levelId;

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
                connection.setReadTimeout(30*1000);
                connection.setConnectTimeout(30*1000);
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
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
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
            System.out.println("featchAllQuestionsByCourseLevel: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    JSONArray questions = loginResponse.getJSONArray("questions");
                    questions = questions.getJSONArray(0);
                    JSONObject question;
                    questionList = new ArrayList<QuestionModel>(questions.length());
                    //languages.getJSONObject(i).getString("languageId");
                    for(int i = 0; i<questions.length();i++){
                        question = questions.getJSONObject(i);

                        questionList.add(new QuestionModel(question.getLong("questionId"),
                                question.getString("question"),
                                question.getInt("audio"),
                                question.getString("answer"),
                                question.getString("questionOption"),
                                question.getLong("questionType"),
                                question.getLong("courseLevel"),
                                question.getString("optionImages")));
                    }
                    totalQuestions = questionList.size();
                    questionList = Utility.shuffleAndReduceQuestionToTen(questionList);
                    if(totalQuestions!=0){
                        nextQuestion(questionList, false);

                    }

                    /*adapter = new UserLectureListAdapter(UserLectureList.this, lectureList);
                    recyclerView.setAdapter(adapter);*/
                    progressBar.dismiss();
                    /*refreshLayout.setRefreshing(false);*/
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_411")) {
                        LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
                        alertDialog.alertDialog("Duplicate", loginResponse.get("message").toString());
                    }
                    Toast toast = Toast.makeText(DisplayLevelQuestions.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextQuestion(List<QuestionModel> questionList, boolean quiz){
        try{
            questionProgressBar.setProgress(questionProgressBar.getProgress()+ (100/totalQuestions));
        }catch (ArithmeticException e){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
            alertDialog.alertDialog("No Question", "No questions, Please contact Admin.".toString());
        }
        if(questionList.isEmpty()){

            //Toast toast = Toast.makeText(DisplayLevelQuestions.this,"Congratulations! you have completed a lesson.",Toast.LENGTH_LONG);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Practice Lesson result")
                    .setMessage("Congratulations! you have completed a lesson.\n" +
                            "your score is \nCorrect: "+correctCount+"\nIncorrect:"+incorrectCount)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(DisplayLevelQuestions.this, String.valueOf(levelId), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DisplayLevelQuestions.this, UserLevelList.class);
                            startActivity(intent);
                            new AddUserProgressAsyncTask(DisplayLevelQuestions.this, levelId, 0, correctCount,
                                    incorrectCount).execute();

                            correctCount=0;
                            incorrectCount=0;
                        }});
            builder.show();

            /*LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
            alertDialog.alertDialog("Congratulation", "Congratulations! you have completed a lesson.\n" +
                    "your score is \nCorrect: "+correctCount+"\nIncorrect:"+incorrectCount);*/

        }else {
            QuestionModel q = questionList.get(0);
            if (q.getQuestionType() == 1) {
                getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                        new FragmentMultipleOption(questionList, quiz)).commit();
            }else if (q.getQuestionType() == 2) {
                getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                        new FragmentMultipleImageOption(questionList, quiz)).commit();
            }else if (q.getQuestionType() == 3) {
                getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                        new FragmentFillingTheBlanks()).commit();
            }
        }
    }

    private class AddUserProgressAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long levelId;
        private long userId = Long.parseLong(Session.get(Constant.User.USER_ID));
        private long quizId;
        private long correctCount;
        private long inCorrectCount;
        private int attempt;

        public AddUserProgressAsyncTask(Activity activity, long levelId, long quizId, int correctCount,
                                    int inCorrectCount){
            progressBar = new ProgressDialog(activity);
            this.levelId = levelId;
            this.quizId = quizId;
            this.correctCount = correctCount;
            this.inCorrectCount = inCorrectCount;
            this.attempt = attempt;

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

            String methodName = "addUpdateProgressReport";
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
                String urlParameters  = "userId="+userId+"&levelId="+levelId+"&quizId="+quizId+
                        "&correctCount="+correctCount+"&inCorrectCount="+inCorrectCount;

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
                connection.setReadTimeout(15*1000);
                connection.setConnectTimeout(15*1000);
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
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
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
            System.out.println("addUpdateProgressReport: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    Toast toast = Toast.makeText(DisplayLevelQuestions.this,
                            loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();


                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_411")) {
                        LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DisplayLevelQuestions.this, DisplayLevelQuestions.this);
                        alertDialog.alertDialog("Duplicate", loginResponse.get("message").toString());
                    }
                    Toast toast = Toast.makeText(DisplayLevelQuestions.this,
                            loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
