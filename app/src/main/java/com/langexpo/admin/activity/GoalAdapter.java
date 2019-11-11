package com.langexpo.admin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Goal;
import com.langexpo.model.Language;
import com.langexpo.model.Lecture;
import com.langexpo.model.Question;

import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Goal> GoalList;

    //getting the context and product list with constructor
    public GoalAdapter(Context mCtx, List<Goal> GoalList) {
        this.mCtx = mCtx;
        this.GoalList = GoalList;
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
        Goal goal = GoalList.get(position);

        //binding the data with the viewholder views

        holder.GoalName.setText(goal.getGoalName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));



        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, Goal.class);
            intent.putExtra("goalId", goal.getGoalId());
            intent.putExtra("goalName",goal.getGoalName());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return GoalList.size();
    }

    class GoalViewHolder extends RecyclerView.ViewHolder {

        TextView GoalName;

        public GoalViewHolder(View itemView) {
            super(itemView);

            GoalName = itemView.findViewById(R.id.goal_row_layout_admin_goal3f_name);

        }
    }
}
