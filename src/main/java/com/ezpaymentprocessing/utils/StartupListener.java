package com.ezpaymentprocessing.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Class to listed for the start event from the servlet container.  This is used to signal the
 * static configuration management class to determine where we are running (locally or on OpenShift) and
 * to generate endpoint URLs
 * @author egetchel
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
				client.sendGet(ConfigManager.getGearRegistrationURL(), "merchantId=" + ConfigManager.getGearName()+ "&promotionURL=" + ConfigManager.getPromotionURL());
				ConfigManager.setRemoteRegistrationSuccessful(Boolean.TRUE);
			}
			catch (Exception e)
			{
				System.out.println("WARNING: Could not register remote service. " + e.getMessage());
			}
		}
		
		// set variable to servlet context
		//context.setAttribute("TEST", "TEST_VALUE");
	}
	public void contextDestroyed(ServletContextEvent contextEvent) 
	{
		context = contextEvent.getServletContext();
		System.out.println("Context Destroyed");
	}
} 

