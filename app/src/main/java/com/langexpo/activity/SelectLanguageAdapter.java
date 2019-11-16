package com.langexpo.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.admin.activity.AddLanguage;
import com.langexpo.model.Language;
import com.langexpo.utility.Constant;
import com.langexpo.utility.Session;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectLanguageAdapter extends RecyclerView.Adapter<SelectLanguageAdapter.LanguageViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the language in a list
    private List<Language> languageList;
    public static RadioButton lastCheckedRB = null;
    private String lastCheckedRBText;
    //getting the context and product list with constructor
    public SelectLanguageAdapter(Context mCtx, List<Language> languageList) {
        this.mCtx = mCtx;
        this.languageList = languageList;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.select_language_row_layout, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        //getting the product of the specified position
        Language language = languageList.get(position);



        //binding the data with the viewholder views
        holder.languageRadioButton.setText(language.getLanguageName());

        if(holder.languageRadioButton.getText().toString().equalsIgnoreCase(Session.get(Constant.Session.USER_SELECTED_LANGUAGE))){
            holder.languageRadioButton.setChecked(true);
        }
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));
        /*Picasso.get()
                .load(language.getLanguageFlagURL())
                .into(holder.languageFlag);*/


        holder.languageRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked_rb = holder.languageRadioButton;
                String checked_text = checked_rb.getText().toString();
                Session.set(Constant.Session.USER_SELECTED_LANGUAGE,checked_rb.getText().toString());
                if(lastCheckedRB != null && !checked_text.equalsIgnoreCase(lastCheckedRBText)){
                    lastCheckedRB.setChecked(false);
                }
                lastCheckedRB = checked_rb;
                lastCheckedRBText = checked_text;
            }
        });
        /*holder.languageRadioButton.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, AddLanguage.class);
            intent.putExtra("languageId", language.getLanguageId());
            intent.putExtra("languageName", language.getLanguageName());
            intent.putExtra("languageFlagURL", language.getLanguageFlagURL());
            mCtx.startActivity(intent);
        });*/

    }


    @Override
    public int getItemCount() {
        return languageList.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        RadioButton languageRadioButton;

        public LanguageViewHolder(View itemView) {
            super(itemView);
            languageRadioButton = itemView.findViewById(R.id.select_language_radio_button);

        }
    }
}
