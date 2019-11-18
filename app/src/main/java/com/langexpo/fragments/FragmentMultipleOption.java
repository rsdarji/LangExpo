package com.langexpo.fragments;


import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.langexpo.R;
import com.langexpo.activity.DisplayLevelQuestions;
import com.langexpo.customfunction.CustomRadioGroupView;
import com.langexpo.model.Question;
import com.langexpo.model.QuestionModel;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMultipleOption extends Fragment implements View.OnClickListener {

    String checkedRadioButtonText = "";
    Button verifyAnswerBT, nextQuestionBT;
    CustomRadioGroupView radioGroup;
    RadioButton optionOneRB, optionTwoRB, optionThreeRB, optionFourRB, optionFiveRB, optionSixRB;
    TextView question;
    LinearLayout verifiedQuestionLayout;
    QuestionModel q;
    List<QuestionModel> questionList = new ArrayList<QuestionModel>();



    public FragmentMultipleOption() {
        // Required empty public constructor
    }

    public FragmentMultipleOption(List<QuestionModel> questionList) {
        // Required empty public constructor
        this.questionList = questionList;
        this.q = questionList.get(0);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_multiple_option, container, false);
        verifiedQuestionLayout = (LinearLayout) view.findViewById(R.id.verified_question_layout);
        verifiedQuestionLayout.setVisibility(View.GONE);


        question = (TextView) view.findViewById(R.id.multiple_option_question );
        radioGroup = (CustomRadioGroupView) view.findViewById(R.id.multiple_question_radio_group);
        optionOneRB = (RadioButton) view.findViewById(R.id.option_one_radio_button);
        optionTwoRB = (RadioButton) view.findViewById(R.id.option_two_radio_button);
        optionThreeRB = (RadioButton) view.findViewById(R.id.option_three_radio_button);
        optionFourRB = (RadioButton) view.findViewById(R.id.option_four_radio_button);
        optionFiveRB = (RadioButton) view.findViewById(R.id.option_five_radio_button);
        optionSixRB = (RadioButton) view.findViewById(R.id.option_six_radio_button);
        verifyAnswerBT = (Button) view.findViewById(R.id.verify_answer_bt);
        nextQuestionBT = (Button) view.findViewById(R.id.next_question_bt);




        RadioButton[] availableOption = {optionOneRB, optionTwoRB, optionThreeRB,
                optionFourRB, optionFiveRB, optionSixRB};
        hideComponentFromArray(availableOption);
        String[] options = q.getQuestionOption().split(",");
        options = (String[]) Utility.shuffleArrayValues(options);

        question.setText(q.getQuestion().toString());
        for(int i = 0; i<options.length;i++){
            availableOption[i].setVisibility(View.VISIBLE);
            availableOption[i].setText(options[i]);
        }



        radioGroup.setOnCheckedChangeListener(new CustomRadioGroupView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomRadioGroupView group, int checkedId) {
                checkedRadioButtonText = ((RadioButton) view.findViewById(checkedId)).getText().toString();
                Toast.makeText(getContext(),checkedRadioButtonText,Toast.LENGTH_SHORT).show();
            }
        });

        verifyAnswerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"clicked multiple option continue button: ",Toast.LENGTH_LONG).show();
                if(checkedRadioButtonText.equalsIgnoreCase("")){
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(getContext(), getActivity());
                    alertDialog.alertDialog("Error", "Please choose answer");
                    return;
                }

                if(checkedRadioButtonText.equalsIgnoreCase(q.getAnswer())){
                    final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.correct_answer);
                    mp.start();
                    verifyAnswerBT.setVisibility(View.GONE);
                    nextQuestionBT.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient_green));
                    verifiedQuestionLayout.setVisibility(View.VISIBLE);

                    questionList.remove(q);
                    //((DisplayLevelQuestions)getActivity()).nextQuestion(questionList);

                }
            }
        });

        nextQuestionBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DisplayLevelQuestions)getActivity()).nextQuestion(questionList);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.verify_answer_bt:
                Toast.makeText(getActivity().getApplicationContext(),
                        "Switch onclick called... ",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.next_question_bt:
                Toast.makeText(getActivity().getApplicationContext(),
                        "clicked NEXT question button: ",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void hideComponentFromArray(Object[] o){
        for(int i=0;i<o.length;i++){
            if(o instanceof RadioButton[]){
                ((RadioButton)o[i]).setVisibility(View.GONE);
            }
        }
    }
}
