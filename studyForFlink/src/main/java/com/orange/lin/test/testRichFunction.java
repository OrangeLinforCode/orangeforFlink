package com.orange.lin.test;


import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class testRichFunction {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(5);

        env
                .fromElements(1, 2, 3, 4, 5)
                .map(new MyRichMapFunction()).setParallelism(1)
                .print();

        env.execute();
    }

    public static class MyRichMapFunction extends RichMapFunction<Integer,Integer>{


        @Override
        public RuntimeContext getRuntimeContext() {
            return super.getRuntimeContext();
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            System.out.println("open only procrss onces");
        }

        @Override
        public void close() throws Exception {
            System.out.println("close only process onces");
        }

        @Override
        public Integer map(Integer value) throws Exception {
            System.out.println(value+"执行一次");
            return value*value;
        }
    }
}
