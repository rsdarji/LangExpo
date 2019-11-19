package com.langexpo.model;

public class UserProgress {
    private long userId;
    private long levelId;
    private String status;
    private long quizId;
    private int attempt;
    private int correctAnswerCount;
    private int inCorrectAnswerCount;

    public UserProgress(long userId, long levelId, String status, long quizId,
                        int attempt, int correctAnswerCount, int inCorrectAnswerCount){
        this.userId = userId;
        this.levelId = levelId;
        this.status = status;
        this.quizId = quizId;
        this.attempt = attempt;
        this.correctAnswerCount = correctAnswerCount;
        this.inCorrectAnswerCount = inCorrectAnswerCount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getCorrectAnswerCount() {
        return correctAnswerCount;
    }

    public void setCorrectAnswerCount(int correctAnswerCount) {
        this.correctAnswerCount = correctAnswerCount;
    }

    public int getInCorrectAnswerCount() {
        return inCorrectAnswerCount;
    }

    public void setInCorrectAnswerCount(int inCorrectAnswerCount) {
        this.inCorrectAnswerCount = inCorrectAnswerCount;
    }
}