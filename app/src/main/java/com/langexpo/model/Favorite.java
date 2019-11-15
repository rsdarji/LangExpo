package com.langexpo.model;

public class Favorite {
    private long favoriteId;
    private long userId;
    private long lectureId;
    private long questionId;
    private String word;
    private String wordLink;
    private String resultWord;

    public Favorite(long favoriteId,long userId,long lectureId,
                    long questionId,String word,String wordLink, String resultWord){

        this.favoriteId = favoriteId;
        this.userId = userId;
        this.lectureId = lectureId;
        this.questionId = questionId;
        this.word = word;
        this.wordLink = wordLink;
        this.resultWord = resultWord;
    }

    public long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordLink() {
        return wordLink;
    }

    public void setWordLink(String wordLink) {
        this.wordLink = wordLink;
    }

    public String getResultWord() {
        return resultWord;
    }

    public void setResultWord(String resultWord) {
        this.resultWord = resultWord;
    }
}
