package com.langexpo.model;

public class Quiz {
    private long quizId;
    private String quizName;

    public Quiz(long quizId, String quizName){
        this.quizId = quizId;
        this.quizName = quizName;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }
}