package com.langexpo.admin.activity;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.langexpo.com.langexpo.navigationdrawer.NavigationDrawer;
import com.langexpo.R;
import com.langexpo.com.langexpo.navigationdrawer.FragmentHome;
import com.langexpo.fragments.AddLanguage;
import com.langexpo.utility.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Home extends NavigationDrawer {

    private StorageReference mStorageRef;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mStorageRef = FirebaseStorage.getInstance().getReference("temp/"+ UUID.randomUUID() +".png");

        /*Uri file = Uri.fromFile(new File("C:\\Users\\UTILISATEUR\\Desktop\\images\\flow\\7.2_contact-us.png"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });*/
        //setContentView(R.layout.fragment_home);
        super.onCreate(savedInstanceState);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                    new FragmentHome()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }




    public void addQuestion(View view) {

        FragmentHome.clickAddQuestion(view);
    }
    public void addQuestionType(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.add_question_type,
                new AddLanguage()).commit();

    }
    public void addLecture(View view){
        FragmentHome.clicAddLecture(view);
    }
    public void addLanguage(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                new AddLanguage()).commit();
        //FragmentHome.clicAddLanguage(view, f);
    }
    public void addGoal(View view){
        FragmentHome.clicAddGoal(view);
    }
    public void addLevel(View view){
        FragmentHome.clicAddLevel(view);
    }

    /*public void pickFromGallery(){

        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:
                    Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    System.out.println("uri : img : " + selectedImage);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    System.out.println("encoded: " + imgDecodableString);
                    Session s = new Session(getApplicationContext());
                    String imgPath = imgDecodableString;
                    s.set("imgPath", imgDecodableString);
                    Toast.makeText(getApplicationContext(), "3 : "+imgDecodableString, Toast.LENGTH_LONG).show();
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    System.out.println("bitmap : " + BitmapFactory.decodeFile(imgDecodableString));

                    break;

            }
    }*/

    private class addUpdateImage extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String image;
        private String encoadedImg;
        String imgStr;

        public addUpdateImage(Home activity, String image){
            progressBar = new ProgressDialog(activity);
            this.image = image;
            this.encoadedImg = encoadedImg;

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


            System.out.println("artid: background method: "+image);
            try {

                //path = se.get("encoadedImg");
                //artname artcategory artyear artprice artdescription path userid categoryid
                String urlParameters  = "image="+image;

                //imgStr = Base64.getEncoder().encodeToString(image);
                byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                int    postDataLength = postData.length;
                // create the HttpURLConnection
                url = new URL("http://"+ Constant.WEB_SERVICE_HOST +":"+Constant.WEB_SERVICE_PORT+"/WebApplication1/webresources/mobile/addprofileimage");

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

                int status =connection.getResponseCode();
                if(status<400){
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    //read response
                }else{
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    //read response
                }
                //reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println(stringBuilder.toString());
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

                    /*Intent intent = new Intent(UploadNewArt.this, Artistchoice.class);
                    startActivity(intent);*/
                    Toast.makeText(getApplicationContext(),"Your profile picture has been changed successfully.",Toast.LENGTH_LONG).show();
                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Sorry! Please try again later...",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
