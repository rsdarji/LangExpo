package com.langexpo.utility;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.langexpo.com.langexpo.navigationdrawer.NavigationDrawer;

import java.util.UUID;

public class UploadImageToCloud {

    private static StorageReference mStorageRef;
    static Uri downloadUri;
    public static void uploadImage(Activity activity, byte[] byteArray){

        mStorageRef = FirebaseStorage.getInstance().getReference("temp/"+ UUID.randomUUID() +".png");
        UploadTask uploadTask = mStorageRef.putBytes(byteArray);

        uploadTask.addOnCompleteListener(activity, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.i("Upload task","Upload task complete...");
                //Log.d("url: ",mStorageRef.getDownloadUrl().getResult().toString());
            }
        });

        Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(
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

        );


        //return downloadUri.toString();
    }

    public static String sendImageURL(String url){
        return url;
    }
}
