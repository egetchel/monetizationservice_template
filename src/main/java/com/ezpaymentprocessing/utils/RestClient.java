package com.ezpaymentprocessing.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class RestClient {
	
	
	public void sendGet(String endpoint, String queryParameters)
	{
		try {
			
  			String URL = endpoint + "?" + queryParameters;
  			System.out.println ("Sending GET request to " + URL);
			//ClientRequest request = new ClientRequest("https://redhatmw-t-signw940wumytdo57zlpx2eq-dev.ac.gen.ric.feedhenry.com/promotion?mobileNumber="+purchaseRequest.getMobileNumber()+"&message="+message);
			ClientRequest request = new ClientRequest(URL);
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
