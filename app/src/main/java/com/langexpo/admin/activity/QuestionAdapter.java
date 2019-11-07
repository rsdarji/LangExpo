package com.langexpo.admin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Language;
import com.langexpo.model.Question;
import com.langexpo.model.QuestionType;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Question> QuestionList;

    //getting the context and product list with constructor
    public QuestionAdapter(Context mCtx, List<Language> languageList) {
        this.mCtx = mCtx;
        this.QuestionList = QuestionList;
    }

    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.language_row_layout_admin, null);
        return new QuestionAdapter.QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionAdapter.QuestionViewHolder holder, int position) {
        //getting the product of the specified position
        Question question = QuestionList.get(position);

        //binding the data with the viewholder views

        holder.questionName.setText(question.getQuestionName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));



        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, Question.class);
            intent.putExtra("questionId", question.getQuestionId());
            intent.putExtra("questionName",question.getQuestionName());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return QuestionList.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionName;

        public QuestionViewHolder(View itemView) {
            super(itemView);

            questionName = itemView.findViewById(R.id.question_row_layout_admin_question3f_name);

        }
    }
}
