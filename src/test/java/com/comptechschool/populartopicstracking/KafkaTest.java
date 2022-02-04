package com.comptechschool.populartopicstracking;

import com.comptechschool.populartopicstracking.function.InputEntityToJsonMapper;
import com.comptechschool.populartopicstracking.function.JsonToInputEntityMapper;
import com.comptechschool.populartopicstracking.sink.KafkaDataSink;
import com.comptechschool.populartopicstracking.source.DataSource;
import com.comptechschool.populartopicstracking.source.KafkaDataSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

public class KafkaTest {

    @Test
    public void produce() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env
                .addSource(new DataSource(50000L))
                .map(new InputEntityToJsonMapper())
                .sinkTo(KafkaDataSink.createKafkaSink());

        env.execute();
    }

    @Test
    public void consume() throws Exception {
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
