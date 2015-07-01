package com.monetizationservice.services;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*; 

/**
 * Class used to send Text messages directly from the web application via Twilio.
 * @author egetchel
 *
 */
public class SmsService 
{
	// Twilio account SID
	public static final String ACCOUNT_SID = ""; 
	// Twilio account authorizatin token
	public static final String AUTH_TOKEN = "";
	
	public void send(String targetNumber, String textMessageBody) throws TwilioRestException
	{

			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
		 
			 // Build the parameters 
			 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
			 params.add(new BasicNameValuePair("To", targetNumber));
			 // Put in (xxx) xxx-xxxx format
			 params.add(new BasicNameValuePair("From", "")); 
			 params.add(new BasicNameValuePair("Body", textMessageBody));   
		 
			 MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
			 Message message = messageFactory.create(params); 
			 System.out.println(message.getSid()); 

	}
}
