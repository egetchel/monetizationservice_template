package com.monetizationservice.services;

import com.ezpaymentprocessing.model.PurchaseRequest;

public class PromotionService {
	
	/**
	 * Quick rule to qualify a purchase.
	 * @param purchaseRequest
	 * @return String representing the promotion, or null if not qualified
	 */
	public static String qualifiy(PurchaseRequest purchaseRequest)
	{
		int purchaseAmount = purchaseRequest.getAmount();
		if (purchaseAmount > 10)
		{
			return "You have qualified for a 5% off coupon on your next purhcase at "+purchaseRequest.getMerchantId() + ". Another value-added service brought to you by EZ-Payment Processing";
		}
		return null;
	}
}
