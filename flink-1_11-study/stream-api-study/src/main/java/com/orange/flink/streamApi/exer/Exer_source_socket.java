package com.orange.flink.streamApi.exer;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

public class Exer_source_socket {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> streamSource1 = env.socketTextStream("localhost", 9999);
        DataStreamSource<String> streamSource2 = env.socketTextStream("localhost", 8888);

        SingleOutputStreamOperator<Integer> intDS = streamSource2.map(String::length);

        ConnectedStreams<String, Integer> connectedStreams = streamSource1.connect(intDS);

        SingleOutputStreamOperator<Object> singleOutputStreamOperator = connectedStreams.map(new CoMapFunction<String, Integer, Object>() {
            @Override
            public Object map1(String s) throws Exception {
                return s;
            }

            @Override
            public Object map2(Integer integer) throws Exception {
                return integer;
            }
        });

        singleOutputStreamOperator.print();
//        streamSource1.print();

        env.execute("socket execute");



    }
}
