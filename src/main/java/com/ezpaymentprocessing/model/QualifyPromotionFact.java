package com.ezpaymentprocessing.model;

/**
 * Result class from qualifying a promotion
 * @author E. Getchell
 *
 */
public class QualifyPromotionFact 
{
	private Integer purchaseAmount;
	private String merchantId;
	
	// Updated by rules enging
	private String message = null;
	private boolean qualified = false;
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
