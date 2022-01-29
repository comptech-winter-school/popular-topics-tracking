package com.comptechschool.populartopicstracking.entity;

public class InputEntity {
    private Long id;
    private Long timestamp;

    public InputEntity(Long id, Long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "InputEntity{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                '}';
    }
}
