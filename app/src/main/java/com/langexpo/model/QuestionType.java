package com.langexpo.model;

public class QuestionType {
    private long QuestionTypeId;
    private String QuestionTypeName;

    public QuestionType(long QuestionTypeId, String QuestionTypeName){
        this.QuestionTypeId = QuestionTypeId;
        this.QuestionTypeName = QuestionTypeName;
    }

    public long getQuestionTypeId() {
        return QuestionTypeId;
    }

    public void setQuestionTypeId(long questionTypeId) {
        QuestionTypeId = questionTypeId;
    }

    public String getQuestionTypeName() {
        return QuestionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName) {
        QuestionTypeName = questionTypeName;
    }
}
