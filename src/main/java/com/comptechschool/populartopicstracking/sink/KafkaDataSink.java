package com.comptechschool.populartopicstracking.sink;

import com.comptechschool.populartopicstracking.utils.ConfigUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class KafkaDataSink {

    private final static Logger logger = LoggerFactory.getLogger(KafkaDataSink.class);

    public static KafkaSink<String> createKafkaSink() throws IOException {
        String bootstrapServer = ConfigUtils.getProperty("kafka.bootstrap-server");
        String topic = ConfigUtils.getProperty("kafka.input-topic");

        logger.debug("Kafka sink bootstrap server: {}", bootstrapServer);
        logger.debug("Kafka sink topic: {}", topic);

        return KafkaSink.<String>builder()
                .setBootstrapServers(bootstrapServer)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(topic)
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }
}
