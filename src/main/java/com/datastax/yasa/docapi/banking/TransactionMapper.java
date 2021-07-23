package com.datastax.yasa.docapi.banking;

import com.datastax.stargate.sdk.rest.domain.Row;
import com.datastax.stargate.sdk.rest.domain.RowMapper;

public class TransactionMapper implements RowMapper<PendingTransaction>{
	
	/** {@inheritDoc} */
	@Override
	public PendingTransaction map(Row row) {
		
		PendingTransaction pt = new PendingTransaction();
		pt.setCorrelationId(row.getInt("correlation_id"));
		pt.setCreatedBy(row.getString("created_by"));
		pt.setMessage(row.getString("message"));
		pt.setStatus(row.getString("status"));
		pt.setTransactionId(row.getString("transaction_id"));
		pt.setUserId(row.getInt("user_id"));
		pt.setCreatedDate(row.getString("created_dt"));
		
		return pt;
    }

	
}
