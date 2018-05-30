import sys
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils


# sys.argv = ['trump', 'trump']

# val topics = Array("trump")
# val stream = KafkaUtils.createDirectStream[String, String](ssc,PreferConsistent,
#                                                            Subscribe[String, String](topics, kafkaParams))


if __name__ == "__main__":
    #spark context
    sc = SparkContext(appName="PythonStreamingDirectKafkaTwtStream")
    #streaming context
    ssc = StreamingContext(sc, 2)
    #setting the topic
    brokers, topic = sys.argv[1:]
    #setting up stream
    
    #KafkaUtils.createDirectStream creates a org.apache.spark.streaming.dstream.DStream.
    #To work with that data we use the Dstream.foreach() method to pluck off RDDs as they are created. 
    #Then we use RDD.foreach() to get each object in the RDD. Those objects will be Kafka ConsumerRecords, 
    #i.e., org.apache.kafka.clients.consumer.ConsumerRecord. 
    #You use the ConsumerRecord.value() method to read the message retrieved from the Kafka topic.
    kvs = KafkaUtils.createDirectStream(ssc, [topic],{"metadata.broker.list": brokers})
    lines = kvs.map(lambda x: x[1])
    counts = lines.flatMap(lambda line: line.split(" ")) \
                  .map(lambda word: (word, 1)) \
                  .reduceByKey(lambda a, b: a+b)
            
            
    counts.pprint()
    ssc.start()
    ssc.awaitTermination()
    
   
    
    
# stream.foreachRDD { rdd =>
# rdd.foreach { record =>
# val value = record.value()
# val tweet = scala.util.parsing.json.JSON.parseFull(value)
# val map:Map[String,Any] = tweet.get.asInstanceOf[Map[String, Any]]
# println(map.get("text"))
# }
# }

