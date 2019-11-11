package com.langexpo.model;

public class Question {
    private long QuestionId;
    private String QuestionName;
    private String QuestionType;
    private long QuestionTypeId;
    private String LanguageName;
    private long LanguageId;
    private String LevelName;
    private long LevelId;
    private String QuestionOption;

    public Question(long QuestionId, String QuestionName, String QuestionType, long QuestionTypeId, String LanguageName, long LanguageId, String LevelName, long LevelId, String QuestionOption){
        this.QuestionId = QuestionId;
        this.QuestionName = QuestionName;
        this.QuestionType = QuestionType;
        this.QuestionTypeId = QuestionTypeId;
        this.LanguageName = LanguageName;
        this.LanguageId = LanguageId;
        this.LevelId = LevelId;
        this.QuestionOption = QuestionOption;
    }

    public long getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(long questionId) {
        QuestionId = questionId;
    }

    public String getQuestionName() {
        return QuestionName;
    }

    public void setQuestionName(String questionName) {
        QuestionName = questionName;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public long getQuestionTypeId() {
        return QuestionTypeId;
    }

    public void setQuestionTypeId(long questionTypeId) {
        QuestionTypeId = questionTypeId;
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

    public String getQuestionOption() {
        return QuestionOption;
    }

    public void setQuestionOption(String questionOption) {
        QuestionOption = questionOption;
    }
}