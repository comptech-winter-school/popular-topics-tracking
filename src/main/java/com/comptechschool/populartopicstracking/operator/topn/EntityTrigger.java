package com.comptechschool.populartopicstracking.operator.topn;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class EntityTrigger extends Trigger<InputEntity, TimeWindow> {
    private final Integer size;

    public EntityTrigger(Integer size) {
        super();
        this.size = size;
    }

    ReducingStateDescriptor<Long> countDesc = new ReducingStateDescriptor<Long>("count_desc", (ReduceFunction<Long>) Long::sum, Long.class);
    ReducingState<Long> reducingState = null;

    @Override
    public TriggerResult onElement(InputEntity inputEntity, long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        reducingState = triggerContext.getPartitionedState(countDesc);
        reducingState.add(1L);
        if (reducingState.get() >= size) {
            reducingState.clear();
            return TriggerResult.FIRE_AND_PURGE;
        } else {
            return TriggerResult.CONTINUE;
        }
    }

    @Override
    public TriggerResult onProcessingTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        return TriggerResult.FIRE_AND_PURGE; //Called when the event time timer is triggered
    }

    @Override
    public void clear(TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        if (reducingState != null) {
            reducingState.clear();
        }
    }
}
