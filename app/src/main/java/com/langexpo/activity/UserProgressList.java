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
import com.langexpo.model.Quiz;
import com.langexpo.model.UserProgress;
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

public class UserProgressList extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    List<UserProgress> userProgressList;
    UserProgressListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_progress_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.user_progress_list_my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserProgressList.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.user_progress_list_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.user_progress_list_refresh_layout);
        //initializing the levelList
        userProgressList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userProgressList = new ArrayList<>();
                new GetUserProgressList(UserProgressList.this, UserProgressList.this).execute();
            }
        });
        //to get quiz list
        new GetUserProgressList(UserProgressList.this, UserProgressList.this).execute();
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
                    userProgressList = new ArrayList<>();
                    new GetUserProgressList(UserProgressList.this, UserProgressList.this).execute();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equalsIgnoreCase("")) {

                    adapter.getFilter().filter(newText);
                }else{
                    userProgressList = new ArrayList<>();
                    new GetUserProgressList(UserProgressList.this, UserProgressList.this).execute();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private class GetUserProgressList extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        Context mContext;
        long userId = Long.parseLong(Session.get(Constant.User.USER_ID));

        public GetUserProgressList(UserProgressList activity, Context mContext) {
            progressBar = new ProgressDialog(activity);
            this.mContext = mContext;
        }

        protected void onPreExecute() {
            progressBar.setMessage("Loading...");
            progressBar.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();

            String methodName = "fetchUserProgress";
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

                String urlParameters  = "userId="+userId;

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

                System.out.println("response: " + stringBuilder.toString());
            } catch (Exception e) {
                if (e instanceof ConnectException) {
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(UserProgressList.this, UserProgressList.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);
                    UserProgressList.this.runOnUiThread(new Runnable() {
                        public void run() {
                            refreshLayout.setRefreshing(false);
                        }
                    });
                } else if (e instanceof SocketTimeoutException) {
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog =
                            new LangExpoAlertDialog(UserProgressList.this, UserProgressList.this);
                    alertDialog.alertDialog("Time out", "Please try again.");

                    UserProgressList.this.runOnUiThread(new Runnable() {
                        public void run() {
                            refreshLayout.setRefreshing(false);
                        }
                    });

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
            System.out.println("fetchUserProgress: " + result);
            try {
                JSONObject allLanguageList = new JSONObject(result);
                if (allLanguageList.get("status").toString().equalsIgnoreCase("ok")) {

                    JSONArray userProgresses = allLanguageList.getJSONArray("userProgresses");
                    userProgresses = userProgresses.getJSONArray(0);
                    JSONObject userProgress;
                    //languages.getJSONObject(i).getString("languageId");
                    for (int i = 0; i < userProgresses.length(); i++) {
                        userProgress = userProgresses.getJSONObject(i);
                        userProgressList.add(new UserProgress(userProgress.getLong("userId"),
                                userProgress.getString("name"),
                                userProgress.getLong("levelId"),
                                userProgress.getString("status"),
                                userProgress.getLong("quizId"),
                                userProgress.getInt("attempt"),
                                userProgress.getInt("correctAnswerCount"),
                                userProgress.getInt("inCorrectAnswerCount")));

                    }
                    adapter = new UserProgressListAdapter(UserProgressList.this, userProgressList);
                    recyclerView.setAdapter(adapter);
                    progressBar.dismiss();
                    refreshLayout.setRefreshing(false);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not available any User Progress report.", Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
