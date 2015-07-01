package com.ezpaymentprocessing.model;

/**
 * Fact model for qualifying a promotion in the rules engine
 * 
 * NOTE: The expectation is that the rule will modify this object as a result of a rule
 * being fired.
 * 
 * @author E. Getchell
 *
 */
public class QualifyPromotionFact 
{
	private Integer purchaseAmount;
	private String merchantId;
	
	// Updated by rules engine
	private String message = null;
	// Updated by rules engine
	private boolean qualified = false;
	// Updated by rules engine
	private Integer discount = null;
	

	public Integer getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(Integer amount) {
		this.purchaseAmount = amount;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isQualified() {
		return qualified;
	}

	public void setQualified(boolean qualified) {
		this.qualified = qualified;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	
	


}
