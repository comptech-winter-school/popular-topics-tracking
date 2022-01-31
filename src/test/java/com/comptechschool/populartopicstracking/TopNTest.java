package com.comptechschool.populartopicstracking;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.InputEntityFilter;
import com.comptechschool.populartopicstracking.operator.topn.EntityProcessFunction;
import com.comptechschool.populartopicstracking.operator.topn.EntityTrigger;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import java.time.Duration;

public class TopNTest {

    @Test
    public void topNTest() throws Exception {
        int n = 3;

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        initProperties(env);

        env.addSource(new DataSource(10000L))
                //.assignTimestampsAndWatermarks(new EntityAssignerWaterMarks(Time.seconds(5)))
                .assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(20)))
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(30)))
                .allowedLateness(Time.seconds(20))
                .trigger(new EntityTrigger(50000))//clean up the window data
                .process(new EntityProcessFunction(n))
                .addSink(new PrintSinkFunction<>());

        // TODO Cassandra Sink
        env.execute("Real-time entity topN");
    }

    @Test
    public void filterKeyByTopNTest() throws Exception {
        int n = 3;

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        initProperties(env);

        env.addSource(new DataSource(10000L))
                //.filter(new InputEntityFilter())
                //.keyBy(...)
                //.assignTimestampsAndWatermarks(new EntityAssignerWaterMarks(Time.seconds(5)))
                .assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(20)))
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(30)))
                .allowedLateness(Time.seconds(20))
                .trigger(new EntityTrigger(50000))//clean up the window data
                .process(new EntityProcessFunction(n))
                .addSink(new PrintSinkFunction<>());

        // TODO Cassandra Sink
        env.execute("Real-time entity topN");
    }

    private void initProperties(StreamExecutionEnvironment env) {
        //Global parallelism
        env.setParallelism(5);

        //checkpoint per minute
        env.enableCheckpointing(1000 * 60 * 10);

        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //Default - EventTime
        //Restart three times after failure, each interval of 20s
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, org.apache.flink.api.common.time.Time.seconds(20)));
        //Set the maximum parallelism of checkpoints
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //Do not delete the save point data even if you manually cancel the task
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.
                ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
    }
}