package com.langexpo.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UploadImageToCloud {

    private static StorageReference mStorageRef;
    private
    static Uri downloadUri;

    public static String uploadImage(Activity activity, byte[] byteArray, String folder, String fileName, String contentType) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.getReference().child("temp").getDownloadUrl();
        mStorageRef = firebaseStorage.getReference(folder + "/" + fileName + "." + contentType);

        Log.d("reference", mStorageRef.getPath());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://firebasestorage.googleapis.com/v0/b/");
        stringBuilder.append(mStorageRef.getBucket());
        stringBuilder.append("/o/");
        stringBuilder.append(folder);
        stringBuilder.append("%2F");
        try {
            stringBuilder.append(URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringBuilder.append(".");
        stringBuilder.append(contentType);
        stringBuilder.append("?alt=media");

        UploadTask uploadTask = mStorageRef.putBytes(byteArray);

        ProgressDialog progressBar = new ProgressDialog(activity);
        progressBar.setMessage("Loading...");
        progressBar.show();
        uploadTask.addOnProgressListener(activity, new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int currentProgress = (int) progress;
                progressBar.setProgress(currentProgress);

            }

        });
        uploadTask.addOnCompleteListener(activity, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.i("Upload task", "Upload task complete...");
                progressBar.dismiss();
            }
        });

        // To download
        /*Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(
                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return mStorageRef.getDownloadUrl();
                    }
                }
        );

        getDownloadUriTask.addOnCompleteListener(activity, new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        downloadUri = task.getResult();
                        Log.d("url: ",downloadUri.toString());
                    }
                }
            }
        );*/

        return stringBuilder.toString();
    }

}
