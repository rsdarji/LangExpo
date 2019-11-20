package com.langexpo.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContentResolverCompat;

import com.langexpo.R;
import com.langexpo.model.Question;
import com.langexpo.model.QuestionModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public static Object[][] shuffleArrayValues(Object[][] options ){
        List<Object> strList = Arrays.asList(options);
        Collections.shuffle(strList);
        options = strList.toArray(new String[strList.size()][]);

        return options;
    }

    public static Bitmap resizeBitmap(Bitmap x, int newWidth, int newHeight){
        int width = x.getWidth();
        int height = x.getHeight();

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);


        return Bitmap.createBitmap(x, 0, 0, width,
                height, matrix, true);
    }

    public static List<QuestionModel> shuffleAndReduceQuestionToTen(List<QuestionModel> questionList){

        Collections.shuffle(questionList, new Random());
        if(questionList.size()>10){
            questionList = questionList.stream().limit(10).collect(Collectors.toList());
        }
        return questionList;
    }
}
