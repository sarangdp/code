package KafkaInt

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

/**
  * Created by Sarang on 15-07-2017.
  */
object RulesCount {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("Rules count").setMaster("yarn-client");

    val topicSet = Set("kafkakp");
    val kafkaParams = Map[String, String] ("metadata.broker.list" -> "nn02.itversity.com:6667");
    val ssc = new StreamingContext(sparkConf, Seconds(60));
    val ruleLogs = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet);

    val logLine = ruleLogs.map(rec => rec._2);
    val rulesCount = logLine.map(rec => (rec.split(":")(1), 1)).reduceByKey(_+_);

    rulesCount.print();
    ssc.start();
    ssc.awaitTermination();
  }
}
