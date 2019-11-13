package com.langexpo.com.langexpo.navigationdrawer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.langexpo.activity.Feedback;
import com.langexpo.activity.MainActivity;
import com.langexpo.utility.Constant;
import com.langexpo.utility.ImagePickerActivity;
import com.langexpo.utility.Session;
import com.langexpo.utility.SetURLImageToImageview;
import com.langexpo.utility.UploadImageToCloud;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

        /*new SetURLImageToImageview(NavigationDrawer.this, R.id.nav_avtar)
                .execute("https://firebasestorage.googleapis.com/v0/b/langexpo.appspot.com/o/temp%2F1fd3fcba-eb93-46fa-976c-7b43b9c72cfd.png?alt=media&token=5c4fb7d9-88f8-484d-9ca8-1cbe38718ef9");
*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                System.out.println("hello home");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_manage_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_progress:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_achievement:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_Dictionary:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_contact_us:
                Intent intent = new Intent(NavigationDrawer.this, ContactUs.class);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_sign_out:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();*/
                intent = new Intent(NavigationDrawer.this, MainActivity.class);
                startActivity(intent);
                Session session = new Session(getApplicationContext());
                session.clear();
                break;
            case R.id.nav_sign_up:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void changeAvtar(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG);
        toast.show();
        imageId = R.id.nav_avtar;
        imageFileName = Session.get(Constant.User.USER_ID);
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
}
