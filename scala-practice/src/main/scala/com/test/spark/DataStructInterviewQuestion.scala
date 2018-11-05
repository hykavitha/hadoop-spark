package com.test.spark


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

/**
 * @author Kavitha Yogaraj
 * 
 * 
 */
object DataStructInterviewQuestion {
  
  def getKeyValuePairs(lst : List[(Int, Int)]) :Unit = {
    //var map1 = new collection.mutable.Map(Int, Int)
    var map1 = scala.collection.mutable.HashMap.empty[Int,List[Any]]
    for(key<-lst) { 
      println(key)
      if(map1.contains(key._1)){
              var values = map1.get(key._1).seq.flatten.toList
              println("The key values are: " + values)
              values = values :+ key._2
              map1.put(key._1, values)
      }
      else {
        var values = List(key._2)
         map1.put(key._1,values )
      } 
    }
    println("--------------")
    map1.foreach(println)
//    this returns me 
    //(2,List(20, 200))
   //(1,List(10, 100))

    return map1
  }
  
  
   def main(args: Array[String]) = {
     //Start the Spark context
     val conf = new SparkConf().setAppName("WordCount").setMaster("local")
     val sc = new SparkContext(conf)
     
     val list_tuple = List((1, 10) ,(1,100), (2,20), (2,200) )
    getKeyValuePairs(list_tuple)
     // returns
     //(2,List(20, 200))
     //(1,List(10, 100))
   
   }
  
  
}