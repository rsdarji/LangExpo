package com.langexpo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.langexpo.R;

import java.io.IOException;
import java.io.InputStream;

public class Dictionary extends AppCompatActivity {
    private EditText inputToTranslate;
    private TextView translatedTv;
    private String originalText;
    private String translatedText;
    private boolean connected;
    Translate translate;

    private ImageView favourite,unfavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        inputToTranslate = findViewById(R.id.editText_dictionary_search);
        translatedTv = findViewById(R.id.textView_result);
        Button translateButton = findViewById(R.id.button_translate);
        //favourite=findViewById(R.id.imageView_favourite);
        unfavourite=findViewById(R.id.imageView_unfavourite);
        unfavourite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dictionary.this, "click listner", Toast.LENGTH_LONG).show();
                if(unfavourite.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.ic_favorite).getConstantState()){
                    unfavourite.setImageResource(R.drawable.ic_favorite_filled);
                }else{
                    unfavourite.setImageResource(R.drawable.ic_favorite);
                }
            }

        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (checkInternetConnection()) {

                    //If there is internet connection, get translate service and start translation:
                    getTranslateService();
                    translate();

                } else {

                    //If not, display "no connection" warning:
                    translatedTv.setText(getResources().getString(R.string.no_connection));
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    public void translate() {

        //Get input text to be translated:
        originalText = inputToTranslate.getText().toString();
        Translation translation = translate.translate(originalText, com.google.cloud.translate.Translate.TranslateOption.targetLanguage("fr"), com.google.cloud.translate.Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        translatedTv.setText(translatedText);

    }

    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

}

