package com.orange.lin.flink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@Slf4j
public class MyFirstTask implements FlinkTask{
    @Override
    public void executeTask(StreamExecutionEnvironment env, String[] args) {

    }
}
