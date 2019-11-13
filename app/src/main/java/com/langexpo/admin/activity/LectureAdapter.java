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
import com.langexpo.model.Language;
import com.langexpo.model.Lecture;
import com.langexpo.model.Level;
import com.langexpo.model.Question;

import java.util.ArrayList;
import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Lecture> lectureList;
    private List<Lecture> mDataFiltered;
    //getting the context and product list with constructor
    public LectureAdapter(Context mCtx, List<Lecture> lectureList) {
        this.mCtx = mCtx;
        this.lectureList = lectureList;
        this.mDataFiltered = lectureList;
    }

    @Override
    public LectureAdapter.LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.lecture_row_layout_admin, null);
        return new LectureAdapter.LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LectureAdapter.LectureViewHolder holder, int position) {
        //getting the product of the specified position
        Lecture lecture = lectureList.get(position);

        //binding the data with the viewholder views

        holder.lectureName.setText(lecture.getLectureName());
        //holder.languageFlag.setImageBitmap(BitmapFactory.decodeStream(in));



        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mCtx, AddLecture.class);
            intent.putExtra("lectureId", lecture.getLectureId());
            intent.putExtra("lectureName",lecture.getLectureName());
            mCtx.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return lectureList.size();
    }

    class LectureViewHolder extends RecyclerView.ViewHolder {

        TextView lectureName;

        public LectureViewHolder(View itemView) {
            super(itemView);

            lectureName = itemView.findViewById(R.id.lecture_row_layout_admin_lecture3f_name);

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
