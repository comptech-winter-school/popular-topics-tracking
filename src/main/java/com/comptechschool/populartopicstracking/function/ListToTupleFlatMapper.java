package com.comptechschool.populartopicstracking.function;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.util.Collector;

import java.util.List;

public class ListToTupleFlatMapper
        implements FlatMapFunction<List<Tuple4<Long, Long, String, Long>>, Tuple4<Long, Long, String, Long>> {
    @Override
    public void flatMap(List<Tuple4<Long, Long, String, Long>> tuples,
                        Collector<Tuple4<Long, Long, String, Long>> collector)
            throws Exception {
        for (Tuple4<Long, Long, String, Long> tuple : tuples) {
            collector.collect(tuple);
        }
    }
}
