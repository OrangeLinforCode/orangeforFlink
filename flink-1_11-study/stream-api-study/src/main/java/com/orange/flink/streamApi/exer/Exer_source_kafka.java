package com.orange.flink.streamApi.exer;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

public class Exer_source_kafka {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties prop = new Properties();

        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "flink_0121");
        DataStreamSource<String> kafkaDS = env.addSource(new FlinkKafkaConsumer011<String>("test", new SimpleStringSchema(), prop));

        kafkaDS.print();

        env.execute("kafka execute");



    }
}
