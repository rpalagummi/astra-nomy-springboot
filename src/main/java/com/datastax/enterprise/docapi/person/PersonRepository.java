package com.datastax.enterprise.docapi.person;

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

@Repository
public class PersonRepository extends StargateDocumentRepository<Person> {
    
    
	private static final String WORKING_NAMESPACE    = "enterprise";
    private static final String COLLECTION_PERSON    = "person";
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
    public PersonRepository(AstraClient astraClient) {
        super(astraClient.getStargateClient(), 
              astraClient.cqlSession().getKeyspace().get().toString());
        collectionClient = astraClient.apiStargateDocument().namespace(WORKING_NAMESPACE).collection(COLLECTION_PERSON);
    }
    
    @PostConstruct
    public void generateBitData() {
        //Address a1 = new Address(20, "Champ Elysees", "PARIS", 75008);
        //create(new Person("Cedrick", "Lunven", "lala@hotmail.com", Arrays.asList(a1)));
        //create(new Person("John", "Connor", "jc@hotmail.com", Arrays.asList(a1)));
        //create(new Person("RAM", "RAM", "jc@hotmail.com", Arrays.asList(a1)));
    }
    
    /**
     * Sample of custom code
     * @param lastName
     * @return
     */
    public DocumentResultPage<Person> findPersonByLastName(String lastName) {
    	    	
        return searchPageable(SearchDocumentQuery.builder()
                .where("lastname").isEqualsTo(lastName)
                .build());
    }
    
     
    public void createDocument(String docId, Person person) {
    	//docId is not currently used. May be in the future....
    	super.create(person);
    }
    
    
    public String updateDocument(String docId, Person person) {
    	
    	System.out.println("Updating the doc "+docId);
    	collectionClient.document(docId).upsert(person);
    	return docId;
    }
    
    public String updateSubDocument(String docId, String path, Address a) {
    	
    	System.out.println("Updating the Sub Document "+path);
    	collectionClient.document(docId).replaceSubDocument(path, a);
    	return docId;
    }
    
	 public String updateSubDocument(String docId, String path, String value) throws JsonProcessingException {
	    	
	    	System.out.println("***** Updating the Sub Document "+path+" with value "+value);
	    	
	    	Person person = mapper.readValue(value, Person.class);
	    	
	    	String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        	System.out.println(jsonString);
	    	
	    	collectionClient.document(docId).updateSubDocument(path, person);
	    	return docId;
	    }
	 
	 public String updateSubDocument(String docId, String path, Integer value) {
	 	
	 	System.out.println("Updating the Sub Document "+path);
	 	collectionClient.document(docId).replaceSubDocument(path, value);
	 	return docId;
	 }
    
    public String deleteDocument(String docId) {
    	
    	System.out.println("Deleting the doc "+docId);
    	
    	collectionClient.document(docId).delete();
    	return docId;
    }
    
    
}
