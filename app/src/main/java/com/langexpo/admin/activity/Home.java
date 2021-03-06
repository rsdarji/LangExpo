package com.langexpo.admin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.langexpo.R;
import com.langexpo.activity.UserLectureList;
import com.langexpo.activity.UserLevelList;
import com.langexpo.activity.UserProgressList;
import com.langexpo.activity.UserQuizList;
import com.langexpo.com.langexpo.navigationdrawer.FragmentHome;
import com.langexpo.com.langexpo.navigationdrawer.NavigationDrawer;
import com.langexpo.fragments.AddGoalFragment;
import com.langexpo.fragments.AddLectureFragment;
import com.langexpo.fragments.AddLevelFragment;
import com.langexpo.fragments.AddQuestionFragment;
import com.langexpo.fragments.AddQuizFragment;
import com.langexpo.fragments.FragmentUserHome;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;

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
import java.nio.charset.StandardCharsets;

public class Home extends NavigationDrawer {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        if (savedInstanceState == null && Session.get(Constant.User.ROLE).equalsIgnoreCase("1")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                    new FragmentUserHome()).commit();
            //Toast.makeText(Home.this, "Not admin",Toast.LENGTH_LONG).show();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                    new FragmentHome()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    public void addQuestion(View view) {
        Intent i = new Intent(Home.this, AddQuestion.class);
        startActivity(i);
        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddQuestionFragment()).commit();*/
    }

    public void addQuestionType(View view) {
        Intent i = new Intent(Home.this, QuestionTypeList.class);
        startActivity(i);
        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddLanguage()).commit();*/

    }

    public void addLecture(View view) {
        Intent i = new Intent(Home.this, LectureList.class);
        startActivity(i);
        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddLectureFragment()).commit();*/
    }

    public void addLanguage(View view) {
        Intent i = new Intent(Home.this, LanguageList.class);
        startActivity(i);
        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddLanguage()).commit();*/
    }

    public void addGoal(View view) {
        Intent i = new Intent(Home.this, GoalList.class);
        startActivity(i);
        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddGoalFragment()).commit();*/
    }

    public void addLevel(View view) {
        Intent i = new Intent(Home.this, LevelList.class);
        startActivity(i);
        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddLevelFragment()).commit();*/
    }

    public void addQuiz(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddQuizFragment()).commit();
    }

    public void goToUserProgress(View view) {
        Intent i = new Intent(Home.this, UserProgressList.class);
        startActivity(i);
    }

    public void goToUserQuiz(View view) {
        Intent i = new Intent(Home.this, UserQuizList.class);
        startActivity(i);

    }

    public void goToUserLecture(View view) {
        Intent i = new Intent(Home.this, UserLectureList.class);
        startActivity(i);
    }

    public void goToUserLevel(View view) {
        Intent i = new Intent(Home.this, UserLevelList.class);
        startActivity(i);
    }

    private class addUpdateImage extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String image;
        private String encoadedImg;
        String imgStr;

        public addUpdateImage(Home activity, String image) {
            progressBar = new ProgressDialog(activity);
            this.image = image;
            this.encoadedImg = encoadedImg;
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


            System.out.println("artid: background method: " + image);
            try {

                //path = se.get("encoadedImg");
                //artname artcategory artyear artprice artdescription path userid categoryid
                String urlParameters = "image=" + image;

                //imgStr = Base64.getEncoder().encodeToString(image);
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                // create the HttpURLConnection
                url = new URL("http://" + Constant.WEB_SERVICE_HOST + ":" + Constant.WEB_SERVICE_PORT + "/WebApplication1/webresources/mobile/addprofileimage");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                // uncomment this if you want to write output to this url
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                // give it 15 seconds to respond
                connection.setReadTimeout(45 * 1000);


                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(postData);
                }


                connection.connect();




                /*System.out.println("path: "+path);
                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/addart" +
                        "&"+artName+"&"+artCategory+"&"+artYear+"&"+artPrice+"&"+artDescription+"&"+path+"&"+userId+"&"+categoryId);
                //System.out.println("url addart: "+url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url
                connection.setDoInput(true);
                // give it 15 seconds to respond
                connection.setReadTimeout(15*1000);
                connection.connect();*/

                // read the output from the server

                int status = connection.getResponseCode();
                if (status < 400) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    //read response
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    //read response
                }
                //reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println(stringBuilder.toString());
            } catch (Exception e) {
                if(e instanceof ConnectException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Home.this, Home.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Home.this, Home.this);
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
            System.out.println(result);

            try {
                JSONObject loginResponse = new JSONObject(result);
                if (loginResponse.length() != 0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok")) {

                    /*Intent intent = new Intent(UploadNewArt.this, Artistchoice.class);
                    startActivity(intent);*/
                    Toast.makeText(getApplicationContext(), "Your profile picture has been changed successfully.", Toast.LENGTH_LONG).show();
                    progressBar.dismiss();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sorry! Please try again later...", Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
