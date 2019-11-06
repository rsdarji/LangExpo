package com.langexpo.model;

public class Language {
    private long languageId;
    private String languageName;
    private String languageFlagURL;

    public Language(long languageId, String languageName, String languageFlagURL){
        this.languageId = languageId;
        this.languageName = languageName;
        this.languageFlagURL = languageFlagURL;
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

    public String getLanguageFlagURL() {
        return languageFlagURL;
    }

    public void setLanguageFlagURL(String languageFlagURL) {
        this.languageFlagURL = languageFlagURL;
    }
}
