package com.langexpo.model;

public class UserLecture {
    private long lectureId;
    private String lectureName;
    private String lectureContent;
    private long languageId;
    private String languageName;
    private long levelId;
    private String levelName;
    private int sequenceNumber;

    public UserLecture(long lectureId, String lectureName, String lectureContent,
                       long languageId, String languageName, long levelId,
                       String levelName, int sequenceNumber){
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.lectureContent = lectureContent;
        this.languageId = languageId;
        this.languageName = languageName;
        this.levelId = levelId;
        this.levelName = levelName;
        this.sequenceNumber = sequenceNumber;
    }

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLectureContent() {
        return lectureContent;
    }

    public void setLectureContent(String lectureContent) {
        this.lectureContent = lectureContent;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}