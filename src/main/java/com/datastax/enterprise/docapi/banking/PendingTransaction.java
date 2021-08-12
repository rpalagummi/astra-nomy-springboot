package com.datastax.enterprise.docapi.banking;

public class PendingTransaction {
	
	private static final long serialVersionUID = 2798538288964412234L;

    private String transactionId;
    
    private int correlationId;
    
    private int userId;
    
    private String message;
    
    private String status;
    
    private String createdBy;
    
    private String createdDate;
    
    /**
     * Default Constructor.
     */
    public PendingTransaction() {}

    /* Getters and Setters */
    
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public int getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(int correlationId) {
		this.correlationId = correlationId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
    

}
