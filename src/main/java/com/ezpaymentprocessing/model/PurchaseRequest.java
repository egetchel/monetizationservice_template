package com.ezpaymentprocessing.model;

/**
 * Data object modeling a purchase request.
 * 
 * @author egetchel
 *
 */
public class PurchaseRequest {
	private String merchantId;
	private Integer amount;
	private String mobileNumber;
	
	public String getMerchantId() {
		return merchantId;  
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobilePhone) {
		this.mobileNumber = mobilePhone;
	}
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Merchant Id: [");
		b.append(merchantId);
		b.append("]\nAmount: [");
		b.append(amount);
		b.append("]\nMoibile Phone: [");
		b.append(mobileNumber);
		b.append("]");
		return b.toString();
	}

}
