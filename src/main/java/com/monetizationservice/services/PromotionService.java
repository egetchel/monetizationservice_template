package com.monetizationservice.services;

import com.ezpaymentprocessing.model.PurchaseRequest;

public class PromotionService {
	public static String qualifiy(PurchaseRequest purchaseRequest)
	{
		int purchaseAmount = purchaseRequest.getAmount();
		if (purchaseAmount > 10)
		{
			return "You have qualified for a 5% off coupon on your next purhcase.";
		}
		return null;
	}
}
