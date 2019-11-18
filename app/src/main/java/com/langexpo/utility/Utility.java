package com.langexpo.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContentResolverCompat;

import com.langexpo.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utility {
    public static int getValue(String value, int defaultValue){
        if(!value.equalsIgnoreCase("")){
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public static Object[] shuffleArrayValues(Object[] options ){
        List<Object> strList = Arrays.asList(options);
        Collections.shuffle(strList);
        options = strList.toArray(new String[strList.size()]);

        return options;
    }
}
