package com.langexpo.model;

public class Quiz {
    private long quizId;
    private String quizName;
    private String userLevel;
    private String levelIds;

    public Quiz(long quizId, String quizName,String userLevel,
                String levelIds){
        this.quizId = quizId;
        this.quizName = quizName;
        this.userLevel = userLevel;
        this.levelIds = levelIds;
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

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(String levelIds) {
        this.levelIds = levelIds;
    }
}