package com.langexpo.admin.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.langexpo.R;
import com.langexpo.activity.MainActivity;
import com.langexpo.com.langexpo.navigationdrawer.FragmentHome;
import com.langexpo.utility.Constant;
import com.langexpo.utility.ImagePickerActivity;
import com.langexpo.utility.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private static final String TAG = Home.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    @BindView(R.id.nav_avtar)
    ImageView nav_avtar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentHome()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        /*nav_avtar = findViewById(R.id.nav_avtar);

        nav_avtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.pickFromGallery(Home.this);
            }
        });*/

        // Clearing older images from cache directory

        // don't call this line if you want to choose multiple images in the same activity

        // call this once the bitmap(s) usage is over

        ImagePickerActivity.clearCache(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_manage_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_progress:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_achievement:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_Dictionary:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_contact_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_deactivate_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_sign_out:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();*/
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
                Session session = new Session(getApplicationContext());
                session.clear();
                break;
            case R.id.nav_sign_up:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                break;
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



    public void changeAvtar(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG);
        toast.show();

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


        //pickFromGallery();
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

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(Home.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(Home.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    String a = stream.toString();
                    String ab = Base64.getEncoder().encodeToString( a.getBytes( "utf-8" ) );
                    Log.d("encoded img: ",ab);
                    byte[] byteArray = stream.toByteArray();
                    //new addUpdateImage(Home.this, ab).execute();
                    // loading profile image from local cache
                    ((ImageView)findViewById(R.id.nav_avtar)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.length));

                    bitmap.recycle();
                    //loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.getEncoder().encodeToString(byteArrayOS.toByteArray(), Base64.classEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }*/

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
        nav_avtar.setImageBitmap(BitmapFactory.decodeFile(url));
        //nav_avtar.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    public void addQuestion(View view) {

        FragmentHome.clickAddQuestion(view);
    }
    public void addQuestionType(View view){
        FragmentHome.clicAddQuestionType(view);
    }
    public void addLecture(View view){
        FragmentHome.clicAddLecture(view);
    }
    public void addLanguage(View view){
        FragmentHome.clicAddLanguage(view);
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
