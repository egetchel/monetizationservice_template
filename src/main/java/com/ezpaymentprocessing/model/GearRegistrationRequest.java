package com.ezpaymentprocessing.model;

/**
 * Class representing a remote gear/application registration
 * @author E. Getchell
 *
 */
public class GearRegistrationRequest 
{
	private String merchantId;
	private String promotionUrl;
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getPromotionUrl() {
		return promotionUrl;
	}
	public void setPromotionUrl(String promotionUrl) {
		this.promotionUrl = promotionUrl;
	}
	
	
}
