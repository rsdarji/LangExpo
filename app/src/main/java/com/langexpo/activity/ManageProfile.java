package com.langexpo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.langexpo.R;
import com.langexpo.admin.activity.Home;
import com.langexpo.utility.Constant;
import com.langexpo.utility.ImagePickerActivity;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;
import com.langexpo.utility.UploadImageToCloud;
import com.langexpo.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

public class ManageProfile extends AppCompatActivity {
    ImageView profileImg;
    EditText firstNameET, lastNameET, emailET, phoneET, passwordET, confirmPasswordET;
    TextView profileImgTV;
    Button updateBT, skipBT;
    String firstName, lastName, email, oldEmail, profileImageFileName, oldProfileImageFileName, url, phone, oldPhone;
    boolean isUserHaveProfileImage = false;
    Toolbar myToolbar;
    int profileImgId;
    byte[] byteArray;
    public static final int REQUEST_IMAGE = 100;
    long userId = Utility.getValue(Session.get(Constant.User.USER_ID), 0) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        myToolbar = (Toolbar) findViewById(R.id.user_manage_profile_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*profileImgTV = (TextView) findViewById(R.id.profile_img_tv);*/
        firstNameET = (EditText) findViewById(R.id.profile_firstname_et);
        lastNameET = (EditText) findViewById(R.id.profile_lastname_et);
        emailET = (EditText) findViewById(R.id.profile_email_et);
        phoneET = (EditText) findViewById(R.id.profile_phone_et);
        passwordET = (EditText) findViewById(R.id.profile_password_et);
        confirmPasswordET = (EditText) findViewById(R.id.profile_confirm_password_et);
        /*profileImgId = R.id.img_create_profile_img;*/
        updateBT = (Button)findViewById(R.id.profile_update_bt);


        firstNameET.setText(Session.get(Constant.User.FIRST_NAME));
        lastNameET.setText(Session.get(Constant.User.LAST_NAME));
        emailET.setText(Session.get(Constant.User.EMAIL));
        phoneET.setText(Session.get(Constant.User.PHONE));
        /*Picasso.get()
                .load(Session.get(Constant.User.AVTAR))
                .into((ImageView) findViewById(profileImgId));
        if(!Session.get(Constant.User.AVTAR).equalsIgnoreCase("")){
            isUserHaveProfileImage = true;
        }*/


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void update(View view) {
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        email = emailET.getText().toString();
        oldEmail = Session.get(Constant.User.EMAIL);
        phone = phoneET.getText().toString();
        oldPhone = Session.get(Constant.User.PHONE);

        //validation start

        if(firstName.equalsIgnoreCase("")){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
            alertDialog.alertDialog("Error", "Please enter first name.");
            firstNameET.requestFocus();
            return;
        }
        if(lastName.equalsIgnoreCase("")){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
            alertDialog.alertDialog("Error", "Please enter last name.");
            lastNameET.requestFocus();
            return;
        }
        if(email.equalsIgnoreCase("") || !email.contains("@")){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
            alertDialog.alertDialog("Error", "Please enter correct email.");
            emailET.requestFocus();
            return;
        }
        if(phone.equalsIgnoreCase("") || String.valueOf(phone).length()<10){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
            alertDialog.alertDialog("Error", "Phone number should not empty or less than 10 digit.");
            phoneET.requestFocus();
            return;
        }

        /*if(!password.trim().equalsIgnoreCase(confirmPassword.trim())){
            LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
            alertDialog.alertDialog("Error", "password and confirm password must be same.");
            confirmPasswordET.requestFocus();
            return;
        }*/

        //validation end

        /*if(byteArray.length!=0) {
            profileImageFileName = Utility.createProfileImageName(phone);
            oldProfileImageFileName = Utility.createProfileImageName(oldPhone);
            url = UploadImageToCloud.uploadImage(ManageProfile.this, byteArray, Constant.IMAGE_FOLDER,
                    profileImageFileName, Constant.PNG);
        }*/

        new ManageProfileAsyncTask(ManageProfile.this, firstName, lastName, email,
                phone).execute();
    }

    /*public void changeProfileImg(View view){
        //UUID uuid = UUID.randomUUID();
        //imageFileName = "languageflag"+uuid.toString();
        checkPermission();
    }*/

    private class ManageProfileAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;

        private String profileImageURL;

        public ManageProfileAsyncTask(Activity activity, String firstName, String lastName,
                                      String email, String phone){
            progressBar = new ProgressDialog(activity);
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;

            this.profileImageURL = profileImageURL;
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

            String methodName = "updateProfile";
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

                String urlParameters = "userId="+userId+"&firstName="+firstName+"&lastName="+lastName+
                        "&email="+email+"&phone="+phone;

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
                if(e instanceof ConnectException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
                    alertDialog.alertDialog("Network issue", Constant.NO_INTERNET_ERROR_MESSAGE);

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
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
            System.out.println("updateProfile: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {


                    /*if(oldLanguageFlagURL!=null) {
                        if (!oldLanguageFlagURL.equalsIgnoreCase(languageFlagURL)) {
                            UploadImageToCloud.deleteStorageFile(oldLanguageFlagURL);
                        }
                    }*/
                    Toast toast = Toast.makeText(com.langexpo.activity.ManageProfile.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Session.set(Constant.User.USER_NAME, firstName+"_"+lastName);
                    Session.set(Constant.User.EMAIL, email);
                    Session.set(Constant.User.FIRST_NAME,firstName);
                    Session.set(Constant.User.LAST_NAME,lastName);
                    Session.set(Constant.User.PHONE,phone);
                    Intent intent = new Intent(ManageProfile.this, Home.class);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_PHONE_EMAIL")) {
                        LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(ManageProfile.this, ManageProfile.this);
                        alertDialog.alertDialog("Duplicate", loginResponse.get("message").toString());
                    }

                    Toast toast = Toast.makeText(ManageProfile.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);

                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * START - Image (Crop, camera, gallery)
     */
    public void checkPermission(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ManageProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ManageProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    String a = stream.toString();
                    String ab = Base64.getEncoder().encodeToString(a.getBytes("utf-8"));
                    Log.d("encoded img: ", ab);
                    byteArray = stream.toByteArray();

                    /*String url = UploadImageToCloud.uploadImage(CreateProfile.this, byteArray, Constant.IMAGE_FOLDER,
                            imageFileName, Constant.PNG);*/
                    /*Session.set(Constant.UPLOADED_ITEM_URL,"");
                    Session.set(Constant.UPLOADED_ITEM_URL,url);
                    Log.d("method return url: ", url);*/

                    // loading profile image from local cache
                    ((ImageView) findViewById(profileImgId)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

                    bitmap.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * END - Image (Crop, camera, gallery)
     */
}
