package com.orange.flink.streamApi.exer.need;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Log_Page_TopN {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);


    }
}
