package com.orange.flink.streamApi.exer.window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SessionWindowTimeGapExtractor;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class exer_window_1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> socketStream = env.socketTextStream("localhost", 9999);


        //滚动窗口
        SingleOutputStreamOperator<Tuple2<String, Long>> stream = socketStream.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {

            @Override
            public void flatMap(String s, Collector<Tuple2<String, Long>> collector) throws Exception {
                Arrays.stream(s.split(" ")).forEach(word -> collector.collect(new Tuple2<String, Long>(word, 1L)));
            }
        });

        //printTumblingStream(stream);

        //printSlideStream(socketStream);

        printSessionStream(stream,false);

        env.execute("Tumbling Test");
    }


    public static void printTumblingStream(SingleOutputStreamOperator<Tuple2<String, Long>> stream){
        stream.keyBy(t -> t.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(8)))
                .sum(1)
                .print();
    }

    public static void printSlideStream( SingleOutputStreamOperator<Tuple2<String, Long>> stream){

        WindowedStream<Tuple2<String, Long>, String, TimeWindow> slideWindowedStream = stream.keyBy(t -> t.f0)
                .window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(1)));

        slideWindowedStream.sum(1).print();
    }
    public static void printSessionStream( SingleOutputStreamOperator<Tuple2<String, Long>> stream,Boolean flag){

        if(flag == false){
            //静态gap
            stream.keyBy(t -> t.f0)
                    .window(ProcessingTimeSessionWindows.withGap(Time.seconds(3)))
                    .sum(1).print();
        }else{
            //动态gap
            stream.keyBy(t -> t.f0)
                    .window(ProcessingTimeSessionWindows.withDynamicGap(new SessionWindowTimeGapExtractor<Tuple2<String, Long>>() {
                        @Override
                        public long extract(Tuple2<String, Long> elem) {
                            return elem.f0.length()*1000;
                        }
                    }))
                    .sum(1).print();
        }


    }


}