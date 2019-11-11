package com.langexpo.admin.activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Utility;

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

public class AddLevel extends AppCompatActivity {

    long levelId;
    Toolbar myToolbar;
    EditText levelNameET, sequenceNumberET;
    Button addLevelBT;
    Spinner userLevelSpinner;
    String levelNameValue, userLevelValue;
    int sequenceNumberValue;
    boolean updateLevel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_level);

        myToolbar = (Toolbar) findViewById(R.id.admin_add_level_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addLevelBT = (Button) findViewById(R.id.admin_button_add_level);
        levelNameET = (EditText) findViewById(R.id.admin_add_level_level_name);
        sequenceNumberET = (EditText) findViewById(R.id.admin_add_level_levelname_et);
        userLevelSpinner = (Spinner) findViewById(R.id.admin_add_level_spinner);

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
                getIntent().hasExtra("sequenceNumber")) {
            updateLevel = true;
            myToolbar.setTitle("Update Level");
            addLevelBT.setText("Update Level");
            levelId = getIntent().getLongExtra("levelId", 0);
            levelNameValue = getIntent().getStringExtra("levelName");
            userLevelValue = getIntent().getStringExtra("userLevel");
            sequenceNumberValue = getIntent().getIntExtra("sequenceNumber",0);



            setLevelDetail(levelId, levelNameValue, userLevelValue, sequenceNumberValue);
        }
    }

    private void setLevelDetail(long levelId, String levelNameValue,
               String userLevelValue, int sequenceNumberValue){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Levels, android.R.layout.simple_spinner_item);
        levelNameET.setText(levelNameValue);
        if(userLevelValue!=""){
            userLevelSpinner.setSelection(adapter.getPosition(userLevelValue));
        }
        //userLevelSpinner.setSelection();
        sequenceNumberET.setText(String.valueOf(sequenceNumberValue));

    }

    public void addLevel(View view) {

        levelNameValue = levelNameET.getText().toString();
        userLevelValue = userLevelSpinner.getSelectedItem().toString();
        sequenceNumberValue = Utility.getValue(sequenceNumberET.getText().toString(),0);

        //Validation - start
        if (levelNameValue.equalsIgnoreCase("")) {
            levelNameET.setError("Please enter level name");
            levelNameET.requestFocus();
            return;
        }
        if (userLevelValue.equalsIgnoreCase("---Select User Level---")) {
            ((TextView)userLevelSpinner.getSelectedView()).setError("Please select user level");
            userLevelSpinner.requestFocus();
            return;
        }
        if (sequenceNumberValue == 0) {
            sequenceNumberET.setError("Please enter sequence number");
            sequenceNumberET.requestFocus();
            return;
        }
        //validation - end

        new AddLevelAsyncTask(AddLevel.this, levelId,
                levelNameValue, userLevelValue, sequenceNumberValue).execute();
    }

    public void deleteLevelToDB(View view) {
        if(levelId!=0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Delete Level")
                    .setMessage("Do you really want to delete Level?")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Toast.makeText(AddLanguage.this, "Yaay", Toast.LENGTH_SHORT).show();
                            new DeleteLevelAsyncTask(AddLevel.this, levelId).execute();
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

    private class DeleteLevelAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long levelId;

        public DeleteLevelAsyncTask(Activity activity, long levelId){
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

            String methodName = "deleteLevel";
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

                    Toast toast = Toast.makeText(AddLevel.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(AddLevel.this, LevelList.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast.makeText(AddLevel.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddLevelAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long levelId;
        private String levelNameValue;
        private String userLevelValue;
        private int sequenceNumberValue;

        public AddLevelAsyncTask(Activity activity, long levelId,
                    String levelNameValue, String userLevelValue, int sequenceNumberValue){
            progressBar = new ProgressDialog(activity);
            this.levelId = levelId;
            this.levelNameValue = levelNameValue;
            this.userLevelValue = userLevelValue;
            this.sequenceNumberValue = sequenceNumberValue;

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

            String methodName = "addUpdateLevel";
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
                String urlParameters  = "levelId="+levelId+"&levelNameValue="+levelNameValue+
                        "&userLevelValue="+userLevelValue+"&sequenceNumberValue="+sequenceNumberValue;

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
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(AddLevel.this, AddLevel.this);
                    alertDialog.alertDialog("Network issue", "Due to maintenance, currently service unavailable");

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(AddLevel.this, AddLevel.this);
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

                    Toast toast = Toast.makeText(AddLevel.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(AddLevel.this, LevelList.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_411")) {
                        LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(AddLevel.this, AddLevel.this);
                        alertDialog.alertDialog("Duplicate", "Level with this sequence and user level already added. \n" + loginResponse.get("message").toString());
                    }
                    Toast toast = Toast.makeText(AddLevel.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
