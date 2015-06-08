package com.monetizationservice.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ezpaymentprocessing.model.GenericResponse;
import com.ezpaymentprocessing.model.PurchaseRequest;
import com.ezpaymentprocessing.model.QualifyPromotionFact;
import com.ezpaymentprocessing.utils.RestClient;
import com.monetizationservice.services.PromotionService;
import com.monetizationservice.services.SmsService;


// http://localhost:8081/monetizationservice/rest/qualifyPromotion?merchantId=monetizationservice&mobileNumber=5556667777&amount=10

@Path ("/qualifyPromotion")
public class PromotionEndpoint {
	
	@GET
	@Produces("application/json")
	public Response qualifyPromotion(
			@QueryParam("merchantId") String merchantId, 
			@QueryParam("amount") String amount,
			@QueryParam("mobileNumber") String mobileNumber,
			@Context HttpServletRequest request ){
		
		PurchaseRequest purchaseRequest = new PurchaseRequest();
		purchaseRequest.setMerchantId(merchantId);
		purchaseRequest.setAmount(Integer.parseInt(amount));
		purchaseRequest.setMobileNumber(mobileNumber);
		qualify(purchaseRequest);
		System.out.println("[GET] qualifyPromotion: MerchantId[" + merchantId + "] Amount: [ " + amount + "] Mobile Phone: [" + mobileNumber +"] for contextPath: " + request.getContextPath());
		// Simply return the fact that we received the qualification request
		GenericResponse qualifyResponse = new GenericResponse();
		return Response.status(200).entity(qualifyResponse).build();
 
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response qualifyPromotion(PurchaseRequest purchaseRequest) 
	{
		System.out.println("[POST] qualifyPromotion(PurchaseRequest): " + purchaseRequest);
		qualify(purchaseRequest);
		// Simply return the fact that we received the qualification request
		GenericResponse qualifyResponse = new GenericResponse();
		return Response.status(200).entity(qualifyResponse).build();

	}
	
	
	private void qualify(PurchaseRequest purchaseRequest)
	{
		QualifyPromotionFact fact = PromotionService.qualifiy(purchaseRequest);
		
		if (fact.isQualified())
		{
			//https://redhatmw-t-signw940wumytdo57zlpx2eq-dev.ac.gen.ric.feedhenry.com/promotion?mobileNumber=555&message=XYZ
			System.out.println("Invoking promotion Push Notification...\n");
			try 
			{
				// Send to Feed Henry
				RestClient client = new RestClient();
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("merchantId", purchaseRequest.getMerchantId()));
				parameters.add(new BasicNameValuePair("mobileNumber", purchaseRequest.getMobileNumber()));
				parameters.add(new BasicNameValuePair("message", fact.getMessage()));
				
				client.sendRequest("https://redhatmw-t-signw940wumytdo57zlpx2eq-dev.ac.gen.ric.feedhenry.com/promotion", parameters, HttpMethod.GET, String.class); 
				
				// Send to Twilio
				SmsService smsClient = new SmsService();
				smsClient.send(purchaseRequest.getMobileNumber(), fact.getMessage());
		 
			  } 
		  catch (Exception E)
		  {
			  E.printStackTrace();
		  }
		}
		

	}


}
