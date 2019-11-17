package com.langexpo.model;

public class QuestionModel {

    private long questionId;
    private String question;
    private int audio;
    private String answer;
    private String questionOption;
    private long questionType;
    private long courseLevel;

    public QuestionModel(long questionId, String question, int audio,
                         String answer, String questionOption, long questionType,
                         long courseLevel){
        this.questionId = questionId;
        this.question = question;
        this.audio = audio;
        this.answer = answer;
        this.questionOption = questionOption;
        this.questionType = questionType;
        this.courseLevel = courseLevel;

    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(String questionOption) {
        this.questionOption = questionOption;
    }

    public long getQuestionType() {
        return questionType;
    }

    public void setQuestionType(long questionType) {
        this.questionType = questionType;
    }

    public long getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(long courseLevel) {
        this.courseLevel = courseLevel;
    }
}
