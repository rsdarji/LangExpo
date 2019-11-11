package com.langexpo.model;

public class Level {
    private long levelId;
    private String levelName;
    private String levelType;
    private int sequenceNumber;


    public Level(long levelId, String levelName, String levelType, int sequenceNumber){
        this.levelId = levelId;
        this.levelName = levelName;
        this.levelType = levelType;
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

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
