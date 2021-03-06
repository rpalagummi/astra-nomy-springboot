package com.datastax.enterprise.banking;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.astra.sdk.AstraClient;
import com.datastax.enterprise.docapi.person.Person.Address;
import com.datastax.stargate.sdk.rest.ApiRestClient;
import com.datastax.stargate.sdk.doc.ApiDocument;
import com.datastax.stargate.sdk.doc.CollectionClient;
import com.datastax.stargate.sdk.doc.StargateDocumentRepository;
import com.datastax.stargate.sdk.doc.domain.DocumentResultPage;
import com.datastax.stargate.sdk.doc.domain.SearchDocumentQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Banking Transactions Repository, we want to show the gate.
 *
 * @author Ram Palagummi 
 */
@Repository
public class TransactionDocumentAPIRepository extends StargateDocumentRepository<PendingTransaction> {
    
    
	private static final String WORKING_NAMESPACE    = "enterprise";
    
    private static final String COLLECTION_PENDING_TRANSACTIONS    = "pendingtransactions";
    
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
    public TransactionDocumentAPIRepository(AstraClient astraClient) {
        super(astraClient.getStargateClient(), 
              astraClient.cqlSession().getKeyspace().get().toString());
        collectionClient = astraClient.apiStargateDocument().namespace(WORKING_NAMESPACE).collection(COLLECTION_PENDING_TRANSACTIONS);
    }
    
    @PostConstruct
    public void generateBitData() {
        
    }
    
    /**
     * Sample of custom code
     * @param lastName
     * @return
     */
    public DocumentResultPage<PendingTransaction> findByTransactionId(String transactionId) {
    	    	
        return searchPageable(SearchDocumentQuery.builder()
                .where("transaction_id").isEqualsTo(transactionId)
                .build());
    }
    
     
    public void createTransaction(String docId, PendingTransaction doc) {
    	collectionClient.create(doc);
    }
    
    
    public String updateTransaction(String docId, PendingTransaction doc) {
    	
    	collectionClient.document(docId).upsert(doc);
    	return docId;
    }
    
    public List<ApiDocument<PendingTransaction>> searchTransactionsWithQuery(String column, String value){
    	
    	SearchDocumentQuery query = SearchDocumentQuery.builder().where(column).isEqualsTo(value).build();
    	DocumentResultPage<PendingTransaction> results = collectionClient.searchPageable(query, PendingTransaction.class);
    	System.out.println(" Number of Transactions Found : "+results.getResults().size());
    	return results.getResults();
    }
    
    public void updateDocuments(String column, String currentValue, String toValue) {
    	
    	List<ApiDocument<PendingTransaction>> results = searchTransactionsWithQuery(column, currentValue);
    	for (ApiDocument<PendingTransaction> pendingTransaction : results) {
          
    		PendingTransaction pt = pendingTransaction.getDocument();
    		pt.setStatus(toValue);
    		updateTransaction(pendingTransaction.getDocumentId(), pt);
    	}
    	System.out.println(" Total Transactions updated = "+results.size());
    	
    }
    
    public String updateSubDocument(String docId, String path, Address a) {
    	
    	System.out.println("Updating the Sub Document "+path);
    	collectionClient.document(docId).replaceSubDocument(path, a);
    	return docId;
    }
    
	 public String updateSubDocument(String docId, String path, String value) throws JsonProcessingException {
	    	
	    	System.out.println("***** Updating the Sub Document "+path+" with value "+value);
	    	
	    	PendingTransaction doc = mapper.readValue(value, PendingTransaction.class);
	    	
	    	String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(doc);
        	System.out.println(jsonString);
	    	
	    	collectionClient.document(docId).updateSubDocument(path, doc);
	    	return docId;
	    }
	 
	 public String updateSubDocument(String docId, String path, Integer value) {
	 	
	 	System.out.println("Updating the Sub Document "+path);
	 	collectionClient.document(docId).replaceSubDocument(path, value);
	 	return docId;
	 }
    
    public String deleteDocument(String docId) {
    	
    	collectionClient.document(docId).delete();
    	return docId;
    }
    
    
}
