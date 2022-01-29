package com.comptechschool.populartopicstracking.topnoperator.topn;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class EntityAssignerWaterMarks extends BoundedOutOfOrdernessTimestampExtractor<InputEntity> {


    public EntityAssignerWaterMarks(Time maxOutOfOrderness) {
        super(maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(InputEntity inputEntity) {
        return inputEntity.getTimestamp();
    }
}
