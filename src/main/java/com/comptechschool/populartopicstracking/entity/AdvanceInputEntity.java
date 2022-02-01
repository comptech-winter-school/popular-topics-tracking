package com.comptechschool.populartopicstracking.entity;


public class AdvanceInputEntity implements Comparable<AdvanceInputEntity> {

    private long eventFrequency;
    private InputEntity inputEntity;

    public AdvanceInputEntity(long eventFrequency, InputEntity inputEntity) {
        this.eventFrequency = eventFrequency;
        this.inputEntity = inputEntity;
    }

    public long getEventFrequency() {
        return eventFrequency;
    }

    public void setEventFrequency(long eventFrequency) {
        this.eventFrequency = eventFrequency;
    }

    public InputEntity getInputEntity() {
        return inputEntity;
    }

    public void setInputEntity(InputEntity inputEntity) {
        this.inputEntity = inputEntity;
    }

    @Override
    public String toString() {
        return "AdvanceInputEntity{" +
                "eventFrequency=" + eventFrequency +
                ", inputEntity=" + inputEntity +
                '}';
    }

    @Override
    public int compareTo(AdvanceInputEntity advanceInputEntity) {
        return (int) (this.getEventFrequency() - advanceInputEntity.getEventFrequency());
    }
}
