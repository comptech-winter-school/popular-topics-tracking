package com.comptechschool.populartopicstracking.entity;

public class InputEntity {
    private Long id;
    private Long timestamp;
    private String actionType;

    public InputEntity(Long id, Long timestamp, String actionType) {
        this.id = id;
        this.timestamp = timestamp;
        this.actionType = actionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "InputEntity{" +
                "id=" + id +
                ", actionType='" + actionType + '\'' +
                '}';
    }
}
