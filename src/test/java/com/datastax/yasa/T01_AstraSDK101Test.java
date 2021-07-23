package com.datastax.yasa;

import org.junit.jupiter.api.Test;

import com.datastax.astra.sdk.AstraClient;
import com.datastax.astra.sdk.databases.domain.Database;
import com.datastax.astra.sdk.databases.domain.DatabaseInfo;
import com.datastax.astra.sdk.iam.domain.User;
import com.datastax.astra.sdk.streaming.domain.Tenant;

/**
 * This first test show you how to initialize the {@link AstraClient} object
 */
public class T01_AstraSDK101Test {
    
    @Test
    public void initAstraClient() {
        
        // You can create the astraClient
        AstraClient astraClient = AstraClient.builder()
                // Your Astra Instance IDS
                .databaseId("0fd948c4-5f1b-4521-adb5-01de81df6503")
                .cloudProviderRegion("us-east-1")
                // The keyspace you created  
                .keyspace("springboot_doc")
                // App token generated from AstraIO
                .appToken("AstraCS:tvfdeICPGDjneZxhXcDmIYAH:6bd43ac6de0ba4b1e71a332305c2309cf993d67ac9092a96f55df151725366f8")
                .clientId("tvfdeICPGDjneZxhXcDmIYAH")
                .clientSecret("bmeQ,vw0.nmAcMD4F2h7BALBRA7zGsnL,LWGXI,hItW9PpDjYWlG.E,q43+5-+A+v5dEN_FgkilSWKzB,pn_Xk6LPAau7Q4t-uzfH,GhOt,NU-j0_9-iGpMrwKGPyq+J")
                .build();
        
        // ------------------------------
        // DEVOPS APIS: DATABASE 
        // ------------------------------
        
        System.out.println("What are running DB ?\n----");
        astraClient.apiDevopsDatabases()
                   .databasesNonTerminated()
                   .map(Database::getInfo)
                   .map(DatabaseInfo::getName)
                   .forEach(System.out::println);
        
        // ------------------------------
        // DEVOPS APIS: IAM 
        // ------------------------------
        
        System.out.println("\nList of users:\n----");
        astraClient.apiDevopsIam()
                   .users()
                   .map(User::getEmail)
                   .forEach(System.out::println);
        
        // ------------------------------
        // DEVOPS APIS: STREAMING 
        // ------------------------------
        
        System.out.println("\nList of Tenants:\n----");
        astraClient.apiDevopsStreaming()
                   .tenants()
                   .map(Tenant::getTenantName)
                   .forEach(System.out::println);
        
        // ------------------------------
        // CQLSESSION (use Cassandra)
        // 
        // Note; CloudSecureBundle is downloaded if needed for you
        // ------------------------------
        
        System.out.println("\nShow Cassandra Version:\n----");
        System.out.println(astraClient
                .cqlSession().execute("SELECT release_version FROM system.local")
                .one()
                .getString("release_version"));
        
        // ------------------------------
        // DOC API
        // ------------------------------
        
        System.out.println("\nList of namespaces:\n----");
        astraClient.apiStargateDocument()
                   .namespaceNames()
                   .forEach(System.out::println);
        
        // ------------------------------
        // REST API (DATA)
        // ------------------------------
        
        System.out.println("\nList of tables in your keyspace:\n----");
        astraClient.apiStargateData()
                   .keyspace(astraClient.cqlSession().getKeyspace().get().toString())
                   .tableNames()
                   .forEach(System.out::println);
        
    }
    
}
