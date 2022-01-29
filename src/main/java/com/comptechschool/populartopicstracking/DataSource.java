package com.comptechschool.populartopicstracking;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Random;

public class DataSource extends RichSourceFunction<Long> {

    private boolean isCancelled = false;
    private final Random id = new Random();
    private Long maxValue;
    private int delayMillis;

    public DataSource() {
        maxValue = Long.MAX_VALUE;
        delayMillis = 0;
    }

    public DataSource(Long maxValue) {
        this.maxValue = maxValue;
    }

    public DataSource(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public DataSource(Long maxValue, int delayMillis) {
        this.maxValue = maxValue;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run(SourceContext<Long> sourceContext) throws Exception {
        while (!isCancelled) {
            sourceContext.collect(Math.abs(id.nextLong() % maxValue));
            Thread.sleep(delayMillis);
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }
}
