package com.comptechschool.populartopicstracking.source;

import com.comptechschool.populartopicstracking.utils.ConfigUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class KafkaDataSource {

    private final static Logger logger = LoggerFactory.getLogger(KafkaDataSource.class);

    public static KafkaSource<String> createKafkaSource() throws IOException {
        String bootstrapServer = ConfigUtils.getProperty("kafka.bootstrap-server");
        String topic = ConfigUtils.getProperty("kafka.input-topic");
        String groupId = ConfigUtils.getProperty("kafka.group-id");

        logger.debug("Kafka source bootstrap server: {}", bootstrapServer);
        logger.debug("Kafka source topic: {}", topic);
        logger.debug("Kafka source groupId: {}", groupId);

        return KafkaSource.<String>builder()
                .setBootstrapServers(bootstrapServer)
                .setTopics(topic)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest()) //TODO: set OffsetsInitializer.latest() when app will have done
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
    }
}
