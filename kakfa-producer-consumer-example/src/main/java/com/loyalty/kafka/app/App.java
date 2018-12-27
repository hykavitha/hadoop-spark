package com.loyalty.kafka.app;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import com.loyalty.kafka.constants.IKafkaConstants;
import com.loyalty.kafka.couchbase.CBQuery;
import com.loyalty.kafka.cli.ConsumerCreator;
import com.loyalty.kafka.cli.ProducerCreator;

public class App {
	public  static CBQuery cb_obj = null;
    public static void main(String[] args) {
    	cb_obj = new CBQuery();
    	int sequenceId = 1;
    	//ProducerCreator.runProducer(IKafkaConstants.TOPIC_NAME_1, sequenceId);
    	runConsumerProducer();
//    	boolean isProduce = false ;
//    	runConsumer(IKafkaConstants.TOPIC_NAME_2, isProduce);
    }
   
    
    private static void runConsumerProducer() {
    	boolean isProduce = true;
		runConsumer( IKafkaConstants.TOPIC_NAME_1, isProduce);	
	}
    
    static int fromByteArray(byte[] bytes) {
        return new BigInteger(bytes).intValue();
    }
    
    static void readMessages(Iterator iterator, Boolean isProduce){
        ConsumerRecord<Long, String> record = null;
        
        while(iterator.hasNext()){
              record = (ConsumerRecord<Long, String>) iterator.next();
                            
              String cid = "1";
              try{
            	  cid =  new String(record.headers().lastHeader("CID").value());
              }catch (Exception e){
            	  System.err.println("Exception at CID");
              }
              System.out.println(record.headers());
             
              int sequenceId = 0 ;
              try{
               sequenceId = fromByteArray(record.headers().lastHeader("sequenceId").value()); 
              } catch (Exception e){
            	  System.err.println("Exception at sequenceId");
              }
              
              System.out.printf("Message Received ==>> topic = %s, cid =%s ,sequenceId = %s, message = %s, \n", 
            		  record.topic(),cid,sequenceId, record.value());
              
              System.out.printf("Message Received Value is ==>> %s, \n", 
            		  record.value());
              
              
              System.out.printf("Message Received ==>> timestamp = %s, partition = %s, offset = %d\n", 
            		 record.partition(), record.timestamp(), record.offset());
                
              cb_obj.getId(record.value());

          if( isProduce) {
            	ProducerCreator.produceforThisCID(IKafkaConstants.TOPIC_NAME_2 , record.value(), cid, sequenceId);
            	System.out.println("Done producing message for CID: " +cid);
          }
//          
      	  }
      	
      	System.out.println("Done reading messages: ");
    	return;
    }



    /*
     * run this consumer 
     */
	static void runConsumer(String topic, Boolean isProduce) {
       
    	Consumer<String, String> consumer = ConsumerCreator.createConsumer();
		consumer.subscribe(Collections.singletonList(topic));
		System.out.println("************ consumer with topic : " + topic);
    	int noMessageFound = 0;
        
    	while (true) {
          ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
          // 1000 is the time in milliseconds consumer will wait if no record is found at broker.
          Iterator iterator =  consumerRecords.iterator();
          if (consumerRecords.count() == 0) {
              noMessageFound++;
              if (noMessageFound > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                // If no message found count is reached to threshold exit loop.  
                break;
              else
                  continue;
          }  
          readMessages(iterator, isProduce);
          //commits the offset of record to broker. 
          consumer.commitAsync();
        }
    consumer.close();
    System.out.println("---------Done with runnig consumer-------");
    }
   
  
}
