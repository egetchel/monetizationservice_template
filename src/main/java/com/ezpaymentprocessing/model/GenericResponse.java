package com.ezpaymentprocessing.model;

/**
 * Data modeling a generic response
 * @author E. Getchell
 *
 */
public class GenericResponse 
{
	private String status = "OK";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("status [");
		builder.append(status);
		builder.append("]");
		return builder.toString();
		
	}
	
//	

}
