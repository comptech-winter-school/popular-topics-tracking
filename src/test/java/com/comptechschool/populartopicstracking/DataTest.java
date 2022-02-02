package com.comptechschool.populartopicstracking;

import com.comptechschool.populartopicstracking.source.DataSource;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.junit.Test;

public class DataTest {

    @Test
    public void dataSourceTest() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        environment
                .addSource(new DataSource(10L))
                .map(inputEntity -> new Tuple2<Long, Integer>(inputEntity.getId(), 1))
                .returns(TypeInformation.of(new TypeHint<Tuple2<Long, Integer>>() {}))
                .keyBy(value -> value.f0)
                .sum(1)
                .addSink(new PrintSinkFunction<>());
        environment.execute();
    }
}
