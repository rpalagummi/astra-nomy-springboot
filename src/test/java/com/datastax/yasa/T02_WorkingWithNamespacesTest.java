package com.datastax.yasa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.astra.sdk.AstraClient;

@SpringBootTest
public class T02_WorkingWithNamespacesTest {
    
    /**
     * Coming from the astra Spring boot starter
     * - Load properties astra.* from application.yaml
     * - DevopsApi        : if token provided
     * - RestAPi + DocApi : if token,databaseid,cloudregion provided
     * - CqlSession       : if clientid,clientsecret,databaseid, cloudregion provided
     */
    @Autowired
    private AstraClient astraClient;
    
    @Test
    public void should_create_namespace() {
        String ns1 = "ns1";
        // DB should exist
        if (astraClient.apiDevopsDatabases()
                       .database(astraClient.getDatabaseId().get())
                       .exist()) {
            System.out.println("Database found, checking namespace:");
            // Target namespace should NOT exist
            if (!astraClient.apiStargateDocument()
                            .namespace(ns1)
                            .exist()) {
                astraClient.apiDevopsDatabases()
                           .database(astraClient.getDatabaseId().get())
                           .createNamespace(ns1);
                System.out.println("Creating namepace ns1.");
            } else {
                System.out.println("Namespace ns1 found - nothing to do");
            }
        }
    }
    

}
