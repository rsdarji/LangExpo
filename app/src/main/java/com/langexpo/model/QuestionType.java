package com.langexpo.model;

public class QuestionType {
    private long questionTypeId;
    private String questionTypeName;
    private int totalOptions; //how many total options
    private int totalDisplayOptions; //how many total options user will get displayed.
    private boolean multipleAnswer;
    private boolean questionAudio;
    private boolean optionAudio;
    private boolean questionImage;
    private boolean optionImage;

    public QuestionType(long QuestionTypeId, String QuestionTypeName,
                        int totalOptions, int totalDisplayOptions,
                        boolean multipleAnswer, boolean questionAudio,
                        boolean optionAudio, boolean questionImage,
                        boolean optionImage){
        this.questionTypeId = QuestionTypeId;
        this.questionTypeName = QuestionTypeName;
        this.totalOptions = totalOptions;
        this.totalDisplayOptions = totalDisplayOptions;
        this.multipleAnswer = multipleAnswer;
        this.questionAudio = questionAudio;
        this.optionAudio = optionAudio;
        this.questionImage = questionImage;
        this.optionImage = optionImage;
    }

    public long getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(long questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getQuestionTypeName() {
        return questionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public int getTotalDisplayOptions() {
        return totalDisplayOptions;
    }

    public void setTotalDisplayOptions(int totalDisplayOptions) {
        this.totalDisplayOptions = totalDisplayOptions;
    }

    public boolean isMultipleAnswer() {
        return multipleAnswer;
    }

    public void setMultipleAnswer(boolean multipleAnswer) {
        this.multipleAnswer = multipleAnswer;
    }

    public boolean isQuestionAudio() {
        return questionAudio;
    }

    public void setQuestionAudio(boolean questionAudio) {
        this.questionAudio = questionAudio;
    }

    public boolean isOptionAudio() {
        return optionAudio;
    }

    public void setOptionAudio(boolean optionAudio) {
        this.optionAudio = optionAudio;
    }

    public boolean isQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(boolean questionImage) {
        this.questionImage = questionImage;
    }

    public boolean isOptionImage() {
        return optionImage;
    }

    public void setOptionImage(boolean optionImage) {
        this.optionImage = optionImage;
    }
}
