package com.langexpo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.admin.activity.AddLevel;
import com.langexpo.model.Level;

import java.util.ArrayList;
import java.util.List;

public class UserLevelAdapter extends RecyclerView.Adapter<UserLevelAdapter.LevelViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Level> levelList;
    private List<Level> mDataFiltered;

    //getting the context and product list with constructor
    public UserLevelAdapter(Context mCtx, List<Level> levelList) {
        this.mCtx = mCtx;
        this.levelList = levelList;
        this.mDataFiltered = levelList;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_level_row_layout_admin, null);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LevelViewHolder holder, int position) {
        //getting the product of the specified position
        Level level = levelList.get(position);

        //binding the data with the viewholder views

        holder.levelName.setText(level.getLevelName());
        holder.languageName.setText(level.getLanguageName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));

        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, DisplayLevelQuestions.class);
            intent.putExtra("levelId", level.getLevelId());
            intent.putExtra("levelName", level.getLevelName());
            intent.putExtra("userLevel", level.getLevelType());
            intent.putExtra("languageId", level.getLanguageId());
            intent.putExtra("languageName", level.getLanguageName());
            intent.putExtra("sequenceNumber", level.getSequenceNumber());
            mCtx.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return levelList.size();
    }

    class LevelViewHolder extends RecyclerView.ViewHolder {

        TextView levelName;
        TextView languageName;

        public LevelViewHolder(View itemView) {
            super(itemView);

            levelName = itemView.findViewById(R.id.level_row_layout_user_level_name);
            languageName = itemView.findViewById(R.id.level_row_layout_user_level_language);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = levelList ;
                }
                else {
                    List<Level> lstFiltered = new ArrayList<>();
                    for (Level row : levelList) {
                        if (row.getLevelName().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }if (row.getLanguageName().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //levelList.clear();
                List<Level> levelList1 = levelList;
                levelList = (List<Level>) results.values;
                notifyDataSetChanged();
                /*levelList = levelList1;*/
            }
        };
    }
}
