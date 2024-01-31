package com.orange.lin.test;


import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;
import scala.collection.Iterable;


import java.sql.Timestamp;
import java.util.Collection;

public class testwindow_Tumbling {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> socketTextStream = env.socketTextStream("localhost", 9999);

        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMapStream = socketTextStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String in, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] strings = in.split(" ");
                for (String word : strings) {
                    out.collect(new Tuple2<>(word, 1));
                }

            }
        });

        KeyedStream<Tuple2<String, Integer>, String> keyedStream = flatMapStream.keyBy(data -> data.f0);

        WindowedStream<Tuple2<String, Integer>, String, TimeWindow> windowedStream = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(5)));

        //SingleOutputStreamOperator<Tuple2<String, Integer>> result = windowedStream.sum(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> result = windowedStream.aggregate(new myAggFunc(), new myWindowFunction());

        result.print();

        env.execute();
    }

    private static class myAggFunc implements AggregateFunction<Tuple2<String,Integer>,Integer,Integer> {

        @Override
        public Integer createAccumulator() {
            return 0;
        }

        @Override
        public Integer add(Tuple2<String, Integer> value, Integer accumulator) {
            return accumulator+1;
        }

        @Override
        public Integer getResult(Integer accumulator) {
            return accumulator;
        }

        @Override
        public Integer merge(Integer a, Integer b) {
            return a+b;
        }


    }


    public static class myWindowFunction implements WindowFunction<Integer,Tuple2<String,Integer>,String,TimeWindow> {


        @Override
        public void apply(String key, TimeWindow window, java.lang.Iterable<Integer> input, Collector<Tuple2<String, Integer>> out) throws Exception {
            Integer next = input.iterator().next();

            out.collect(new Tuple2<>(new Timestamp(window.getStart()) +":"+key,next));
        }
    }
}
