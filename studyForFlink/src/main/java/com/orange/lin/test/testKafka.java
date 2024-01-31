package com.orange.lin.test;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * @author oranglzc
 * @Description:
 * @creat 2021-03-31-21:53
 */
public class testKafka {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.1.101:9092");
        properties.setProperty("group.id", "testKafka");
        properties.setProperty("auto.offset.reset", "latest");


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> kafkaSource = env.addSource(
                new FlinkKafkaConsumer<>("sun", new SimpleStringSchema(), properties));

        kafkaSource.print("message:");

        env.execute();

    }
}
