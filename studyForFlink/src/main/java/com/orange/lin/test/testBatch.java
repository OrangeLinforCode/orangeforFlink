package com.orange.lin.test;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author oranglzc
 * @Description:
 * @creat 2021-03-30-19:31
 */
public class testBatch {
    public static void main(String[] args) {


        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> lineDs =
                env.readTextFile("E:\\gitREP\\orangeforFlink\\studyForFlink\\src\\main\\resources\\word.txt");

        FlatMapOperator<String, Tuple2<String, Long>> wordAndOne = lineDs.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] splits = line.split(" ");
            for (String word : splits) {
                out.collect(Tuple2.of(word, 1L));
            }
        })
                //lambda写法会擦除泛型导致编译报错，需要显示接returns
                .returns(Types.TUPLE(Types.STRING, Types.LONG));



    }
}
