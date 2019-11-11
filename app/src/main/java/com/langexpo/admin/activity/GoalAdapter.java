package com.langexpo.admin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Goal;
import com.langexpo.model.Language;
import com.langexpo.model.Lecture;
import com.langexpo.model.Level;
import com.langexpo.model.Question;

import java.util.ArrayList;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Goal> goalList;
    private List<Goal> mDataFiltered;

    //getting the context and product list with constructor
    public GoalAdapter(Context mCtx, List<Goal> goalList) {
        this.mCtx = mCtx;
        this.goalList = goalList;
        this.mDataFiltered = goalList;
    }

    @Override
    public GoalAdapter.GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.goal_row_layout_admin, null);
        return new GoalAdapter.GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoalAdapter.GoalViewHolder holder, int position) {
        //getting the product of the specified position
        Goal goal = goalList.get(position);

        //binding the data with the viewholder views

        holder.goalName.setText(goal.getGoalName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));



        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, AddGoal.class);
            intent.putExtra("goalId", goal.getGoalId());
            intent.putExtra("goalName",goal.getGoalName());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return goalList.size();
    }

    class GoalViewHolder extends RecyclerView.ViewHolder {

        TextView goalName;

        public GoalViewHolder(View itemView) {
            super(itemView);

            goalName = itemView.findViewById(R.id.goal_row_layout_admin_goal3f_name);

        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = goalList ;
                }
                else {
                    List<Goal> lstFiltered = new ArrayList<>();
                    for (Goal row : goalList) {
                        if (row.getGoalName().toLowerCase().contains(Key.toLowerCase())){
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
                goalList = (List<Goal>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
