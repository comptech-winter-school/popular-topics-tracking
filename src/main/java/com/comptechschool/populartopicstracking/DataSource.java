package com.comptechschool.populartopicstracking;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Random;

public class DataSource extends RichSourceFunction<InputEntity> {

    private boolean isCancelled = false;
    private final Random random = new Random();
    private Long maxValue;
    private int delayMillis;
    private final String[] actionTypes = new String[]{"like", "video", "product"};

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
    public void run(SourceContext<InputEntity> sourceContext) throws Exception {
        while (!isCancelled) {
            InputEntity inputEntity = new InputEntity(
                    Math.abs(random.nextLong() % maxValue),
                    System.currentTimeMillis(),
                    actionTypes[random.nextInt(actionTypes.length)]
            );
            sourceContext.collectWithTimestamp(inputEntity, inputEntity.getTimestamp());
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
