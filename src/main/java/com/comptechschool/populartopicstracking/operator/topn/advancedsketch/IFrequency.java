package com.comptechschool.populartopicstracking.operator.topn.advancedsketch;

public interface IFrequency {

    void add(long item, long count);

    void add(String item, long count);

    long estimateCount(long item);

    long estimateCount(String item);

    long size();
}