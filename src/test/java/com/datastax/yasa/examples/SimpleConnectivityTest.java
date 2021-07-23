package com.datastax.yasa.examples;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.datastax.astra.sdk.AstraClient;
import com.datastax.stargate.sdk.doc.ApiDocumentClient;
import com.datastax.stargate.sdk.rest.ApiRestClient;
import com.datastax.stargate.sdk.rest.TableClient;
import com.datastax.stargate.sdk.rest.domain.Ordering;
import com.datastax.stargate.sdk.rest.domain.QueryWithKey;
import com.datastax.stargate.sdk.rest.domain.Row;
import com.datastax.stargate.sdk.rest.domain.RowResultPage;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class SimpleConnectivityTest {
	

	@Autowired
	private AstraClient astraClient;
	
	
	private static ApiDocumentClient clientApiDoc;
	private static ApiRestClient clientApiRest;
	
	private static final String WORKING_NAMESPACE    = "enterprise";
    private static final String COLLECTION_PERSON    = "person";
    
    private static final String WORKING_TABLE = "movies_and_tvshows";
    
    public static final String ANSI_RESET           = "\u001B[0m";
    public static final String ANSI_GREEN           = "\u001B[32m";
    public static final String ANSI_YELLOW          = "\u001B[33m";
	
	@Test
	@Order(1)
	@DisplayName("Test connectivity to Astra explicit values")
    public void test_connectivity() {
		
		System.out.println("**************************");
        astraClient.apiDevopsDatabases().databasesNonTerminated().forEach(db-> {
            System.out.println("Database " + db.getId() + " is " + db.getStatus());
            System.out.println("- Dbname:" + db.getInfo().getName());
            System.out.println("- Cloud Provider:" + db.getInfo().getCloudProvider());
            System.out.println("- Keyspace:" + db.getInfo().getKeyspace());
            System.out.println("- Region:" + db.getInfo().getRegion());
        });
    }
	
	@Test
	@Order(2)
	@DisplayName("Create Collection If Not Exists")
	public void create_collection() throws InterruptedException {
		
		clientApiDoc = astraClient.apiStargateDocument();
		
        // Operations on collections
        System.out.println(ANSI_YELLOW + "\n#02 Create Collection" + ANSI_RESET);
        // Create working collection is not present
        if (!clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PERSON).exist()) {
            
	        System.out.println(ANSI_YELLOW + "[OK]" + ANSI_RESET + " - Collection does not exist");
	        // When
	        clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PERSON).create();
	        System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Creation request sent");
	        Thread.sleep(1000);
	        // Then
	        Assertions.assertTrue(clientApiDoc.namespace(WORKING_NAMESPACE).collection(COLLECTION_PERSON).exist());
	        System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Collection now exist");
        }else {
        	System.out.println(ANSI_GREEN + "[OK]" + ANSI_RESET + " - Collection already exists");
        }
    }
	
	@Test
	@Order(3)
	@DisplayName("Create Collection If Not Exists")
	public void test_rest_API() throws InterruptedException {
		
//		clientApiDoc = astraClient.apiStargateDocument();
		
		clientApiRest = astraClient.apiStargateData();
		
        // Operations on collections
		System.out.println(ANSI_YELLOW + "\n#25 Retrieves row from primaryKey" + ANSI_RESET);
		
		// Given
        TableClient table = clientApiRest.keyspace(WORKING_NAMESPACE).table(WORKING_TABLE);
        
        RowResultPage rrp = table.key(2015).find(QueryWithKey.builder()
                        		 .build());
		
        List<Row> rows = rrp.getResults();
        System.out.println("Release Year \t \t Title \t \t \t Director");
        for (int i=0; i<rows.size(); i++) {
        	Row row = rows.get(i);
        	System.out.print(row.getInt("release_year"));
        	System.out.print(" \t\t "+row.getString("title"));
        	System.out.print("\t \t \t"+row.getString("director")+"\n");
        }
    }
    
	
}
