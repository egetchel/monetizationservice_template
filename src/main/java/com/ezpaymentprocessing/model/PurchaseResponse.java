package com.ezpaymentprocessing.model;

/**
 * Data model for a Purchase Response
 * @author E. Getchell
 *
 */
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
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Approved: [");
		b.append(approved);
		b.append("]\nMessage: [");
		b.append(message);
		b.append("]");
		return b.toString();

	}
	

}
