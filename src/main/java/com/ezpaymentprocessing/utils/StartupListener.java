package com.ezpaymentprocessing.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.HttpMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.monetizationservice.services.PromotionService;

/**
 * Class to listed for the start event from the servlet container.  This is used to signal the
 * static configuration management class to determine where we are running (locally or on OpenShift) and
 * to generate endpoint URLs
 * @author E. Getchell
 *
 */
public class StartupListener implements ServletContextListener
{
	ServletContext context;
	public void contextInitialized(ServletContextEvent contextEvent) 
	{
		System.out.println("Context Created Event");
		
		context = contextEvent.getServletContext();
		String contextPath = context.getContextPath();
		
		ConfigManager.generateRestUrls(contextPath);
		
		if (ConfigManager.getGearRegistrationURL() != null)
		{
			RestClient client = new RestClient(); 
			try
			{
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("merchantId", ConfigManager.getGearName()));
				parameters.add(new BasicNameValuePair("promotionUrl", ConfigManager.getPromotionURL()));
				client.sendRequest(ConfigManager.getGearRegistrationURL(), parameters, HttpMethod.POST, String.class);
				ConfigManager.setRemoteRegistrationSuccessful(Boolean.TRUE);
			}
			catch (Exception e)
			{
				System.out.println("WARNING: Could not register remote service. " + e.getMessage());
			}
		}
		
		PromotionService.start();

	}
	public void contextDestroyed(ServletContextEvent contextEvent) 
	{
		context = contextEvent.getServletContext();
		System.out.println("Context Destroyed");
		RestClient client = new RestClient(); 
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("merchantId", ConfigManager.getGearName()));
		client.sendRequest(ConfigManager.getGearRegistrationURL(), parameters, HttpMethod.POST, String.class);

	}
} 

