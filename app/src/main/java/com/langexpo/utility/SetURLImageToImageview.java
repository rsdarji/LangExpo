package com.langexpo.utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.langexpo.R;

import java.io.InputStream;



public class SetURLImageToImageview extends AsyncTask<String, Void, Bitmap> {
    int imageView;
    Activity activity;

    public SetURLImageToImageview(Activity activity, int imageView) {
        this.imageView = imageView;
        this.activity = activity;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        //bmImage = (ImageView)activity.findViewById(R.id.nav_avtar);
        ((ImageView)activity.findViewById(imageView)).setImageBitmap(result);

    }
}

