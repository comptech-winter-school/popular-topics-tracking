package com.comptechschool.populartopicstracking;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.junit.Test;

public class DataTest {

    @Test
    public void dataSourceTest() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment
                .addSource(new DataSource(10L, 100))
                .addSink(new PrintSinkFunction<>());
        environment.execute();
    }
}
