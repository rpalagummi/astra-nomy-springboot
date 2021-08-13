package com.datastax.enterprise.banking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.astra.sdk.AstraClient;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

@Repository
public class TransactionCQLAPIRepository {
	
	@Autowired
	 private AstraClient astraClient;
	
	private static final String WORKING_KEYSPACE = "enterprise";
	private static final String WORKING_TABLE    = "pendingtransactions_by_correlationid" ;
	
	private static final String TABLE_NAME = WORKING_KEYSPACE+"."+WORKING_TABLE;

	public TransactionCQLAPIRepository(AstraClient astraClient) {
		super();
		this.astraClient = astraClient;
	}
	
	
	public void update(String txId, int correlationId) {
		
		
		SimpleStatement stmt = SimpleStatement.builder("UPDATE "+TABLE_NAME+ " SET status = \'Complete\' WHERE transaction_id = ? AND correlation_id = ?")
	        		.addPositionalValue(txId)
	        		.addPositionalValue(correlationId)
	                .build();
		     
		     astraClient.cqlSession().execute(stmt);
	}

}
