package com.datastax.enterprise.banking;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.astra.sdk.AstraClient;
import com.datastax.stargate.sdk.rest.ApiRestClient;
import com.datastax.stargate.sdk.rest.TableClient;
import com.datastax.stargate.sdk.rest.domain.SearchTableQuery;

@Repository
public class TransactionRestAPIRepository {

//  @Autowired
//  private AstraClient astraClient;
//  
  private static ApiRestClient clientApiRest;
  
  private static final String WORKING_KEYSPACE = "enterprise";
  private static final String WORKING_TABLE    = "pendingtransactions_by_correlationid" ;
  
  private TableClient transactionTable; 
  
  
  
  public TransactionRestAPIRepository(AstraClient astraClient) {
	super();
	
	clientApiRest = astraClient.apiStargateData();
	transactionTable = clientApiRest.keyspace(WORKING_KEYSPACE).table(WORKING_TABLE);
		
	}
	

	public void upsert(Map<String, Object> record) {
		
		transactionTable.upsert(record);
		
	}
	
	
	public List<PendingTransaction> search(String column, String value){
		
		return transactionTable.search(SearchTableQuery.builder()
                .where(column).isEqualsTo(value)
                .build(), new TransactionMapper()).getResults();
		
	}

	
}
