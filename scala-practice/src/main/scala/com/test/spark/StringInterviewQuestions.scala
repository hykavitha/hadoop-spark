package com.test.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

/**
 * @author Kavitha Yogaraj
 * 
 */
object StringInterviewQuestions {
     
  /**
   * String reversal 
   */
  def stringRev(s:String) : String = {
    val vec_rev = for (i <- s.length-1 to 0 by -1 ) yield s.charAt(i) 
    val rev_string = vec_rev.mkString("")
    println("The original string is : "+ s)
    println("The reversed string is : "+ rev_string)
    return rev_string
  }
  
  def isPalindrome(s:String) :Boolean = {
    var j = s.length-1
    var isPali = false
    
    for (i <- 0 to s.length/2 ) {
      if (s.charAt(i) != s.charAt(j)) {
        isPali = false
        println("isPali is false : "+ s.charAt(i)+ "\nj is " +s.charAt(j))
      } else
       isPali = true 
       
       j -= 1
    }
    return isPali
  }
  
  def isIntegerPalindrome(i:Int): Boolean ={
    var num = i 
    var rev = 0
        println("number is : "+ num)

    while (num > 0) {
      var rem = num%10
      rev = (rev*10 ) + rem
      num = num / 10
    }
    println("reversed is : "+ rev)

    if (i == rev)
      return true
    else 
      return false
  }
  
  def main(args: Array[String]) = {

     //Start the Spark context
     val conf = new SparkConf().setAppName("WordCount").setMaster("local")
     val sc = new SparkContext(conf)
     val inputString = "reverse_me"
     stringRev(inputString)
     
     
     val inputstring = "madam"
     println("Calling isPalindrome with this input string " +inputstring) 
     println("is the input string palindorme: " + isPalindrome(inputstring))

     val i = 12321
     println("Integer palindrome with this input string " +i) 
     println("is the input integer palindorme: " + isIntegerPalindrome(i))


     sc.stop()
  
  }
}