package com.comptechschool.populartopicstracking.operator.topn;

import com.comptechschool.populartopicstracking.DataSource;
import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;


/**
 * TopN counts requests in real time
 */
public class TopNStreamProcess {

    private static int topN = 3;

    public static void runTopNOperator() throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        initProperties(env);
        DataStreamSource<InputEntity> source = env.addSource(new DataSource(10000L));
        SingleOutputStreamOperator<List<InputEntity>> dataStream = source
                .assignTimestampsAndWatermarks(new EntityAssignerWaterMarks(Time.seconds(5))) //FIXME Replace with current implementation
                //.windowAll(TumblingEventTimeWindows.of(Time.days(1), Time.hours(16)))
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(30))) //TODO needs time adjustment
                .allowedLateness(Time.seconds(20))
                .trigger(new EntityTrigger(50000))//clean up the window data
                .process(new EntityProcessFunction(topN));

        dataStream.print();
        // TODO Sink
        env.execute("Real-time entity topN");
    }


    private static void initProperties(StreamExecutionEnvironment env) {
        //Global parallelism
        env.setParallelism(1);

        //checkpoint per minute
        env.enableCheckpointing(1000 * 60 * 10);

        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //Default - EventTime
        //Restart three times after failure, each interval of 20s
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, org.apache.flink.api.common.time.Time.seconds(20)));
        //Set the maximum parallelism of checkpoints
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //Do not delete the save point data even if you manually cancel the task
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.
                ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION); //FIXME Replace with current implementation


    }
}
