package com.langexpo.fragments;


import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.langexpo.R;
import com.langexpo.activity.DisplayLevelQuestions;
import com.langexpo.activity.DisplayQuizQuestions;
import com.langexpo.customfunction.CustomRadioGroupView;
import com.langexpo.model.QuestionModel;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;
import com.langexpo.utility.Session;
import com.langexpo.utility.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPictureQuestion extends Fragment implements View.OnClickListener {

    String checkedRadioButtonText = "";
    Button verifyAnswerBT, nextQuestionGreenBT, nextQuestionThemeBT;
    CustomRadioGroupView radioGroup;
    RadioButton optionOneRB, optionTwoRB, optionThreeRB, optionFourRB, optionFiveRB, optionSixRB;
    TextView question, questionVerificationResult, correctAnswerTV, correctAnswer;
    LinearLayout verifiedQuestionLayout;
    ImageView questionImageIV;
    QuestionModel questionModel;
    List<Long> questionIdList = new ArrayList<Long>();
    boolean quiz = false;

    public FragmentPictureQuestion() {
        // Required empty public constructor
    }

    public FragmentPictureQuestion(QuestionModel questionModel,
                                  List<Long> questionIdList, boolean quiz) {
        // Required empty public constructor
        this.questionIdList = questionIdList;
        this.questionModel = questionModel;
        this.quiz = quiz;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture_question, container, false);

        verifiedQuestionLayout = view.findViewById(R.id.verified_question_layout);
        verifiedQuestionLayout.setVisibility(View.GONE);


        question = view.findViewById(R.id.multiple_option_question);
        questionImageIV = view.findViewById(R.id.picture_question_img);
        radioGroup = view.findViewById(R.id.multiple_question_radio_group);
        optionOneRB = view.findViewById(R.id.option_one_radio_button);
        optionTwoRB = view.findViewById(R.id.option_two_radio_button);
        optionThreeRB = view.findViewById(R.id.option_three_radio_button);
        optionFourRB = view.findViewById(R.id.option_four_radio_button);
        optionFiveRB = view.findViewById(R.id.option_five_radio_button);
        optionSixRB = view.findViewById(R.id.option_six_radio_button);
        verifyAnswerBT = view.findViewById(R.id.verify_answer_bt);
        nextQuestionGreenBT = view.findViewById(R.id.next_question_green_bt);
        nextQuestionThemeBT = view.findViewById(R.id.next_question_theme_bt);

        questionVerificationResult = view.findViewById(R.id.question_verification_result);
        correctAnswerTV = view.findViewById(R.id.correct_answer_tv);
        correctAnswer = view.findViewById(R.id.correct_answer);


        RadioButton[] availableOption = {optionOneRB, optionTwoRB, optionThreeRB,
                optionFourRB, optionFiveRB, optionSixRB};
        hideComponentFromArray(availableOption);
        String[] options = questionModel.getQuestionOption().split(",");
        options = (String[]) Utility.shuffleArrayValues(options);

        question.setText(questionModel.getQuestion());
        Picasso.get()
                .load(questionModel.getQuestionImage())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(questionImageIV);
        for (int i = 0; i < options.length; i++) {
            availableOption[i].setVisibility(View.VISIBLE);
            availableOption[i].setText(options[i]);
        }

        radioGroup.setOnCheckedChangeListener(new CustomRadioGroupView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomRadioGroupView group, int checkedId) {
                checkedRadioButtonText = ((RadioButton) view.findViewById(checkedId)).getText().toString();
                //Toast.makeText(getContext(),checkedRadioButtonText,Toast.LENGTH_SHORT).show();
            }
        });

        verifyAnswerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(),"clicked multiple option continue button: ",Toast.LENGTH_LONG).show();
                if (checkedRadioButtonText.equalsIgnoreCase("")) {
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(getContext(), getActivity());
                    alertDialog.alertDialog("Error", Constant.CHOOSE_ANSWER_ERROR_MESSAGE);
                    return;
                }
                verifyAnswerBT.setVisibility(View.GONE);
                verifiedQuestionLayout.setVisibility(View.VISIBLE);
                MediaPlayer mp;
                if (checkedRadioButtonText.toLowerCase().trim().equalsIgnoreCase(questionModel.getAnswer().toLowerCase().trim())) {
                    mp = MediaPlayer.create(getContext(), R.raw.correct_answer);
                    mp.start();

                    //nextQuestionBT.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient_green));

                    questionVerificationResult.setText(Constant.CORRECT_ANSWER);
                    questionVerificationResult.setTextColor(getResources().getColor(R.color.colorGreenDark));
                    correctAnswerTV.setVisibility(View.GONE);
                    correctAnswer.setVisibility(View.GONE);
                    nextQuestionThemeBT.setVisibility(View.GONE);
                    if (quiz) {
                        DisplayQuizQuestions.correctCount += 1;
                    } else {
                        DisplayLevelQuestions.correctCount += 1;
                    }

                } else {
                    mp = MediaPlayer.create(getContext(), R.raw.incorrect_answer);
                    mp.start();
                    verifiedQuestionLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    nextQuestionGreenBT.setVisibility(View.GONE);
                    questionVerificationResult.setText(Constant.INCORRECT_ANSWER);
                    questionVerificationResult.setTextColor(getResources().getColor(R.color.colorPrimary));
                    correctAnswerTV.setVisibility(View.VISIBLE);
                    correctAnswer.setVisibility(View.VISIBLE);
                    correctAnswer.setText(questionModel.getAnswer());

                    Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(500);
                    }
                    if (quiz) {
                        DisplayQuizQuestions.incorrectCount += 1;
                    } else {
                        DisplayLevelQuestions.incorrectCount += 1;
                    }

                }
                questionIdList.remove(questionModel.getQuestionId());
            }
        });

        nextQuestionGreenBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        nextQuestionThemeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        return view;
    }
    public void next() {
        if (quiz) {
            ((DisplayQuizQuestions)getActivity()).nextQuestion(questionIdList,quiz);
        } else {
            ((DisplayLevelQuestions) getActivity()).nextQuestion(questionIdList, quiz);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void hideComponentFromArray(Object[] o) {
        for (int i = 0; i < o.length; i++) {
            if (o instanceof RadioButton[]) {
                ((RadioButton) o[i]).setVisibility(View.GONE);
            }
        }
    }
}
