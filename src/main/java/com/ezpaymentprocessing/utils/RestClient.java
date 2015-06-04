package com.ezpaymentprocessing.utils;

import java.util.List;

import javax.ws.rs.HttpMethod;

import org.apache.http.NameValuePair;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.json.simple.JSONObject;

/**
 * Class used to send Rest GET or POST
 * @author E. Getchell
 *
 */
public class RestClient {
	

	/**
	 * Send a Rest request
	 * @param endpoint The fully qualified URL
	 * @param parameters A List of parameters to send. Based on the method (GET or POST) these will be used in the following manner:
	 * a method of GET will expose these parmeters on the query string.  A POST will sent these as a JSON object in the message body
	 * @param method GET or POST
	 * @param returnType The Class of the return type the remote method is returning.  This will be deserialized into the coresponding object
	 */
	public Object sendRequest(String endpoint, List<NameValuePair> parameters, String method, Class<?> returnType)
	{
		try 
		{
			ClientRequest request = new ClientRequest(endpoint);
			ClientResponse<?> response = null;
			
			if (HttpMethod.GET.equals(method))
			{
				if (parameters != null)
				{
					for (NameValuePair parameter : parameters)
					{
						request.queryParameter(parameter.getName(), parameter.getValue());
					}
				}
				request.accept("application/json");
				System.out.println("Sending GET :" + request.getUri());
				response = request.get(returnType);
				
			}
			else if (HttpMethod.POST.equals(method))
			{
				
				if (parameters != null)
				{	
					JSONObject obj = new JSONObject();
					for (NameValuePair parameter : parameters)
					{
						obj.put(parameter.getName(), parameter.getValue());
					}
					request.body("application/json", obj);
				}
				request.accept("application/json");
				
				System.out.println("Sending POST :" + request);
				response = request.post(returnType);
			}
	 
			if (response.getStatus() != 200) 
			{
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
	 
			Object deserializedObject = response.getEntity(returnType);
			System.out.println("Response: " + deserializedObject);
			return deserializedObject;
	 
		  } 
	  catch (Exception E)
	  {
		  E.printStackTrace();
	  }
	  return null;
	}

}
