package com.datastax.yasa.docapi.iot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.astra.sdk.AstraClient;
import com.datastax.stargate.sdk.doc.CollectionClient;
import com.datastax.stargate.sdk.doc.StargateDocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class IoTRepository extends StargateDocumentRepository<Power> {
	
private static final String WORKING_NAMESPACE    = "enterprise";
    
    private static final String COLLECTION_IOT_SENSORS    = "powersensors";
    
    ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
	private AstraClient astraClient;
	
	private CollectionClient collectionClient;
	
	/**
     * Constructor from {@link AstraClient}.
     * 
     * @param astraClient
     *      client for Astra
     */
    public IoTRepository(AstraClient astraClient) {
        super(astraClient.getStargateClient(), 
              astraClient.cqlSession().getKeyspace().get().toString());
        collectionClient = astraClient.apiStargateDocument().namespace(WORKING_NAMESPACE).collection(COLLECTION_IOT_SENSORS);
        if(!collectionClient.exist()) {
        	System.out.println("Creating Collection :"+COLLECTION_IOT_SENSORS);
        	collectionClient.create();
        }
    }
    
    public void createIoTDocument(Power doc) {
    	collectionClient.create(doc);
    }

}
