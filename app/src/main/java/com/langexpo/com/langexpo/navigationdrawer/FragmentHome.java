package com.langexpo.com.langexpo.navigationdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.langexpo.R;

public class FragmentHome extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CardView cardView = (CardView) view.findViewById(R.id.card_view_add_question);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(view.getContext(), "Please enter correct Username", Toast.LENGTH_LONG);
            }
        });

        return view;
    }

    public static void clickAddQuestion(View v){
        Toast toast = Toast.makeText(v.getContext(), "Add Question clicked...", Toast.LENGTH_LONG);
        toast.show();
    }

    public static void clicAddQuestionType(View v){
        Toast toast = Toast.makeText(v.getContext(), "Add Question Type clicked...", Toast.LENGTH_LONG);
        toast.show();
    }
    public static void clicAddLecture(View v){
        Toast toast = Toast.makeText(v.getContext(), "Add Lecture clicked...", Toast.LENGTH_LONG);
        toast.show();
    }
    public static void clicAddLanguage(View v){
        Toast toast = Toast.makeText(v.getContext(), "Add Language clicked...", Toast.LENGTH_LONG);
        toast.show();
    }
    public static void clicAddGoal(View v){
        Toast toast = Toast.makeText(v.getContext(), "Add Goal clicked...", Toast.LENGTH_LONG);
        toast.show();
    }
    public static void clicAddLevel(View v){
        Toast toast = Toast.makeText(v.getContext(), "Add Level clicked...", Toast.LENGTH_LONG);
        toast.show();
    }







}
