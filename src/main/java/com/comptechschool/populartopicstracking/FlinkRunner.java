package com.comptechschool.populartopicstracking;

import com.comptechschool.populartopicstracking.function.JsonToInputEntityMapper;
import com.comptechschool.populartopicstracking.source.KafkaDataSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FlinkRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env
                .fromSource(
                        KafkaDataSource.createKafkaSource(),
                        WatermarkStrategy.forMonotonousTimestamps(),
                        "Kafka Source"
                )
                .map(new JsonToInputEntityMapper())
                .print();

        env.execute();
    }
}
