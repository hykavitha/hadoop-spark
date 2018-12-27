package com.loyalty.kafka.couchbase;

import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.query.*;
import com.loyalty.kafka.constants.CouchbaseConstants;;

public class CBQuery {
	public Bucket bucket = null;
	Cluster cluster = null;
    public CBQuery(){
    	this.cluster = CouchbaseCluster.create(CouchbaseConstants.COUCHBASE_INSTANCE);
        this.cluster.authenticate(CouchbaseConstants.USERNAME, CouchbaseConstants.PASSWORD);
        this.bucket = cluster.openBucket(CouchbaseConstants.BUCKET);
    }
//    public static void main(String... args) throws Exception {
//        CBQuery cb_obj = new CBQuery();
//        // Initialize the Connection        
//        
//        cb_obj.upSert();
//        cb_obj.insert();
//        cb_obj.update();
//        cb_obj.getId("123");
//      
//    }
    
    public void getAllDoc (){
        N1qlQueryResult result = bucket.query(
                N1qlQuery.simple("SELECT * FROM `Devpoc` ")
            );

            // Print each found Row
            for (N1qlQueryRow row : result) {
                System.out.println(row);
            }
    }
	public void getId(String ID ) {

	      N1qlQueryResult result = this.bucket.query(
	              N1qlQuery.parameterized("SELECT * FROM `bucketname`  WHERE meta().id =",	            		  
	              JsonArray.from(ID)));

	      // Print each found Row
	      System.out.println("\n***********************************\n");
	          for (N1qlQueryRow row : result) {
	              // Prints {"name":"Arthur"}
	              System.out.println(row);
	          }
		 System.out.println("\n***********************************\n");
	}
	
	public void insert() {
		// TODO Auto-generated method stub
		
	}
	public void update() {
		// TODO Auto-generated method stub
		
	}
	public void upSert() {
		
		  // Create a JSON Document
        JsonObject arthur = JsonObject.create()
            .put("name", "Arthur")
            .put("email", "kingarthur@couchbase.com")
            .put("interests", JsonArray.from("Holy Grail", "African Swallows"));

        // Store the Document
        this.bucket.upsert(JsonDocument.create("u:king_arthur", arthur));

        // Load the Document and print it
        // Prints Content and Metadata of the stored Document
        System.out.println(bucket.get("u:king_arthur"));

        // Create a N1QL Primary Index (but ignore if it exists)
        this.bucket.bucketManager().createN1qlPrimaryIndex(true, false);

        // Perform a N1QL Query
        N1qlQueryResult result = bucket.query(
            N1qlQuery.parameterized("SELECT name FROM `bucketname` WHERE $1 IN interests",
            JsonArray.from("African Swallows"))
        );

        // Print each found Row
        for (N1qlQueryRow row : result) {
            // Prints {"name":"Arthur"}
            System.out.println(row);
        }		
	}
}