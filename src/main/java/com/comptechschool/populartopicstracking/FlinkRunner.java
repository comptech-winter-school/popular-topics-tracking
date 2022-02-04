package com.comptechschool.populartopicstracking;

import com.comptechschool.populartopicstracking.function.InputEntityFilter;
import com.comptechschool.populartopicstracking.function.InputEntityKeyBy;
import com.comptechschool.populartopicstracking.function.JsonToInputEntityMapper;
import com.comptechschool.populartopicstracking.function.ListToTupleFlatMapper;
import com.comptechschool.populartopicstracking.operator.topn.EntityTrigger;
import com.comptechschool.populartopicstracking.operator.topn.processimpl.DefaultEntityProcessFunction;
import com.comptechschool.populartopicstracking.source.KafkaDataSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.cassandra.CassandraSink;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class FlinkRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        int n = 3;

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        initProperties(env);

        DataStream<Tuple4<Long, Long, String, Long>> result = env
                .fromSource(
                        KafkaDataSource.createKafkaSource(),
                        WatermarkStrategy.forMonotonousTimestamps(),
                        "Kafka Source"
                )
                .map(new JsonToInputEntityMapper())
                .filter(new InputEntityFilter())
                .keyBy(new InputEntityKeyBy())
                .assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(5)))
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(20)))
                .allowedLateness(Time.seconds(20))
                .trigger(new EntityTrigger(500000))//clean up the window data
                .process(new DefaultEntityProcessFunction(n))
                .flatMap(new ListToTupleFlatMapper())
                .returns(TypeInformation.of(new TypeHint<Tuple4<Long, Long, String, Long>>() {
                }));

        CassandraSink.addSink(result)
                .setQuery("INSERT INTO comptech.topn(id, frequency, action, timestamp) values (?, ?, ?, ?);")
                .setHost("127.0.0.1")
                .build()
                .name("cassandra Sink")
                .disableChaining();

        env.execute("kafka- 3.0 source, cassandra-4.0.1 sink, tuple4");
    }

    private void initProperties(StreamExecutionEnvironment env) {
        env.setParallelism(5);
        env.enableCheckpointing(1000 * 60 * 10);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //Restart three times after failure, each interval of 20s
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, org.apache.flink.api.common.time.Time.seconds(20)));
        //Set the maximum parallelism of checkpoints
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //Do not delete the save point data even if you manually cancel the task
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.
                ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
    }
}
