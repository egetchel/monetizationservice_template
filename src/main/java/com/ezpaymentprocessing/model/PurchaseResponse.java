package com.ezpaymentprocessing.model;

public class PurchaseResponse {
	private boolean approved;
	private String message;
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
