package com.orange.lin.demo

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}


/**
 * @Description:
 * @author oranglzc
 * @creat 2021-03-30-19:01
 */
object testBatch {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment


    val lineDs: DataSet[String] = env.readTextFile("E:\\gitREP\\orangeforFlink\\studyForFlink\\src\\main\\resources\\word.txt")
    import org.apache.flink.api.scala._


    val wordCountDs: AggregateDataSet[(String, Int)] = lineDs.flatMap(_.split(" ")).map((_, 1))
      .groupBy(0).sum(1)


    wordCountDs.print()




  }
}
