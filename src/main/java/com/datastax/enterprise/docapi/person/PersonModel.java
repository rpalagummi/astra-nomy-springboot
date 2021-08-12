package com.datastax.enterprise.docapi.person;

public class PersonModel {
	
	 private String docId;
	 private String subDocumentPath;
	 private String content;

	  public String getDocId() {
	    return docId;
	  }

	  public void setDocId(String docId) {
	    this.docId = docId;
	  }

	  public String getContent() {
	    return content;
	  }

	  public void setContent(String content) {
	    this.content = content;
	  }

	public String getSubDocumentPath() {
		return subDocumentPath;
	}

	public void setSubDocumentPath(String subDocumentPath) {
		this.subDocumentPath = subDocumentPath;
	}

	  
	  
}
