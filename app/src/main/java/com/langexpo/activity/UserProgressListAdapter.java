package com.langexpo.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Goal;
import com.langexpo.model.Lecture;
import com.langexpo.model.UserProgress;
import com.langexpo.utility.Session;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserProgressListAdapter extends RecyclerView.Adapter<UserProgressListAdapter.UserProgressViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the language in a list
    private List<UserProgress> userProgressList;
    private List<UserProgress> mDataFiltered;

    //getting the context and product list with constructor
    public UserProgressListAdapter(Context mCtx, List<UserProgress> userProgressList) {
        this.mCtx = mCtx;
        this.userProgressList = userProgressList;
        this.mDataFiltered = userProgressList;
    }

    @Override
    public UserProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_progress_list_row_layout_admin, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new UserProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserProgressViewHolder holder, int position) {
        //getting the product of the specified position
        UserProgress userProgress = userProgressList.get(position);
        //binding the data with the viewholder views
        holder.name.setText(userProgress.getName());
        holder.status.setText(userProgress.getStatus());
        holder.attempt.setText(String.valueOf(userProgress.getAttempt()));
        float correctAnswerCount = userProgress.getCorrectAnswerCount();
        float inCorrectAnswerCount = userProgress.getInCorrectAnswerCount();
        float totalQuestion = correctAnswerCount+inCorrectAnswerCount;
        float percentageScore = (correctAnswerCount/totalQuestion)*100;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        holder.percentageScore.setText(String.valueOf( df.format(percentageScore))+"%");
        if(percentageScore>60.0){
            holder.percentageScore.setTextColor(mCtx.getResources().getColor(R.color.colorGreenDark));
        }

    }

    @Override
    public int getItemCount() {
        return userProgressList.size();
    }

    class UserProgressViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView status;
        TextView attempt;
        TextView percentageScore;

        public UserProgressViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.user_progress_level_or_quiz_name);
            status = itemView.findViewById(R.id.user_progress_level_or_quiz_status);
            attempt = itemView.findViewById(R.id.user_progress_level_or_quiz_attempt);
            percentageScore = itemView.findViewById(R.id.user_progress_level_or_quiz_score);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = userProgressList ;
                }
                else {
                    List<UserProgress> lstFiltered = new ArrayList<>();
                    for (UserProgress row : userProgressList) {
                        if (row.getName().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                        if (row.getStatus().toLowerCase().contains(Key.toLowerCase())){
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
                userProgressList = (List<UserProgress>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
