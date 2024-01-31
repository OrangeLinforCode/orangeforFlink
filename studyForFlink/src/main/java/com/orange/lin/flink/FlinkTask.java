package com.orange.lin.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public interface FlinkTask {

    void executeTask(StreamExecutionEnvironment env,String[] args);


}
