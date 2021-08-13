package com.datastax.examples;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.*;
import au.com.bytecode.opencsv.CSVReader;


import org.apache.pulsar.client.api.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.datastax.astra.sdk.*;
import com.datastax.enterprise.banking.PendingTransaction;
import com.datastax.enterprise.docapi.person.Person;
import com.datastax.enterprise.docapi.person.Person.Address;
import com.datastax.enterprise.iot.CSV;
import com.datastax.enterprise.iot.Power;
import com.datastax.stargate.sdk.doc.ApiDocument;
import com.datastax.stargate.sdk.doc.ApiDocumentClient;
import com.datastax.stargate.sdk.doc.CollectionClient;
import com.datastax.stargate.sdk.doc.DocumentClient;
import com.datastax.stargate.sdk.doc.domain.DocumentResultPage;
import com.datastax.stargate.sdk.doc.domain.SearchDocumentQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@TestMethodOrder(OrderAnnotation.class)
public class DocumentApiTest extends AbstractAstraIntegrationTest{
	
	private static final String WORKING_NAMESPACE    = "enterprise";
    private static final String COLLECTION_PERSON    = "person";
    private static final String COLLECTION_PENDING_TRANSACTIONS    = "pendingtransactions";
    
    private static final String SERVICE_URL = "pulsar+ssl://pulsar-aws-useast2.streaming.datastax.com:6651";
    
    
    private static ApiDocumentClient clientApiDoc;
    ObjectMapper mapper = new ObjectMapper();
    
    @BeforeAll
    public static void config() {
        System.out.println(ANSI_YELLOW + "[T04_DocumentApi_IntegrationTest]" + ANSI_RESET);
        //initDb("sdk_test_docApi");
        
        
        client = AstraClient.builder()
                .databaseId("397d302e-1a86-4186-aec9-bf45a1f7b511")
                .cloudProviderRegion("us-east-1")
                .appToken("AstraCS:tnlKAtjrRvOIwOkZtlhTNWWG:8404f8a0c6130fac0f8aa5bc94b294113c8d52bdda131956c69c2fb90f6b7c5c")
                .build();
        
        
        clientApiDoc = client.apiStargateDocument();
        System.out.println("Database ID = "+client.getDatabaseId());
        
        client.apiDevopsDatabases().databasesNonTerminated().forEach(db -> {
        	System.out.println(db.getId()+ "_"+db.getInfo().getName());
        });
        
        /*
        // Not available in the doc API anymore as of now
        if (!client.apiStargateDocument().namespace(WORKING_NAMESPACE).exist()) {
            client.apiDevopsDatabases().database(dbId.get())
                  .createNamespace(WORKING_NAMESPACE);
            System.out.print(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Creating namespace ");
            while(!client.apiStargateDocument().namespace(WORKING_NAMESPACE).exist()) {
                System.out.print(ANSI_GREEN + "\u25a0" +ANSI_RESET); 
                waitForSeconds(1);
            }
            System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Namespace created");
        } 
        */
       
    }
    
    /*
    @Test
    @Order(1)
    @DisplayName("Parameter validations should through IllegalArgumentException(s)")
    public void builderParams_should_not_be_empty() {
        System.out.println(ANSI_YELLOW + "\n#01Checking required parameters " + ANSI_RESET);
        Assertions.assertAll("Required parameters",
                () -> Assertions.assertThrows(IllegalArgumentException.class, 
                        () -> { AstraClient.builder().databaseId(null); }),
                () -> Assertions.assertThrows(IllegalArgumentException.class, 
                        () -> { AstraClient.builder().databaseId(""); }),
                () -> Assertions.assertThrows(IllegalArgumentException.class, 
                        () -> { AstraClient.builder().cloudProviderRegion(""); }),
                () -> Assertions.assertThrows(IllegalArgumentException.class, 
                        () -> { AstraClient.builder().cloudProviderRegion(null); }),
                () -> Assertions.assertThrows(IllegalArgumentException.class, 
                        () -> { AstraClient.builder().appToken(""); }),
                () -> Assertions.assertThrows(IllegalArgumentException.class, 
                        () -> { AstraClient.builder().appToken(null); })
        );
        System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Astra DB Validation Successful ");
    }
    */
    
    
    @Test
    @Order(2)
    @DisplayName("Create collection")
    public void should_create_collection() throws InterruptedException {
        // Operations on collections
        System.out.println(ANSI_YELLOW + "\n#02 Create Collection" + ANSI_RESET);
        // Create working collection is not present
        if (!clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PENDING_TRANSACTIONS).exist()) {
            
	        System.out.println(ANSI_YELLOW + "[OK]" + ANSI_RESET + " - Collection does not exist");
	        // When
	        clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PENDING_TRANSACTIONS).create();
	        System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Creation request sent");
	        Thread.sleep(1000);
	        // Then
	        Assertions.assertTrue(clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PENDING_TRANSACTIONS).exist());
	        System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Collection now exist");
        }else {
        	System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Collection already exists");
        }
    }
    
    /*
    @Test
    @Order(3)
    @DisplayName("Create document")
    public void should_create_newDocument() throws InterruptedException, JsonMappingException, JsonProcessingException {
        System.out.println(ANSI_YELLOW + "\n#09 Create document" + ANSI_RESET);
        // Given
        CollectionClient collectionPersonAstra = clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PENDING_TRANSACTIONS);
//        Assertions.assertTrue(collectionPersonAstra.exist());
        UUID uid = UUID.randomUUID();
        if (clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PENDING_TRANSACTIONS).exist()) {
        	
        	System.out.println("Collection pendingtransactions already exists ");
			  PendingTransaction transaction = new PendingTransaction();
			  transaction.setTransactionId(uid.toString());
			  transaction.setCorrelationId(101);
			  transaction.setMessage("Sample Transaction");
			  transaction.setCreatedBy("System");
			  transaction.setStatus("Completed");
			  transaction.setUserId(11011);
			  
			  collectionPersonAstra.create(transaction);
			  
			  System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Document created with ID = "+uid.toString());
        }else {
        	 System.out.println(ANSI_YELLOW + "[OK]" + ANSI_RESET + " - Could NOT create Pending Transaction with ID = "+uid.toString());
        }
        
        // When
//        Person person = new Person("loulou", "looulou", 20, new Address("Paris", 75000));
//        String docId = collectionPersonAstra.create(person);
//        String docId = "fbf16b0e-d1a8-4e75-83fe-59dc235d4f87";
        
//        String json = "{\n"
//    			+ "  \"firstname\" : \"Ram\",\n"
//    			+ "  \"lastname\" : \"Palagummi\",\n"
//    			+ "  \"age\" : 20,\n"
//    			+ "  \"countries\" : null,\n"
//    			+ "  \"address\" : {\n"
//    			+ "    \"street\" : \" 101 Lala Ln \",\n "
//    			+ "    \"city\" : \"Los Angeles\",\n"
//    			+ "    \"zipCode\" : 95000\n"
//    			+ "  }\n"
//    			+ "}";  
//    	
//        System.out.println(json);
//        Person person = mapper.readValue(json, Person.class);
//    	collectionPersonAstra.document(docId).upsert(person);
        
//        try {
//        	String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
//        	System.out.println(jsonString);
//        }catch(Exception e) {
//        	e.printStackTrace();
//        } 
        
        
        
        // Then
//        Assertions.assertNotNull(docId);
        Thread.sleep(1000);
//        Assertions.assertTrue(collectionPersonAstra.document(docId).exist());
//        System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Document created with ID = "+uid.toString());
    }
   */ 
    
    /*
    @Test
    @Order(4)
    @DisplayName("Search Query")
    public void should_search_withQuery() {
        System.out.println(ANSI_YELLOW + "\n#15 Find with where clause" + ANSI_RESET);
        // Given
        CollectionClient collectionPersonAstra = clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PENDING_TRANSACTIONS);
        Assertions.assertTrue(collectionPersonAstra.exist());

       
        // Create a query
        SearchDocumentQuery query = SearchDocumentQuery.builder().where("status").isEqualsTo("Pending").build();
        System.out.println("Query = " + query.toString());

        // Execute q query
        DocumentResultPage<PendingTransaction> results = collectionPersonAstra.searchPageable(query, PendingTransaction.class);
        Assert.assertNotNull(results);
        if(results.getResults().isEmpty())
        	System.out.println(ANSI_YELLOW + ANSI_RESET + " - Document list NOT found");
        else
        	System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Document list found");
        
        for (ApiDocument<PendingTransaction> pendingTransaction : results.getResults()) {
//            Assert.assertNotNull(Person);
            
            String documentId = pendingTransaction.getDocumentId();
            PendingTransaction pt = pendingTransaction.getDocument();
        	System.out.println("Document ID = "+documentId);
            System.out.println("Transaction ID = "+pt.getTransactionId());
            System.out.println("Status = "+pt.getStatus());
            
            if(!pt.getStatus().equals("Completed")) {
            	 pt.setStatus("Completed");
                 collectionPersonAstra.document(documentId).upsert(pt);
                 System.out.println("Set the status to Completed");
            }
            
        }
        
    }
    
    @Test
    @Order(5)
    @DisplayName("Astra Pulsar Producer")
    public void test_producer() throws IOException, InterruptedException{
    	

    	System.out.println("Starting Pulsar Producer ....");
    	// Create client object
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .authentication(
                    AuthenticationFactory.token("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnQ7YzY3NmJhNWItNTEwOS00ZjlkLTgzNWYtM2U5YzE5MTJkYjFkO2MzUnlaV0Z0WkdWdGJ3PT0ifQ.iRGG9va_xDuITc57MSUm-FTx_W1XVyNqqd6KSi75-YetP62rFdbmxfUowOEaWuEYzqlHUhZzeQEEfyfVA-ArANYqm9erCxMwVOtgmgHGXnExUUfNpTvvokuYKS3Wx0IAPmIaRMOd8JFwjeczDyaMQsRXPKG954etHLImwHtsJ_abeSy53YY5ENJcfY2Kc5En8RaBX9c9WzBsXpbi2MRQLYOZWyeMJBERQRSactu_d6IUO_G22_7IfZlsngzf8DzF9iSmgxpgwjsrzoihPQWjWTKSYCgWPY01_iRdIxSQJROBOOCywMPHUrecJZwRRc2I1_Kcjzm8-pDuJJ46c-Pf5Q")
                )
                .build();

        // Create producer on a topic
        Producer<byte[]> producer = client.newProducer()
                .topic("persistent://streamdemo/default/demotopic")
                .create();

        // Send a message to the topic
        producer.send("Hello Ram".getBytes());
        
        System.out.println("Sent the message  ....");

        //Close the producer
        producer.close();

        // Close the client
        client.close();

    }

    
    @Test
    @Order(6)
    @DisplayName("Astra Pulsar Consumer")
    public void test_consumer() throws IOException, InterruptedException{
    	
    	System.out.println("Starting Pulsar Consumer ....");
            // Create client object
            PulsarClient client = PulsarClient.builder()
                    .serviceUrl(SERVICE_URL)
                    .authentication(
                        AuthenticationFactory.token("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnQ7YzY3NmJhNWItNTEwOS00ZjlkLTgzNWYtM2U5YzE5MTJkYjFkO2MzUnlaV0Z0WkdWdGJ3PT0ifQ.iRGG9va_xDuITc57MSUm-FTx_W1XVyNqqd6KSi75-YetP62rFdbmxfUowOEaWuEYzqlHUhZzeQEEfyfVA-ArANYqm9erCxMwVOtgmgHGXnExUUfNpTvvokuYKS3Wx0IAPmIaRMOd8JFwjeczDyaMQsRXPKG954etHLImwHtsJ_abeSy53YY5ENJcfY2Kc5En8RaBX9c9WzBsXpbi2MRQLYOZWyeMJBERQRSactu_d6IUO_G22_7IfZlsngzf8DzF9iSmgxpgwjsrzoihPQWjWTKSYCgWPY01_iRdIxSQJROBOOCywMPHUrecJZwRRc2I1_Kcjzm8-pDuJJ46c-Pf5Q")
                    )
                    .build();

            // Create consumer on a topic with a subscription
            Consumer consumer = client.newConsumer()
                    .topic("streamdemo/default/demotopic")
                    .subscriptionName("my-subscription")
                    .subscribe();

            boolean receivedMsg = false;
            // Loop until a message is received
            do {
                // Block for up to 1 second for a message
                Message msg = consumer.receive(1, TimeUnit.SECONDS);

                if(msg != null){
                    System.out.printf("Message received: %s", new String(msg.getData()));

                    // Acknowledge the message to remove it from the message backlog
                    consumer.acknowledge(msg);

                    receivedMsg = true;
                }

            } while (!receivedMsg);

            //Close the consumer
            consumer.close();

            // Close the client
            client.close();

    }
    */
    
   
    
    
    @Test
    @Order(7)
    @DisplayName("Read JSON")
    public void json_reader() throws IOException, InterruptedException, JSONException{
    	String file = "Wearables-DFE.csv";
    	
    	String[] keys = new String[9];
        System.out.println("----------------");
        convertCSV2JSON("household-power-consumption-QueryResult.csv");
    	
    }
    
   
    public void convertCSV2JSON(String csvFile) throws FileNotFoundException, IOException{
    	
    	try (InputStream in = new FileInputStream(csvFile);) {
    	    CSV csv = new CSV(true, ',', in );
    	    List < String > fieldNames = null;
    	    if (csv.hasNext()) fieldNames = new ArrayList < > (csv.next());
    	    List < Map < String, String >> list = new ArrayList < > ();
    	    while (csv.hasNext()) {
    	        List < String > x = csv.next();
    	        Map < String, String > obj = new LinkedHashMap < > ();
    	        for (int i = 0; i < fieldNames.size(); i++) {
    	            obj.put(fieldNames.get(i), x.get(i));
    	        }
    	        list.add(obj);
    	        Power power = mapper.convertValue(obj, Power.class);
    	        System.out.println("Voltage = "+power.getVoltage());
    	    }
    	    
    	}

    }
    
    /**
    public List<String[]> readCSV(String csvName) throws IOException {
        File file = new File(csvName);
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        CSVReader csvReader = new CSVReader(new InputStreamReader(dataInputStream,"utf-8"));
        List<String[]> stringsList = csvReader.readAll();
        return stringsList;
    }
    
    public JSONObject[] csv2JSON(String[] keys,List<String[]> stringsList) throws JSONException {
    	 
        JSONObject[] jsons = new JSONObject[stringsList.size()];
        int index = 0 ;
        for (String[] strings : stringsList
             ) {
            JSONObject json = this.csv2JSON(keys, strings);
            jsons[index] = json;
            index ++ ;
        }
        return jsons;
    }

    
    public JSONObject csv2JSON(String[] keys,String[] values) throws JSONException {
    	 
        JSONObject json = new JSONObject();
        for (int i = 0; i < keys.length; i++) {
            try{
                json.append(keys[i],values[i]);
            }
            catch (ArrayIndexOutOfBoundsException e){
                json.append(keys[i],null);
            }
        }
//        System.out.println(json.toString());
        return json;
    }
    */

    
}
