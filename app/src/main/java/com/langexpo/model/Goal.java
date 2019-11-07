package com.langexpo.model;

public class Goal {
    private long GoalId;
    private String GoalName;

    public Goal(long GoalId, String GoalName){
        this.GoalId = GoalId;
        this.GoalName = GoalName;
    }

    public long getGoalId() {
        return GoalId;
    }

    public void setGoalId(long goalId) {
        GoalId = goalId;
    }

    public String getGoalName() {
        return GoalName;
    }

    public void setGoalName(String goalName) {
        GoalName = goalName;
    }
}