package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.langexpo.R;

public class DisplayLecture extends AppCompatActivity {

    Toolbar myToolbar;
    TextView displayLectureTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lecture);

        myToolbar = (Toolbar) findViewById(R.id.admin_display_lecture_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        displayLectureTV = (TextView) findViewById(R.id.admin_display_lecture_content_preview);

        getIncomingIntent();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getIncomingIntent(){
        if(getIntent().hasExtra("lectureContent")) {
            String s = getIntent().getStringExtra("lectureContent");
            if (s.length() != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    displayLectureTV.setText(Html.fromHtml(s.toString(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    displayLectureTV.setText(Html.fromHtml(s.toString()));
                }
            }
        }
    }
}
