package com.langexpo.activity;

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
import android.widget.RadioButton;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.admin.activity.AddLanguage;
import com.langexpo.admin.activity.LanguageAdapter;
import com.langexpo.admin.activity.LanguageList;
import com.langexpo.model.Language;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SelectLaunguage extends AppCompatActivity {


    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    List<Language> languageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_launguage);


        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.select_language_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.select_language_refresh_layout);
        //initializing the languageList
        languageList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                languageList = new ArrayList<>();
                new GetLanguageList(SelectLaunguage.this).execute();
            }
        });
        //to get all language list
        new GetLanguageList(this).execute();
        if(Session.get(Constant.Session.USER_SELECTED_LANGUAGE).equalsIgnoreCase("")){
            SelectLanguageAdapter.lastCheckedRB=null;
        }



    }

    public void continue1(View view) {
        if(SelectLanguageAdapter.lastCheckedRB==null){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(SelectLaunguage.this, SelectLaunguage.this);
            alertDialog.alertDialog("Error", "Please select Language.");
            return;
        }

        Intent intent= new Intent(this,DailyGoal.class);
        startActivity(intent);

    }
    public void back(View view)
    {
        Intent intent=new Intent (this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class GetLanguageList extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;

        public GetLanguageList(SelectLaunguage activity){
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

            String methodName = "featchAllLanguages";
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
                if(e instanceof ConnectException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(SelectLaunguage.this, SelectLaunguage.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(SelectLaunguage.this, SelectLaunguage.this);
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
            System.out.println(result);
            try {
                JSONObject allLanguageList = new JSONObject(result);
                if(allLanguageList.get("status").toString().equalsIgnoreCase("ok")) {

                    JSONArray languages = allLanguageList.getJSONArray("languages");
                    languages = languages.getJSONArray(0);
                    JSONObject language;
                    //languages.getJSONObject(i).getString("languageId");
                    for(int i = 0; i<languages.length();i++){
                        language = languages.getJSONObject(i);
                        languageList.add(new Language(language.getLong("languageId"),
                                language.getString("languageName"),
                                language.getString("languageFlagURL")));
                    }
                    SelectLanguageAdapter adapter = new SelectLanguageAdapter(SelectLaunguage.this, languageList);
                    recyclerView.setAdapter(adapter);
                    progressBar.dismiss();
                    refreshLayout.setRefreshing(false);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Not available any language. We are working on it. Thanks for your patience.",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

   }
