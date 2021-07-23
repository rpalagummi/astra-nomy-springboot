package com.datastax.yasa;

import org.junit.jupiter.api.Test;

import com.datastax.astra.sdk.AstraClient;

public class Demo {
    
    @Test
    public void connect() {
        AstraClient client = AstraClient.builder()
                .databaseId("astra_cluster_id")           // Unique identifier for your instance
                .cloudProviderRegion("astra_db_region")   // Cloud Provider region picked for you instance
                .keyspace("ks1")                          // (optional) Set your keyspace

                .appToken("AstraCS:......")               // App Token will be used as ApiKey for Devops, Docs and REST Api.
                .clientId("TWRvjlcrgfZYfhcxGZhUlAAA")     // Will be used as your username
                .clientSecret("7xKSrZPLbWxDJ0WXyj..")     // Will be used as your password

                .secureConnectBundle("/tmp/sec.zip")      // (optional) if not provided download in ~/.astra
                .build();
        
        
    }

}
