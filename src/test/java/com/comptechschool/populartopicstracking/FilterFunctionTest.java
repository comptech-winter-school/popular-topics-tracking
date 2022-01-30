package com.comptechschool.populartopicstracking;

import com.comptechschool.populartopicstracking.operator.InputEntityFilter;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.junit.Test;

public class FilterFunctionTest {

    @Test
    public void filterTest() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment
                .addSource(new DataSource())
                .filter(new InputEntityFilter())
                .addSink(new PrintSinkFunction<>());
        environment.execute();
    }
}
