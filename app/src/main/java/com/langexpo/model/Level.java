package com.langexpo.model;

public class Level {
    private long levelId;
    private String levelName;

    public Level(long levelId, String levelName){
        this.levelId = levelId;
        this.levelName = levelName;
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
}
