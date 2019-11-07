package com.langexpo.model;

public class Lecture {
    private long LectureId;
    private String LectureName;
    private String LanguageName;
    private long LanguageId;
    private String LevelName;
    private long LevelId;
    private String Content;

    public Lecture(long LectureId, String LectureName, String LanguageName, long LanguageId, String LevelName, long LevelId, String Content){
        this.LectureId = LectureId;
        this.LectureName = LectureName;
        this.LanguageName = LanguageName;
        this.LanguageId = LanguageId;
        this.LevelName = LevelName;
        this.LevelId = LevelId;
        this.Content = Content;
    }

    public long getLectureId() {
        return LectureId;
    }

    public void setLectureId(long lectureId) {
        LectureId = lectureId;
    }

    public String getLectureName() {
        return LectureName;
    }

    public void setLectureName(String lectureName) {
        LectureName = lectureName;
    }

    public String getLanguageName() {
        return LanguageName;
    }

    public void setLanguageName(String languageName) {
        LanguageName = languageName;
    }

    public long getLanguageId() {
        return LanguageId;
    }

    public void setLanguageId(long languageId) {
        LanguageId = languageId;
    }

    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String levelName) {
        LevelName = levelName;
    }

    public long getLevelId() {
        return LevelId;
    }

    public void setLevelId(long levelId) {
        LevelId = levelId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}