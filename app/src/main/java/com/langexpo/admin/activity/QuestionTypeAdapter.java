package com.langexpo.admin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.model.Language;
import com.langexpo.model.QuestionType;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QuestionTypeAdapter extends RecyclerView.Adapter<QuestionTypeAdapter.QuestionTypeViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<QuestionType> questionTypeList;

    //getting the context and product list with constructor
    public QuestionTypeAdapter(Context mCtx, List<QuestionType> questionTypeList) {
        this.mCtx = mCtx;
        this.questionTypeList = questionTypeList;
    }

    @Override
    public QuestionTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.questiontype_row_layout_admin, null);
        return new QuestionTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionTypeViewHolder holder, int position) {
        //getting the product of the specified position
        QuestionType questionType = questionTypeList.get(position);

        //binding the data with the viewholder views

        holder.questionTypeName.setText(questionType.getQuestionTypeName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));



        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, AddQuestionType.class);
            intent.putExtra("questionTypeId", questionType.getQuestionTypeId());
            intent.putExtra("questionTypeName",questionType.getQuestionTypeName());
            intent.putExtra("totalOptions", questionType.getTotalOptions());
            intent.putExtra("totalDisplayOptions", questionType.getTotalDisplayOptions());
            intent.putExtra("multipleAnswer", questionType.isMultipleAnswer());
            intent.putExtra("questionAudio", questionType.isQuestionAudio());
            intent.putExtra("optionAudio", questionType.isOptionAudio());
            intent.putExtra("questionImage", questionType.isQuestionImage());
            intent.putExtra("optionImage", questionType.isOptionImage());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return questionTypeList.size();
    }

    class QuestionTypeViewHolder extends RecyclerView.ViewHolder {

        TextView questionTypeName;

        public QuestionTypeViewHolder(View itemView) {
            super(itemView);

            questionTypeName = itemView.findViewById(R.id.questiontype_row_layout_admin_questiontype3f_name);

        }
    }
}
