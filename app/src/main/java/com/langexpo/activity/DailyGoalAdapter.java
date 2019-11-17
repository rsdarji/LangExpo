package com.langexpo.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Goal;
import com.langexpo.model.Language;
import com.langexpo.utility.Constant;
import com.langexpo.utility.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailyGoalAdapter extends RecyclerView.Adapter<DailyGoalAdapter.DailGoalViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the language in a list
    private List<Goal> goalList;
    public static RadioButton lastCheckedRB = null;
    private String lastCheckedRBText;

    Set<String> userSelectedGoals = new HashSet<>();
    //getting the context and product list with constructor
    public DailyGoalAdapter(Context mCtx, List<Goal> goalList) {
        this.mCtx = mCtx;
        this.goalList = goalList;
    }

    @Override
    public DailGoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.select_goal_row_layout, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new DailGoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DailGoalViewHolder holder, int position) {
        //getting the product of the specified position
        Goal goal = goalList.get(position);
        //binding the data with the viewholder views
        holder.dailyGoalChbx.setText(goal.getGoalName());

        if(Boolean.parseBoolean(Session.get(holder.dailyGoalChbx.getText().toString()))){
            holder.dailyGoalChbx.setChecked(true);
        }

        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));
        /*Picasso.get()
                .load(language.getLanguageFlagURL())
                .into(holder.languageFlag);*/

        //if true, your checkbox will be selected, else unselected
        //holder.dailyGoalChbx.setChecked();
        holder.dailyGoalChbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.dailyGoalChbx.setChecked(isChecked);
                Session.set(holder.dailyGoalChbx.getText().toString(),String.valueOf(isChecked));
                if(isChecked) {
                    DailyGoal.userSelectedGoals.add(holder.dailyGoalChbx.getText().toString());
                }else{
                    DailyGoal.userSelectedGoals.remove(holder.dailyGoalChbx.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    class DailGoalViewHolder extends RecyclerView.ViewHolder {

        CheckBox dailyGoalChbx;

        public DailGoalViewHolder(View itemView) {
            super(itemView);
            dailyGoalChbx = itemView.findViewById(R.id.daily_goal_chbx);

        }
    }
}
