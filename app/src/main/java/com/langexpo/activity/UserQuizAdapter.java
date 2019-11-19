package com.langexpo.activity;

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
import com.langexpo.model.Level;
import com.langexpo.model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class UserQuizAdapter extends RecyclerView.Adapter<UserQuizAdapter.QuizViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Quiz> quizList;
    private List<Quiz> mDataFiltered;

    //getting the context and product list with constructor
    public UserQuizAdapter(Context mCtx, List<Quiz> quizList) {
        this.mCtx = mCtx;
        this.quizList = quizList;
        this.mDataFiltered = quizList;
    }

    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_quiz_row_layout, null);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int position) {
        //getting the product of the specified position
        Quiz quiz = quizList.get(position);

        //binding the data with the viewholder views

        holder.quizName.setText(quiz.getQuizName());

        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, DisplayQuizQuestions.class);
            intent.putExtra("quizId", quiz.getQuizId());
            intent.putExtra("quizName", quiz.getQuizName());
            intent.putExtra("userLevel", quiz.getUserLevel());
            intent.putExtra("levelIds", quiz.getLevelIds());
            mCtx.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView quizName;

        public QuizViewHolder(View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quiz_row_layout_user_quiz_name);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = quizList ;
                }
                else {
                    List<Quiz> lstFiltered = new ArrayList<>();
                    for (Quiz row : quizList) {
                        if (row.getQuizName().toLowerCase().contains(Key.toLowerCase())){
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

                quizList = (List<Quiz>) results.values;
                notifyDataSetChanged();
                /*levelList = levelList1;*/
            }
        };
    }
}
