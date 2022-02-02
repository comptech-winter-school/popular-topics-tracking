package com.comptechschool.populartopicstracking.function;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import java.util.List;

public class ListToTupleFlatMapper
        implements FlatMapFunction<List<Tuple3<Long, Long, String>>, Tuple3<Long, Long, String>> {
    @Override
    public void flatMap(List<Tuple3<Long, Long, String>> tuples,
                        Collector<Tuple3<Long, Long, String>> collector)
            throws Exception {
        for (Tuple3<Long, Long, String> tuple : tuples) {
            collector.collect(tuple);
        }
    }
}
