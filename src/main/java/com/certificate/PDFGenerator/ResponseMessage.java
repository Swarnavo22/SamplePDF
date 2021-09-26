package com.certificate.PDFGenerator;

public class ResponseMessage {

	private String message;
	
	  public ResponseMessage(String message,String temp) {
	    this.message = message;
	  }

	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }


}