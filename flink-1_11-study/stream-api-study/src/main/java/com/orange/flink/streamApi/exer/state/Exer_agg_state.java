package com.orange.flink.streamApi.exer.state;

import com.orange.flink.streamApi.exer.pojo.WaterSensor;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

public class Exer_agg_state {
    public static void main(String[] args) throws Exception {


        ArrayList<WaterSensor> waterSensors = new ArrayList<>();
        waterSensors.add(new WaterSensor("sensor_1", 1607527992000L, 20));
        waterSensors.add(new WaterSensor("sensor_1", 1607527994000L, 50));
        waterSensors.add(new WaterSensor("sensor_1", 1607527996000L, 30));
        waterSensors.add(new WaterSensor("sensor_2", 1607527993000L, 10));
        waterSensors.add(new WaterSensor("sensor_2", 1607527995000L, 30));

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> source = env.fromCollection(waterSensors);

        KeyedStream<WaterSensor, String> keyedStream = source.keyBy(WaterSensor::getId);

        //获取平均水位
        SingleOutputStreamOperator<Double> avgStateStream = keyedStream.process(new KeyedProcessFunction<String, WaterSensor, Double>() {

            private AggregatingState<Integer, Double> avgState;

            @Override
            public void open(Configuration parameters) throws Exception {
                AggregatingStateDescriptor<Integer, Tuple2<Integer, Integer>, Double> aggregatingStateDescriptor = new AggregatingStateDescriptor<>("avgState", new AggregateFunction<Integer, Tuple2<Integer, Integer>, Double>() {
                    @Override
                    public Tuple2<Integer, Integer> createAccumulator() {
                        return Tuple2.of(0, 0);
                    }

                    @Override
                    public Tuple2<Integer, Integer> add(Integer integer, Tuple2<Integer, Integer> integerIntegerTuple2) {
                        return Tuple2.of(integerIntegerTuple2.f0 + integer, integerIntegerTuple2.f1 + 1);
                    }

                    @Override
                    public Double getResult(Tuple2<Integer, Integer> integerIntegerTuple2) {
                        return integerIntegerTuple2.f0 * 1D / integerIntegerTuple2.f1;
                    }

                    @Override
                    public Tuple2<Integer, Integer> merge(Tuple2<Integer, Integer> integerIntegerTuple2, Tuple2<Integer, Integer> acc1) {
                        return Tuple2.of(integerIntegerTuple2.f0 + acc1.f0, integerIntegerTuple2.f1 + acc1.f1);
                    }
                }, Types.TUPLE(Types.INT, Types.INT));

                avgState = getRuntimeContext().getAggregatingState(aggregatingStateDescriptor);
            }

            @Override
            public void processElement(WaterSensor waterSensor, Context context, Collector<Double> collector) throws Exception {
                avgState.add(waterSensor.getVc());
                collector.collect(avgState.get());
            }
        });

        avgStateStream.print();

        env.execute("avg-state test");
    }



}
