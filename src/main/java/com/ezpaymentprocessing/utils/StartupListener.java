package com.ezpaymentprocessing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.HttpMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ezpaymentprocessing.model.InventoryItem;
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
		
		// Load the inventory
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("inventory.dat");
		
		try {
			List<InventoryItem>i = new ArrayList<InventoryItem>();
			
			//File file = new File("inventory.dat");
			//FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{
				int commaLocation = line.indexOf(',');
				String description = line.substring(0,commaLocation);
				String price = line.substring(++commaLocation, line.length());
				InventoryItem item = new InventoryItem();
				item.setDescription(description);
				item.setPrice(price);
				i.add(item);

			}
			in.close();
			//System.out.println("Contents of file:");
			//System.out.println(i.toString());
			ConfigManager.setInventory(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (ConfigManager.getGearRegistrationURL() != null)
		{
			// Register this application with the "mothership" - EZPayment Processing
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
		// Strangeness is about with this method enabled.  Need to dig into some of this as this was 
		// a last minute addition to prevent having to cycle the EZPayment processing application during gear delete operations in OSE
/*		
		context = contextEvent.getServletContext();
		System.out.println("Context Destroyed");
		RestClient client = new RestClient(); 
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("merchantId", ConfigManager.getGearName()));
		client.sendRequest(ConfigManager.getGearRegistrationURL(), parameters, HttpMethod.POST, String.class);
*/		

	}
} 

