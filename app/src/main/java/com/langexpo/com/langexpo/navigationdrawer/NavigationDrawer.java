package com.langexpo.com.langexpo.navigationdrawer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.langexpo.R;
import com.langexpo.activity.ContactUs;
import com.langexpo.activity.Dictionary;
import com.langexpo.activity.FavoriteList;
import com.langexpo.activity.Feedback;
import com.langexpo.activity.MainActivity;
import com.langexpo.activity.ManageProfile;
import com.langexpo.admin.activity.AddLanguage;
import com.langexpo.admin.activity.LanguageList;
import com.langexpo.fragments.FragmentUserHome;
import com.langexpo.utility.Constant;
import com.langexpo.utility.ImagePickerActivity;
import com.langexpo.utility.Session;
import com.langexpo.utility.SetURLImageToImageview;
import com.langexpo.utility.UploadImageToCloud;
import com.langexpo.utility.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public static final int REQUEST_IMAGE = 100;
    @BindView(R.id.nav_avtar)
    ImageView nav_avtar;
    String imageFileName;
    int imageId;
    TextView userName, email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("nav_drawer", "navigation drawer");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        /*
        -----To Hide menu in Navigation Drawer menu-----
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        if(true){
            menu.removeItem(R.id.nav_feedback);
        }*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this);


        NavigationView nav = (NavigationView) findViewById(R.id.nav_view1);
        View header = nav.getHeaderView(0);
        nav_avtar = (ImageView) header.findViewById(R.id.nav_avtar);
        userName = (TextView) header.findViewById(R.id.nav_user_name);
        email = (TextView) header.findViewById(R.id.nav_user_email);
        if(!Session.get(Constant.User.AVTAR).equalsIgnoreCase("")){
            Picasso.get()
                    .load(Session.get(Constant.User.AVTAR))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(nav_avtar);
        }
        userName.setText(Session.get(Constant.User.FIRST_NAME)+" "+Session.get(Constant.User.LAST_NAME));
        email.setText(Session.get(Constant.User.EMAIL));

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home:
                if(Session.get(Constant.User.ROLE).equalsIgnoreCase("1")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                            new FragmentUserHome()).commit();

                }else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                            new FragmentHome()).commit();
                }
                item.setChecked(true);
                break;
            case R.id.nav_manage_profile:
                intent = new Intent(NavigationDrawer.this, ManageProfile.class);
                startActivity(intent);
                item.setChecked(true);
                break;
            /*case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            *//*case R.id.nav_progress:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_achievement:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            */case R.id.nav_Dictionary:
                intent = new Intent(NavigationDrawer.this, Dictionary.class);
                startActivity(intent);
                break;
            case R.id.nav_favorite:
                intent = new Intent(NavigationDrawer.this, FavoriteList.class);
                startActivity(intent);
                break;

            case R.id.nav_contact_us:
                intent = new Intent(NavigationDrawer.this, ContactUs.class);
                startActivity(intent);
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();*/
                break;
            case R.id.nav_feedback:
                Intent i = new Intent(NavigationDrawer.this, Feedback.class);
                startActivity(i);

                break;
            case R.id.nav_share:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "LangExpo\n\"https://play.google.com/store/apps/\"";
                String sub = "Lang Expo";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();*/
                break;
            case R.id.nav_deactivate_account:
               /* getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();*/

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                        .setTitle("Deactivate Account?")
                        .setMessage("Do you really want to deactivate your Account?")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(AddLanguage.this, "Yaay", Toast.LENGTH_SHORT).show();
                                new DeactivateAccountAsyncTask(NavigationDrawer.this,
                                        Long.parseLong(Session.get(Constant.User.USER_ID))).execute();
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(AddLanguage.this, "no", Toast.LENGTH_SHORT).show();
                            }

                        });
                builder.show();

                break;
            case R.id.nav_sign_out:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();*/
                intent = new Intent(NavigationDrawer.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Session session = new Session(getApplicationContext());
                session.clear();
                break;
            /*case R.id.nav_sign_up:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;*/
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void changeAvtar(View view) {
        /*Toast toast = Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG);
        toast.show();*/
        imageId = R.id.nav_avtar;
        imageFileName = Utility.createProfileImageName(String.valueOf(Session.get(Constant.User.PHONE)));
        checkPermission();

    }

    /*public void changeLanguageFlag(View view){
        imageId = R.id.img_language_logo;
        UUID uuid = UUID.randomUUID();
        imageFileName = "languagelogo"+uuid.toString();
        checkPermission();
    }*/



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
        Intent intent = new Intent(NavigationDrawer.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(NavigationDrawer.this, ImagePickerActivity.class);
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
                    byte[] byteArray = stream.toByteArray();

                    String url = UploadImageToCloud.uploadImage(NavigationDrawer.this, byteArray, Constant.IMAGE_FOLDER,
                            imageFileName, Constant.PNG);
                    Session.set(Constant.UPLOADED_ITEM_URL,"");
                    Session.set(Constant.UPLOADED_ITEM_URL,url);
                    Log.d("method return url: ", url);

                    /*if(Session.get(Constant.User.AVTAR).equalsIgnoreCase("")){*/
                        new updateUserProfileImage(NavigationDrawer.this,
                                Long.parseLong(Session.get(Constant.User.USER_ID)),
                                url).execute();
                    /*}*/

                    // loading profile image from local cache
                    ((ImageView) findViewById(imageId)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

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

    //start code for update user's profile image
    private class updateUserProfileImage extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long userId;
        private String profileImageURL;

        public updateUserProfileImage(Activity activity,
                                      long userId, String profileImageURL){
            progressBar = new ProgressDialog(activity);
            this.userId = userId;
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

            String methodName = "updateProfileImage";
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

                String urlParameters  = "userId="+userId+"&profileImageURL="+profileImageURL;


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
            System.out.println("updateProfileImage: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    //UploadImageToCloud.deleteStorageFile(Session.get(Constant.UPLOADED_ITEM_URL));
                    Session.set(Constant.User.AVTAR,profileImageURL);
                    Toast toast = Toast.makeText(NavigationDrawer.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast.makeText(NavigationDrawer.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //end code for update user's profile image

    //start code for deactivate account assync task
    private class DeactivateAccountAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long userId;

        public DeactivateAccountAsyncTask(Activity activity, long userId){
            progressBar = new ProgressDialog(activity);
            this.userId = userId;
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

            String methodName = "deactivateAccount";
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
            System.out.println("deactivateAccount: "+result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    //UploadImageToCloud.deleteStorageFile(Session.get(Constant.UPLOADED_ITEM_URL));

                    Toast toast = Toast.makeText(NavigationDrawer.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    Intent intent = new Intent(NavigationDrawer.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    Toast toast = Toast.makeText(NavigationDrawer.this,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //end code for deactivate account assync task
}
