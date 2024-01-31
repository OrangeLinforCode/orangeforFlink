package com.orange.flink.streamApi.exer;

import com.orange.flink.streamApi.exer.pojo.Person;
import com.orange.flink.streamApi.exer.pojo.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;

import java.util.Arrays;
import java.util.List;

public class Exer_1 {
    public static void main(String[] args) throws Exception {



        List<WaterSensor> waterSensors = Arrays.asList(
                new WaterSensor("ws_001", 1577844001L, 45),
                new WaterSensor("ws_002", 1577844015L, 43),
                new WaterSensor("ws_003", 1577844020L, 42)
        );

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        env.fromCollection(waterSensors)
//                .print().setParallelism(1);
        //env.readTextFile("stream-api-study/input/word.txt").print();

        DataStreamSource<Person> personDataStreamSource = env.fromElements(
                new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2),
                new Person("July", 20)
        );

        SingleOutputStreamOperator<Person> filterPerson = personDataStreamSource.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {
                return person.age >= 20;
            }
        });

        SingleOutputStreamOperator<String> abPerson = filterPerson.map(new myRichMapFunction()).setParallelism(1);

        abPerson.print();


        env.execute("test");

    }


    public static class myMapFunction implements MapFunction<Person,String>{

        @Override
        public String map(Person person) throws Exception {
            if (person.name.length() > 4) {
                System.out.println(person+" : a/b :"+"a");
                return person+" : a/b :"+"a";
            } else {
                System.out.println(person+" : a/b :"+"b");
                return person+" : a/b :"+"b";
            }
        }
    }

    public static class myRichMapFunction extends RichMapFunction<Person, String> {

        @Override
        public void open(Configuration parameters) throws Exception {
            System.out.println("only execute once before map operator");

        }

        @Override
        public String map(Person person) throws Exception {
            if (person.name.length() > 4) {

                System.out.println(person+" : a/b :"+"a");
                return person+" : a/b :"+"a";
            } else {
                System.out.println(person+" : a/b :"+"b");
                return person+" : a/b :"+"b";
            }
        }

        @Override
        public void close() throws Exception {
            System.out.println("only execute once after map operator");
        }


    }



}
