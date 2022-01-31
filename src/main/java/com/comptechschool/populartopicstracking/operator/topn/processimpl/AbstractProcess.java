package com.comptechschool.populartopicstracking.operator.topn.processimpl;


import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.List;

public abstract class AbstractProcess extends ProcessAllWindowFunction<InputEntity, List<InputEntity>, TimeWindow> {

    @Override
    public abstract void process(ProcessAllWindowFunction<InputEntity, List<InputEntity>, TimeWindow>.Context context,
                                 Iterable<InputEntity> elements, Collector<List<InputEntity>> out) throws Exception;

    @Override
    public abstract void open(Configuration parameters) throws Exception;

    @Override
    public abstract void clear(Context context) throws Exception;

}
