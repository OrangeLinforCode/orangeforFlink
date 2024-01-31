package com.orange.flink.streamApi.exer;

import com.orange.flink.streamApi.exer.pojo.WaterSensor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

public class Exer_process {

    public static void main(String[] args) throws Exception {

        ArrayList<WaterSensor> waterSensors = new ArrayList<>();
        waterSensors.add(new WaterSensor("sensor_1", 1607527992000L, 20));
        waterSensors.add(new WaterSensor("sensor_1", 1607527994000L, 50));
        waterSensors.add(new WaterSensor("sensor_1", 1607527996000L, 30));
        waterSensors.add(new WaterSensor("sensor_2", 1607527993000L, 10));
        waterSensors.add(new WaterSensor("sensor_2", 1607527995000L, 30));

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> source = env.fromCollection(waterSensors);
        
        //source.process(new myMapProcessFunction()).print();

        SingleOutputStreamOperator<Tuple2<String, Integer>> process = source.keyBy(WaterSensor::getId)
                .process(new KeyedProcessFunction<String, WaterSensor, Tuple2<String, Integer>>() {
                    @Override
                    public void processElement(WaterSensor waterSensor, Context context, Collector<Tuple2<String, Integer>> collector) throws Exception {
                        System.out.println("pojo : "+waterSensor.getId());
                        System.out.println("context : " + context.getCurrentKey());
                        collector.collect(new Tuple2<>("key is " + context.getCurrentKey(), waterSensor.getVc()));
                    }
                });

        process.print();

        env.execute("process");



    }

    public static class myMapProcessFunction extends ProcessFunction<WaterSensor, Tuple2<String,Integer>>{

        @Override
        public void processElement(WaterSensor waterSensor, Context ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
            out.collect(new Tuple2<>(waterSensor.getId(),waterSensor.getVc()));

        }
    }
}
