package com.langexpo.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Language;
import com.langexpo.utility.SetURLImageToImageview;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Language> languageList;

    //getting the context and product list with constructor
    public LanguageAdapter(Context mCtx, List<Language> languageList) {
        this.mCtx = mCtx;
        this.languageList = languageList;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.language_row_layout_admin, null);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        //getting the product of the specified position
        Language language = languageList.get(position);

        //binding the data with the viewholder views

        holder.languageName.setText(language.getLanguageName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));
        Picasso.get()
                .load(language.getLanguageFlagURL())
                .into(holder.languageFlag);

        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, AddLanguage.class);
            intent.putExtra("languageId", language.getLanguageId());
            intent.putExtra("languageName", language.getLanguageName());
            intent.putExtra("languageFlagURL", language.getLanguageFlagURL());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return languageList.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        TextView languageName;
        ImageView languageFlag;

        public LanguageViewHolder(View itemView) {
            super(itemView);

            languageName = itemView.findViewById(R.id.language_row_layout_admin_language_name);
            languageFlag = itemView.findViewById(R.id.language_row_layout_admin_language_logo);
        }
    }
}
