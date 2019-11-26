package com.langexpo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.langexpo.R;
import com.langexpo.fragments.FragmentAudioQuestion;
import com.langexpo.fragments.FragmentFillingTheBlanks;
import com.langexpo.fragments.FragmentMultipleImageOption;
import com.langexpo.fragments.FragmentMultipleOption;
import com.langexpo.fragments.FragmentPictureQuestion;
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
import java.util.List;

public class DisplayLevelQuestions extends AppCompatActivity {


    Toolbar myToolbar;
    private ProgressBar questionProgressBar;
    long levelId;
    public List<QuestionModel> questionList;
    public List<Long> questionIdsList;
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

            levelId = getIntent().getLongExtra("levelId", 0);

            if(levelId!=0){
                new getAllQuestionIdsByCourseLevel(DisplayLevelQuestions.this, levelId).execute();
            }
        }
    }

    private class getAllQuestionIdsByCourseLevel extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long levelId;


        public getAllQuestionIdsByCourseLevel(Activity activity, long levelId){
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

            String methodName = "featchAllQuestionIDsByCourseLevel";
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
                connection.setReadTimeout(60*1000);
                connection.setConnectTimeout(60*1000);
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
                            new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                    DisplayLevelQuestions.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                    DisplayLevelQuestions.this);
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
            System.out.println("featchAllQuestionIDsByCourseLevel: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    JSONArray questions = loginResponse.getJSONArray("questions");
                    questions = questions.getJSONArray(0);
                    JSONObject question;
                    questionIdsList = new ArrayList<Long>(questions.length());
                    for(int i = 0; i<questions.length();i++){
                        question = questions.getJSONObject(i);
                        questionIdsList.add(question.getLong("questionId"));
                    }
                    //questionIdsList = (List<Long>)Utility.shuffleAndReduceQuestionToTen(questionIdsList);
                    totalQuestions = questionIdsList.size();
                    if(totalQuestions!=0){
                        nextQuestion(questionIdsList, false);
                    }

                    progressBar.dismiss();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_411")) {
                        LangExpoAlertDialog alertDialog =
                                new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                        DisplayLevelQuestions.this);
                        alertDialog.alertDialog("Duplicate", loginResponse.get("message").toString());
                    }
                    Toast toast = Toast
                            .makeText(DisplayLevelQuestions.this,
                                    loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextQuestion(List<Long> questionIdList, boolean quiz){
        try{
            questionProgressBar.setProgress(questionProgressBar.getProgress()+ (100/totalQuestions));
        }catch (ArithmeticException e){
            LangExpoAlertDialog alertDialog =
                    new LangExpoAlertDialog(DisplayLevelQuestions.this,
                            DisplayLevelQuestions.this);
            alertDialog.alertDialog("No Question", "No questions, Please contact Admin.".toString());
        }
        if(questionIdList.isEmpty()){

            //Toast toast = Toast.makeText(DisplayLevelQuestions.this,"Congratulations! you have completed a lesson.",Toast.LENGTH_LONG);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Practice Lesson result")
                    .setMessage("Congratulations! you have completed a lesson.\n" +
                            "your score is \nCorrect: "+correctCount+"\nIncorrect:"+incorrectCount)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Toast.makeText(DisplayLevelQuestions.this, String.valueOf(levelId), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DisplayLevelQuestions.this,
                                    UserLevelList.class);
                            startActivity(intent);
                            new AddUserProgressAsyncTask(DisplayLevelQuestions.this,
                                    levelId, 0, correctCount,
                                    incorrectCount).execute();

                            correctCount=0;
                            incorrectCount=0;
                        }});
            builder.show();

        }else {

            new GetQuestionDetail(DisplayLevelQuestions.this,
                    questionIdsList.get(0), questionIdList).execute();

        }
    }

    private class GetQuestionDetail extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long questionId;
        private List<Long> questionIdList;


        public GetQuestionDetail(Activity activity,
                                 long questionId, List<Long> questionIdList){
            progressBar = new ProgressDialog(activity);
            this.questionId = questionId;
            this.questionIdList = questionIdList;

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

            String methodName = "fetchQuestion";
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
                String urlParameters  = "questionId="+questionId;

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
                connection.setReadTimeout(60*1000);
                connection.setConnectTimeout(60*1000);
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
                            new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                    DisplayLevelQuestions.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                    DisplayLevelQuestions.this);
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
            System.out.println("fetchQuestion: "+result);
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
                                question.getString("optionImages"),
                                question.getString("questionImages")));
                    }

                    if(questionList.size()!=0){
                        loadFragmentWithQuestion(questionList.get(0), questionIdList, false);
                    }

                    progressBar.dismiss();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_411")) {
                        LangExpoAlertDialog alertDialog =
                                new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                        DisplayLevelQuestions.this);
                        alertDialog.alertDialog("Duplicate", loginResponse.get("message").toString());
                    }
                    Toast toast = Toast
                            .makeText(DisplayLevelQuestions.this,
                                    loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFragmentWithQuestion(QuestionModel questionModel,
                                         List<Long> questionIdsList, boolean quiz){
        QuestionModel q = questionList.get(0);
        if (questionModel.getQuestionType() == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                    new FragmentMultipleOption(questionModel, questionIdsList, quiz)).commit();
        }else if (q.getQuestionType() == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                    new FragmentMultipleImageOption(questionModel, questionIdsList, quiz)).commit();
        }else if (q.getQuestionType() == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                    new FragmentFillingTheBlanks(questionModel, questionIdsList, quiz)).commit();
        }else if (q.getQuestionType() == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                    new FragmentAudioQuestion(questionModel, questionIdsList, quiz)).commit();
        }else if (q.getQuestionType() == 5) {
            getSupportFragmentManager().beginTransaction().replace(R.id.question_conainer,
                    new FragmentPictureQuestion(questionModel, questionIdsList, quiz)).commit();
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
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                    DisplayLevelQuestions.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                    DisplayLevelQuestions.this);
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
                        LangExpoAlertDialog alertDialog =
                                new LangExpoAlertDialog(DisplayLevelQuestions.this,
                                        DisplayLevelQuestions.this);
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
