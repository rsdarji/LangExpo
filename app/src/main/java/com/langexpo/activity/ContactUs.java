package com.langexpo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.langexpo.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class ContactUs extends AppCompatActivity {
    private int[] mImages = new int[]{
            R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5,R.drawable.image6,R.drawable.image7,R.drawable.image8
    };
    private String[] mImagesTitle= new String[]
            {
                    "mithil","mithil","mithil","mithil","mithil"

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        CarouselView carouselView=(findViewById(R.id.carousel1));
        carouselView.setPageCount(mImages.length);
        carouselView.setImageListener(new ImageListener() {


            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);
            }
        });
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(ContactUs.this,mImagesTitle[position],Toast.LENGTH_SHORT).show();
            }
        });
    }
}
