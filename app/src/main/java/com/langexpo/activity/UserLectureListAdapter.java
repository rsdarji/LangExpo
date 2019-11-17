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
import com.langexpo.admin.activity.AddLecture;
import com.langexpo.model.Lecture;

import java.util.ArrayList;
import java.util.List;

public class UserLectureListAdapter extends RecyclerView.Adapter<UserLectureListAdapter.LectureViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Lecture> lectureList;
    private List<Lecture> mDataFiltered;
    //getting the context and product list with constructor
    public UserLectureListAdapter(Context mCtx, List<Lecture> lectureList) {
        this.mCtx = mCtx;
        this.lectureList = lectureList;
        this.mDataFiltered = lectureList;
    }

    @Override
    public UserLectureListAdapter.LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_lecture_list_row_layout_admin, null);
        return new UserLectureListAdapter.LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserLectureListAdapter.LectureViewHolder holder, int position) {
        //getting the product of the specified position
        Lecture lecture = lectureList.get(position);

        //binding the data with the viewholder views

        holder.lectureName.setText(lecture.getLectureName());
        holder.languageName.setText(lecture.getLanguageName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));



        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, DisplayLecture.class);
            intent.putExtra("lectureId", lecture.getLectureId());
            intent.putExtra("lectureName",lecture.getLectureName());
            intent.putExtra("lectureContent", lecture.getLectureContent());
            intent.putExtra("languageId",lecture.getLanguageId());
            intent.putExtra("languageName", lecture.getLanguageName());
            intent.putExtra("levelId",lecture.getLevelId());
            intent.putExtra("levelName",lecture.getLevelName());
            intent.putExtra("sequenceNumber",lecture.getSequenceNumber());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return lectureList.size();
    }

    class LectureViewHolder extends RecyclerView.ViewHolder {

        TextView lectureName;
        TextView languageName;

        public LectureViewHolder(View itemView) {
            super(itemView);

            lectureName = itemView.findViewById(R.id.lecture_row_layout_user_lecture3f_name);
            languageName = itemView.findViewById(R.id.lecture_row_layout_user_language_name);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = lectureList ;
                }
                else {
                    List<Lecture> lstFiltered = new ArrayList<>();
                    for (Lecture row : lectureList) {
                        if (row.getLectureName().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                        if (row.getLanguageName().toLowerCase().contains(Key.toLowerCase())){
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
                lectureList = (List<Lecture>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
