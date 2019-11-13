package com.langexpo.admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Blob;

public class AddLecture extends AppCompatActivity {

    long lectureId, languageId, levelId;
    Toolbar myToolbar;
    EditText lectureNameET, lectureContentET ,sequenceNumberET;
    Button addLectureBT;
    TextView lectureContentPreview, previewText;
    Spinner languageSpinner, levelSpinner;
    ArrayAdapter languageSpinnerAdapter, levelSpinnerAdapter;
    String lectureNameValue, lectureContentValue, languageNameValue, levelNameValue;
    int sequenceNumberValue;
    boolean updateLecture = false;
    String[] langaugeArray, levelArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        new GetLanguageList(AddLecture.this).execute();
        new GetLevelList(AddLecture.this).execute();

        myToolbar = (Toolbar) findViewById(R.id.admin_add_lecture_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addLectureBT = (Button) findViewById(R.id.button_add_lecture);
        lectureNameET = (EditText) findViewById(R.id.admin_add_lecture_name_et);
        lectureContentET = (EditText) findViewById(R.id.admin_add_lecture_content_et);
        sequenceNumberET = (EditText) findViewById(R.id.admin_add_lecture_sequence_et);
        languageSpinner = (Spinner) findViewById(R.id.admin_add_lecture_language_spinner);
        levelSpinner = (Spinner) findViewById(R.id.admin_add_lecture_level_spinner);
        lectureContentPreview = (TextView) findViewById(R.id.admin_add_lecture_content_preview);
        previewText = (TextView) findViewById(R.id.admin_add_lecture_preview_tv);
        //previewBT = (Button) findViewById(R.id.button_preivew_lecture_content);

        // display preview in dialogbox.
        previewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(AddLecture.this, AddLecture.this);
                alertDialog.alertDialog("Preview", Html.fromHtml(lectureContentET.getText().toString(), Html.FROM_HTML_MODE_LEGACY).toString());
            }
        });
        lectureContentET.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        lectureContentPreview.setText(Html.fromHtml(s.toString(), Html.FROM_HTML_MODE_LEGACY));
                    } else
                        lectureContentPreview.setText(Html.fromHtml(s.toString()));
            }
        });

        //language spinner item on selected listner
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                languageNameValue = parentView.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });
        //level spinner item on selected listner
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                levelNameValue = parentView.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });
        getIncomingIntent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("lectureId") &&
                getIntent().hasExtra("lectureName") &&
                getIntent().hasExtra("lectureContent") &&
                getIntent().hasExtra("languageId") &&
                getIntent().hasExtra("languageName") &&
                getIntent().hasExtra("levelId") &&
                getIntent().hasExtra("levelName") &&
                getIntent().hasExtra("sequenceNumber")) {
            updateLecture = true;
            myToolbar.setTitle("Update Lecture");
            addLectureBT.setText("Update");
            lectureId = getIntent().getLongExtra("lectureId", 0);
            lectureNameValue = getIntent().getStringExtra("lectureName");
            lectureContentValue = getIntent().getStringExtra("lectureContent");
            languageId = getIntent().getLongExtra("languageId",0);
            languageNameValue = getIntent().getStringExtra("languageName");
            levelId = getIntent().getLongExtra("levelId",0);
            levelNameValue = getIntent().getStringExtra("levelName");
            sequenceNumberValue = getIntent().getIntExtra("sequenceNumber",0);

            setLectureDetail(lectureId, lectureNameValue, lectureContentValue, languageId,
                    languageNameValue, levelId, levelNameValue, sequenceNumberValue);
        }
    }

    private void setLectureDetail(long lectureId, String lectureNameValue, String lectureContentValue,
                                  long languageId, String languageNameValue, long levelId,
                                  String levelNameValue, int sequenceNumberValue){
        lectureNameET.setText(lectureNameValue);
        sequenceNumberET.setText(String.valueOf(sequenceNumberValue));
        lectureContentET.setText(lectureContentValue);
        if(lectureContentValue.length() != 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lectureContentPreview.setText(Html.fromHtml(lectureContentValue.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                lectureContentPreview.setText(Html.fromHtml(lectureContentValue.toString()));
            }
    }

    public void addLecture(View view) {
        lectureNameValue = lectureNameET.getText().toString();
        sequenceNumberValue = Utility.getValue(sequenceNumberET.getText().toString(),0);
        lectureContentValue = lectureContentET.getText().toString();

        //Validation - start
        if (lectureNameValue.equalsIgnoreCase("")) {
            lectureNameET.setError("Please enter lecture name");
            lectureNameET.requestFocus();
            return;
        }
        if (sequenceNumberValue==0) {
            sequenceNumberET.setError("Please enter sequence number");
            sequenceNumberET.requestFocus();
            return;
        }
        if (languageNameValue.trim().equalsIgnoreCase(Constant.SPINNER_DEFAULT_VALUE_LANGUAGE)) {
            ((TextView)languageSpinner.getSelectedView()).setError("Please select Language");
            languageSpinner.requestFocus();
            return;
        }
        if (levelNameValue.trim().equalsIgnoreCase(Constant.SPINNER_DEFAULT_VALUE_LEVEL)) {
            ((TextView)levelSpinner.getSelectedView()).setError("Please select level");
            levelSpinner.requestFocus();
            return;
        }
        if (lectureContentValue.equalsIgnoreCase("")) {
            lectureContentET.setError("Please enter lecture content");
            lectureContentET.requestFocus();
            return;
        }
        //validation - end

        new AddLectureAsyncTask(AddLecture.this, lectureId,
                lectureNameValue, sequenceNumberValue, languageNameValue, levelNameValue,
                lectureContentValue).execute();
    }

    public void deleteLecture(View view) {
        if(lectureId!=0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Delete Lecture")
                    .setMessage("Do you really want to delete Lecture?")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Toast.makeText(AddLanguage.this, "Yaay", Toast.LENGTH_SHORT).show();
                            new DeleteLectureAsyncTask(AddLecture.this, levelId).execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Toast.makeText(AddLanguage.this, "no", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.show();
        }
    }

    private class DeleteLectureAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long lectureId;

        public DeleteLectureAsyncTask(Activity activity, long lectureId){
            progressBar = new ProgressDialog(activity);
            this.lectureId = lectureId;
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

            String methodName = "deleteLecture";
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

                String urlParameters  = "lectureId="+lectureId;


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

                    Toast toast = Toast.makeText(AddLecture.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(AddLecture.this, LevelList.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast.makeText(AddLecture.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddLectureAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long lectureId;
        private String lectureNameValue;
        private int sequenceNumberValue;
        private String languageNameValue;
        private String levelNameValue;
        private String lectureContentValue;
        private byte[] lectureContentByteData;

        public AddLectureAsyncTask(Activity activity, long lectureId,
               String lectureNameValue, int sequenceNumberValue, String languageNameValue,
               String levelNameValue, String lectureContentValue){
            progressBar = new ProgressDialog(activity);
            this.lectureId = lectureId;
            this.lectureNameValue = lectureNameValue;
            this.sequenceNumberValue = sequenceNumberValue;
            this.languageNameValue = languageNameValue;
            this.levelNameValue = levelNameValue;
            this.lectureContentValue = lectureContentValue;

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

            String methodName = "addUpdateLecture";
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
                lectureContentByteData = lectureContentValue.getBytes("UTF-8");//Better to specify encoding
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                String urlParameters  = "lectureId="+lectureId+"&lectureNameValue="+lectureNameValue+
                        "&sequenceNumberValue="+sequenceNumberValue+"&languageNameValue="+languageNameValue+
                        "&levelNameValue="+levelNameValue+"&lectureContentValue="+lectureContentByteData;

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
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(AddLecture.this, AddLecture.this);
                    alertDialog.alertDialog("Network issue", "Due to maintenance, currently service unavailable");

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(AddLecture.this, AddLecture.this);
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
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    Toast toast = Toast.makeText(AddLecture.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(AddLecture.this, LectureList.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_411")) {
                        LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(AddLecture.this, AddLecture.this);
                        alertDialog.alertDialog("Duplicate", loginResponse.get("message").toString());
                    }
                    Toast toast = Toast.makeText(AddLecture.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetLanguageList extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;

        public GetLanguageList(AddLecture activity){
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

                    JSONArray lectures = allLanguageList.getJSONArray("languages");
                    lectures = lectures.getJSONArray(0);
                    JSONObject lecture;
                    //languages.getJSONObject(i).getString("languageId");
                    langaugeArray = new String[lectures.length()+1];
                    langaugeArray[0] = Constant.SPINNER_DEFAULT_VALUE_LANGUAGE;
                    for(int i = 1; i<=lectures.length();i++){
                        lecture = lectures.getJSONObject(i-1);
                        langaugeArray[i] = lecture.getString("languageName");
                    }
                    languageSpinnerAdapter = new ArrayAdapter(
                            AddLecture.this,android.R.layout.simple_list_item_1 ,langaugeArray);
                    if(languageSpinnerAdapter!=null) {
                        languageSpinner.setAdapter(languageSpinnerAdapter);
                    }
                    if(languageNameValue!=""){
                        languageSpinner.setSelection(languageSpinnerAdapter.getPosition(languageNameValue));
                    }
                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Not available any language.",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetLevelList extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;

        public GetLevelList(AddLecture activity){
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

            String methodName = "featchAlllevel";
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
            System.out.println(result);
            try {
                JSONObject allLanguageList = new JSONObject(result);
                if(allLanguageList.get("status").toString().equalsIgnoreCase("ok")) {

                    JSONArray levels = allLanguageList.getJSONArray("levels");
                    levels = levels.getJSONArray(0);
                    JSONObject level;
                    //languages.getJSONObject(i).getString("languageId");
                    levelArray = new String[levels.length()+1];
                    levelArray[0] = Constant.SPINNER_DEFAULT_VALUE_LEVEL;
                    for(int i = 1; i<=levels.length();i++){
                        level = levels.getJSONObject(i-1);
                        levelArray[i] = level.getString("levelName");
                    }
                    levelSpinnerAdapter = new ArrayAdapter(
                            AddLecture.this,android.R.layout.simple_list_item_1 ,levelArray);
                    if(levelSpinnerAdapter!=null) {
                        levelSpinner.setAdapter(levelSpinnerAdapter);
                    }
                    if(!levelNameValue.equalsIgnoreCase("")){
                        levelSpinner.setSelection(levelSpinnerAdapter.getPosition(levelNameValue));
                    }
                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Not available any level.",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
