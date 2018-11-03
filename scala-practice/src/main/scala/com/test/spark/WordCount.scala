package com.test.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

/**
 * @author Kavitha Yogaraj
 */

object WordCount {
  
 
  /**
   * Saves the word & its count in the word_count.txt file, 
   */
  def getWordCount(sc: SparkContext, input : String) : Unit = {
         val input = sc.textFile("input.txt")
     input.flatMap { line => line.split(" ")}
   .map{word => (word, 1) }
    .reduceByKey(_ + _) //Sum values with same key
   .saveAsTextFile("word_count.txt") //Save result to a text file
    
  }
 
  /**
   * Returns the maximum length and the word among all the words in that file
   */
  def getMaxLengthWord(sc: SparkContext, input : String) : (Int, String)  = {
         val lines = sc.textFile("input.txt")
         val length = lines.flatMap(line => line.split(" ")).map(word => (word.length, word))
         print("Maximum length is : " +length.max)
         return length.max
  }
    
  def main(args: Array[String]) = {

     //Start the Spark context
     val conf = new SparkConf().setAppName("WordCount").setMaster("local")
     val sc = new SparkContext(conf)
    
     //Read input file
     //val input = sc.textFile("input.txt")
      getWordCount(sc, "input.txt")
      getMaxLengthWord(sc, "input.txt")
     //StoppingSpark context
     sc.stop()
  }
  
  }