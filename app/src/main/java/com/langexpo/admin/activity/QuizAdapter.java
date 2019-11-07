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
import com.langexpo.model.Quiz;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Quiz> QuizList;

    //getting the context and product list with constructor
    public QuizAdapter(Context mCtx, List<Goal> GoalList) {
        this.mCtx = mCtx;
        this.QuizList = QuizList;
    }

    @Override
    public QuizAdapter.QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.quiz_row_layout_admin, null);
        return new QuizAdapter.QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizAdapter.QuizViewHolder holder, int position) {
        //getting the product of the specified position
        Quiz quiz = QuizList.get(position);

        //binding the data with the viewholder views

        holder.quizName.setText(quiz.getQuizName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));



        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, Quiz.class);
            intent.putExtra("quizId", quiz.getQuizId());
            intent.putExtra("quizName",quiz.getQuizName());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return QuizList.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView quizName;

        public QuizViewHolder(View itemView) {
            super(itemView);

            quizName = itemView.findViewById(R.id.quiz_row_layout_admin_quiz3f_name);

        }
    }
}
