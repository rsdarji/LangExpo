package com.langexpo.admin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Language;
import com.langexpo.model.Level;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Level> levelList;
    private List<Level> mDataFiltered;

    //getting the context and product list with constructor
    public LevelAdapter(Context mCtx, List<Level> levelList) {
        this.mCtx = mCtx;
        this.levelList = levelList;
        this.mDataFiltered = levelList;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.level_row_layout_admin, null);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LevelViewHolder holder, int position) {
        //getting the product of the specified position
        Level level = levelList.get(position);

        //binding the data with the viewholder views

        holder.levelName.setText(level.getLevelName());
        holder.userLevel.setText(level.getLevelType());
        holder.sequenceNumber.setText(String.valueOf(level.getSequenceNumber()));
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));

        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, AddLevel.class);
            intent.putExtra("levelId", level.getLevelId());
            intent.putExtra("levelName", level.getLevelName());
            intent.putExtra("userLevel", level.getLevelType());
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
        TextView userLevel;
        TextView sequenceNumber;

        public LevelViewHolder(View itemView) {
            super(itemView);

            levelName = itemView.findViewById(R.id.level_row_layout_admin_level_name);
            userLevel = itemView.findViewById(R.id.level_row_layout_admin_level_user_level);
            sequenceNumber = itemView.findViewById(R.id.level_row_layout_admin_level_sequence_number);
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
                levelList = (List<Level>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
