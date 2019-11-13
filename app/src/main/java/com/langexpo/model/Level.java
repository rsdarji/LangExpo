package com.langexpo.model;

public class Level {
    private long levelId;
    private String levelName;
    private String levelType;
    private long languageId;
    private String languageName;
    private int sequenceNumber;


    public Level(long levelId, String levelName, String levelType,
                 long languageId, String languageName, int sequenceNumber){
        this.levelId = levelId;
        this.levelName = levelName;
        this.levelType = levelType;
        this.languageId = languageId;
        this.languageName = languageName;
        this.sequenceNumber = sequenceNumber;
    }
    public Level(long levelId, String levelName, String levelType,
                 long languageId, int sequenceNumber){
        this.levelId = levelId;
        this.levelName = levelName;
        this.levelType = levelType;
        this.languageId = languageId;
        this.sequenceNumber = sequenceNumber;
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

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
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

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
