package com.loyalty.kafka.cli;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.loyalty.kafka.constants.IKafkaConstants;

import org.apache.kafka.common.header.Headers;

public class ProducerCreator {


	public static void produceforThisCID(String topic, String text,  String cid, int sequenceId) {

		Producer<String, String> producer = ProducerCreator.createProducer();
		System.out.println("*********** Producer with topic***** : " +topic);
		
		//updating the message and sequenceId
		sequenceId ++;
		text += " updated this messgae by topic : " + topic;

		ProducerRecord<String, String> recordToSend = new 
				ProducerRecord<String, String>(topic, "message",
						text + " , timeInMillis=" + System.currentTimeMillis());

		Headers headers = recordToSend.headers();

		headers.add("CID", cid.getBytes());
		headers.add("sequenceId", intToByte(sequenceId));

		
		System.out.printf("Message Sending ==>> topic = %s, cid =%s , sequenceId = %d, message = %s, \n", 
        		  topic,cid,sequenceId, text);
		
		try {
			// synchronous send.... get() waits for the computation to
			// finish
			RecordMetadata rmd = producer.send(recordToSend).get();
			System.out.printf("Message Sent ==>> partition = %s, offset = %d\n", rmd.topic(),
					rmd.partition(), rmd.offset());
			System.out.println("--------------------------------------");
		} catch (Exception ex) {
			// this is test code...so don't judge me !!
			ex.printStackTrace();
		}
		producer.close();


	}

	private static byte[] intToByte( final int i ) {
	    BigInteger bigInt = BigInteger.valueOf(i);      
	    return bigInt.toByteArray();
	}

	public static void runProducer(String topic, int sequenceId) {
		// produce a test message
		// if u run this multiple times ... u will have multiple messages in the
		// test_topic topic (as would be expected)
		Producer<String, String> producer = ProducerCreator.createProducer();
		System.out.println("*********** Producer with topic ***** : " +topic);
		Scanner sc = new Scanner(System.in);
		try {
			while(true) {
				System.out.print(">>>> ");
				String text = sc.nextLine();


				ProducerRecord<String, String> recordToSend = new 
						ProducerRecord<String, String>(topic, "message",
								text + " , timeInMillis=" + System.currentTimeMillis());

				Headers headers = recordToSend.headers();

				String cid = ProducerCreator.getUniqueID();
				headers.add("CID", cid.getBytes());
				headers.add("sequenceId", intToByte(sequenceId));

				
				System.out.printf("Message Sending ==>> topic = %s, cid =%s , sequenceId = %d, message = %s, \n", 
	            		  topic,cid,sequenceId, text);

				try {
					// synchronous send.... get() waits for the computation to
					// finish
					RecordMetadata rmd = producer.send(recordToSend).get();
					System.out.printf("Message Sent ==>> topic = %s, timestamp = %s, partition = %s, offset = %d\n", rmd.topic(),
							rmd.partition(), rmd.timestamp(), rmd.offset());
					
				} catch (Exception ex) {
					// this is test code...so don't judge me !!
					ex.printStackTrace();
				}

				if (text.equalsIgnoreCase("exit")) {
					break;
				}
			}
		} finally {
			sc.close();
		}
	}

	private static String getUniqueID() {
		return UUID.randomUUID().toString();
	}

	private static Producer<String, String> createProducer() {
		Properties kafkaProps = new Properties();
		kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
		kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
		kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		return new KafkaProducer<String, String>(kafkaProps);
	}




}