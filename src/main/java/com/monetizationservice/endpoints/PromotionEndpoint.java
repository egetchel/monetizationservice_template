package com.monetizationservice.endpoints;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.ezpaymentprocessing.model.PurchaseRequest;
import com.monetizationservice.services.PromotionService;


// http://localhost:8081/monetizationservice/rest/qualifyPromotion?merchantId=monetizationservice&mobileNumber=5556667777&amount=10

@Path ("/qualifyPromotion")
public class PromotionEndpoint {
	
	@GET
	@Path("/query")
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
		return Response.status(200).entity("{\"status\": \"OK\"}").build();
 
	}
	
	@POST
	@Path("/query")
	@Produces("application/json")
	@Consumes("application/json")
	public Response qualifyPromotion(PurchaseRequest purchaseRequest) 
	{
		System.out.println("[POST] qualifyPromotion(PurchaseRequest): " + purchaseRequest);
		qualify(purchaseRequest);
		return Response.status(200).entity("{\"status\": \"OK\"}").build();
	}
	
	
	private void qualify(PurchaseRequest purchaseRequest)
	{
		String message = PromotionService.qualifiy(purchaseRequest);
		
		if (message != null)
		{
			//https://redhatmw-t-signw940wumytdo57zlpx2eq-dev.ac.gen.ric.feedhenry.com/promotion?mobileNumber=555&message=XYZ
			System.out.println("Invoking promotion Push Notification...\n");
			try {
				
			  			
				ClientRequest request = new ClientRequest("https://redhatmw-t-signw940wumytdo57zlpx2eq-dev.ac.gen.ric.feedhenry.com/promotion?mobileNumber="+purchaseRequest.getMobileNumber()+"&message="+message);
				request.accept("application/json");
				ClientResponse<String> response = request.get(String.class);
		 
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
				}
		 
				BufferedReader br = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(response.getEntity().getBytes())));
		 
				String output;
				System.out.println("Output from Server .... ");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
		 
			  } 
		  catch (Exception E)
		  {
			  E.printStackTrace();
		  }
		}
		

	}


}
