package com.langexpo.admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.model.Language;
import com.langexpo.model.QuestionType;
import com.langexpo.utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuestionTypeList extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    List<QuestionType> questionTypeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_type_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.admin_QuestionTypeList_my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.admin_question_type_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.admin_question_type_list_refresh_layout);
        //initializing the questionTypeList
        questionTypeList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionTypeList = new ArrayList<>();
                new GetQuestionTypeList(QuestionTypeList.this).execute();
            }
        });
        //to get all language list
        new GetQuestionTypeList(this).execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void goToQuestionType(View view) {
        Intent intent = new Intent(QuestionTypeList.this, AddQuestionType.class);
        startActivity(intent);
    }

    private class GetQuestionTypeList extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;

        public GetQuestionTypeList(QuestionTypeList activity){
            progressBar = new ProgressDialog(activity);
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

            String methodName = "featchAllQuestionType";
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
                url = new URL(stringBuilder.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url

                connection.setDoInput(true);
                connection.setInstanceFollowRedirects( false );

                // give it 15 seconds to respond
                connection.setReadTimeout(15*1000);
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
            System.out.println("featchAllQuestionType: "+result);
            try {
                JSONObject allLanguageList = new JSONObject(result);
                if(allLanguageList.get("status").toString().equalsIgnoreCase("ok")) {

                    JSONArray questionTypes = allLanguageList.getJSONArray("questionTypes");
                    questionTypes = questionTypes.getJSONArray(0);
                    JSONObject questionType;
                    //languages.getJSONObject(i).getString("languageId");
                    for(int i = 0; i<questionTypes.length();i++){
                        questionType = questionTypes.getJSONObject(i);
                        questionTypeList.add(new QuestionType(questionType.getLong("questionTypeId"),
                                questionType.getString("questionTypeName"),
                                questionType.getInt("totalOptions"),
                                questionType.getInt("totalDisplayOptions"),
                                questionType.getBoolean("multipleAnswer"),
                                questionType.getBoolean("questionAudio"),
                                questionType.getBoolean("optionAudio"),
                                questionType.getBoolean("questionImage"),
                                questionType.getBoolean("optionImage")));
                    }
                    QuestionTypeAdapter adapter = new QuestionTypeAdapter(QuestionTypeList.this, questionTypeList);
                    recyclerView.setAdapter(adapter);
                    progressBar.dismiss();
                    refreshLayout.setRefreshing(false);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Not available any Question type.",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
