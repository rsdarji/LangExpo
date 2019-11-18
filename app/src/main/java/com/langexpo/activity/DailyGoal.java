package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.admin.activity.GoalAdapter;
import com.langexpo.admin.activity.GoalList;
import com.langexpo.model.Goal;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DailyGoal extends AppCompatActivity {


    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    List<Goal> dailyGoalList;
    public static Set<String> userSelectedGoals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_goal);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.select_goal_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.select_goal_refresh_layout);
        //initializing the languageList
        dailyGoalList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dailyGoalList = new ArrayList<>();
                new GetGoalList(DailyGoal.this, DailyGoal.this).execute();
            }
        });
        //to get all language list
        new GetGoalList(this, DailyGoal.this).execute();



    }
    public void goalContinue(View view) {
        Intent intent=new Intent(this,Levels.class);
        Session.set(Constant.Session.USER_SELECTED_GOALS, userSelectedGoals);

        if(userSelectedGoals.size()==0){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DailyGoal.this, DailyGoal.this);
            alertDialog.alertDialog("Error", "Please select atleast one goal.");
            return;
        }
        /*List<String> userSelectedGoalList = new ArrayList<String>();
        for(String s : userSelectedGoals){
            Toast.makeText(DailyGoal.this, s,Toast.LENGTH_SHORT).show();
            userSelectedGoalList.add(s);
        }
        intent.putStringArrayListExtra("userSelectedGoals", (ArrayList<String>)userSelectedGoalList);*/
        startActivity(intent);

    }
    public void back(View view)
    {
        Intent intent=new Intent (this,SelectLaunguage.class);
        startActivity(intent);
    }

    private class GetGoalList extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        android.app.AlertDialog.Builder builder;
        Context mContext;

        public GetGoalList(DailyGoal activity, Context mContext){
            progressBar = new ProgressDialog(activity);
            builder = new android.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            this.mContext = mContext;
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

            String methodName = "featchAllGoal";
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
                connection.setConnectTimeout(15*1000);
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
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DailyGoal.this, DailyGoal.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(DailyGoal.this, DailyGoal.this);
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

                    JSONArray goals = allLanguageList.getJSONArray("goals");
                    goals = goals.getJSONArray(0);
                    JSONObject goal;
                    userSelectedGoals = new HashSet<>(goals.length());
                    //languages.getJSONObject(i).getString("languageId");
                    for(int i = 0; i<goals.length();i++){
                        goal = goals.getJSONObject(i);
                        dailyGoalList.add(new Goal(goal.getLong("goalId"),
                                goal.getString("goalName")));
                        if(Boolean.parseBoolean(Session.get(goal.getString("goalName")))){
                            userSelectedGoals.add(goal.getString("goalName"));
                        }
                    }
                    DailyGoalAdapter adapter = new DailyGoalAdapter(DailyGoal.this, dailyGoalList);
                    recyclerView.setAdapter(adapter);
                    progressBar.dismiss();
                    refreshLayout.setRefreshing(false);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Not available any Goal.",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
