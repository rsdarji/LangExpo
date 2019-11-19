package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.admin.activity.Home;
import com.langexpo.model.Level;
import com.langexpo.model.Quiz;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;

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

public class UserQuizList extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    List<Quiz> quizList;
    UserQuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quiz_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.user_quiz_list_my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserQuizList.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.user_quiz_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.user_quiz_list_refresh_layout);
        //initializing the levelList
        quizList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                quizList = new ArrayList<>();
                new GetUserQuizList(UserQuizList.this, UserQuizList.this).execute();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.equalsIgnoreCase("")) {

                    adapter.getFilter().filter(query);
                }else{
                    quizList = new ArrayList<>();
                    new GetUserQuizList(UserQuizList.this, UserQuizList.this).execute();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equalsIgnoreCase("")) {

                    adapter.getFilter().filter(newText);
                }else{
                    quizList = new ArrayList<>();
                    new GetUserQuizList(UserQuizList.this, UserQuizList.this).execute();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private class GetUserQuizList extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        android.app.AlertDialog.Builder builder;
        Context mContext;
        String userLanguage = Session.get(Constant.User.LANGUAGE);
        String userLevel = Session.get(Constant.User.USER_LEVEL);

        public GetUserQuizList(UserQuizList activity, Context mContext) {
            progressBar = new ProgressDialog(activity);
            builder = new android.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            this.mContext = mContext;
        }

        protected void onPreExecute() {
            progressBar.setMessage("Loading...");
            progressBar.show();
            //builder = new LangExpoAlertDialog.Builder(mContext, R.style.MyDialogTheme);
            //builder.create();
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();

            String methodName = "fetchUserQuiz";
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

                String urlParameters  = "userLevel="+String.valueOf(Session.get(Constant.User.USER_LEVEL));

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

                System.out.println("response: " + stringBuilder.toString());
            } catch (Exception e) {
                if (e instanceof ConnectException) {
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(UserQuizList.this, UserQuizList.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                } else if (e instanceof SocketTimeoutException) {
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(UserQuizList.this, UserQuizList.this);
                    alertDialog.alertDialog("Time out", "Please try again.");
                }
                e.printStackTrace();
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuilder.toString();
        }

        @Override

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("fetchUserQuiz: " + result);
            try {
                JSONObject allLanguageList = new JSONObject(result);
                if (allLanguageList.get("status").toString().equalsIgnoreCase("ok")) {

                    JSONArray quizes = allLanguageList.getJSONArray("quizes");
                    quizes = quizes.getJSONArray(0);
                    JSONObject quiz;
                    //languages.getJSONObject(i).getString("languageId");
                    for (int i = 0; i < quizes.length(); i++) {
                        quiz = quizes.getJSONObject(i);
                        quizList.add(new Quiz(quiz.getLong("quizId"),
                                quiz.getString("quizName"),
                                quiz.getString("userLevel"),
                                quiz.getString("levelIds")));
                    }
                    adapter = new UserQuizAdapter(UserQuizList.this, quizList);
                    recyclerView.setAdapter(adapter);
                    progressBar.dismiss();
                    refreshLayout.setRefreshing(false);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not available any Quiz.", Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
