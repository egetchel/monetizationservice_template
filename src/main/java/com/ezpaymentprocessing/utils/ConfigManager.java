package com.ezpaymentprocessing.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;

import com.ezpaymentprocessing.model.InventoryItem;

/**
 * Class to manage programatically defined configuration information, such as REST URLs 
 * @author E. Getchell
 *
 */
public class ConfigManager 
{

	public static String PURCHASE_RESOURCE_ID = "/rest/purchase";
	public static String PROMOTION_RESOURCE_ID = "/rest/qualifyPromotion/";
	public static String GEAR_REGISTRATION_RESOURCE_ID = "/rest/registerGear";
	public static String GEAR_UNREGISTRATION_RESOURCE_ID = "/rest/unregisterGear";
	
	private static String PAYMENT_SERVER_NAME = null;
	private static String PROMOTION_SERVER_NAME = null;
	
	private static String PAYMENT_SERVER_CONTEXT_ROOT = "ezpaymentprocessing";
	// Dynamically calculated at server startup
	private static String PROMOTION_SERVER_CONTEXT_ROOT = null;
	
	// Fully qualified URLs of the REST endpoints
	private static String paymentProcessingURL = null;
	private static String promotionURL = null;
	private static String gearRegistrationURL = null;
	private static String gearUnregistrationURL = null;
	
	private static Boolean remoteRegistrationSuccessful = Boolean.FALSE;
	
	private static String gearName = null;
	private static String gearPort = null;
	
	
	private static boolean isLocal = false;
	
	private static List<InventoryItem> inventory = new ArrayList<InventoryItem>();
	
	
	public static void generateRestUrls(String contextPath)
	{
		System.out.println("Local ContextPath: "+ contextPath);

		// Working locally has the context path start with a slash
		if (contextPath.startsWith("/"))
		{
			isLocal = true;
		}
		else
		{
			isLocal = false;
		}
		
		if (isLocal)
		{
			// local development, the "gear" name will be the context path of the application
			gearName = contextPath.substring(1);
			try
			{
				gearPort = getLocalPort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				gearPort = "8081";
			}
			
			
			// Payment Server is always hard coded
			PAYMENT_SERVER_NAME = "http://localhost:8080/"; 
			paymentProcessingURL = PAYMENT_SERVER_NAME + PAYMENT_SERVER_CONTEXT_ROOT + PURCHASE_RESOURCE_ID;
			
			PROMOTION_SERVER_NAME = "http://localhost:" + gearPort ;
			PROMOTION_SERVER_CONTEXT_ROOT = contextPath;
			promotionURL = PROMOTION_SERVER_NAME + PROMOTION_SERVER_CONTEXT_ROOT + PROMOTION_RESOURCE_ID;
			
			// The Gear registration service resides on the Payment Server
			gearRegistrationURL = PAYMENT_SERVER_NAME + PAYMENT_SERVER_CONTEXT_ROOT + GEAR_REGISTRATION_RESOURCE_ID;
			gearUnregistrationURL = PAYMENT_SERVER_NAME + PAYMENT_SERVER_CONTEXT_ROOT + GEAR_UNREGISTRATION_RESOURCE_ID;
			
		}
		else
		{
			try 
			{
			// Payment Server is always hard coded
			PAYMENT_SERVER_NAME = "http://ezpaymentprocessing-egetchel.rhcloud.com";
			// There is no context root when running in OpenShift, so clean it out in case it's used accidentally.
			PAYMENT_SERVER_CONTEXT_ROOT = "";
			paymentProcessingURL = PAYMENT_SERVER_NAME + PURCHASE_RESOURCE_ID;
			
			String dnsName = System.getenv("OPENSHIFT_APP_DNS");
			
			System.out.println("DNS Name: " + dnsName);
			PROMOTION_SERVER_NAME = "http://" + dnsName;
			
			// Remove everything to the right of the dash in the FQN DNS name.
			gearName = dnsName.substring(0,dnsName.indexOf('-'));
			
			promotionURL = PROMOTION_SERVER_NAME + PROMOTION_RESOURCE_ID;
		
			gearRegistrationURL = PAYMENT_SERVER_NAME +  GEAR_REGISTRATION_RESOURCE_ID;
			}
			catch (Exception e)
			{
				// Something happened in the string parsing... dump the exception
				e.printStackTrace();
			}

		}
		
		
		
	}
	
	/**
	 * Returns the host and port of this server
	 * @return
	 */
	public static String getPromotionServerName()
	{
		return PROMOTION_SERVER_NAME;
	}

	/**
	 * Returns the fully qualified URL for the payment service.
	 * @return
	 */
	public static String getPaymentProcessingURL() {
		return paymentProcessingURL;
	}

	/**
	 * Returns the fully qualified URL for the promotion service
	 * @return
	 */
	public static String getPromotionURL() {
		return promotionURL;
	}
	
	/**
	 * Returns the URL where applications need to register themselves to.
	 * @return
	 */
	public static String getGearRegistrationURL()
	{
		return gearRegistrationURL;
	}

	/**Unregistration endpoint for when this application is shutting down
	 * 
	 * @return
	 */
	public static String getGearUnregistrationURL() {
		return gearUnregistrationURL;
	}

	/**
	 * Return the name of this gear (or application, if working locally)
	 * @return
	 */
	public static String getGearName() {
		return gearName;
	}
	
	

	public static String getGearPort() {
		return gearPort;
	}
	
	

	public static Boolean getRemoteRegistrationSuccessful() 
	{
		return remoteRegistrationSuccessful;
	}

	public static void setRemoteRegistrationSuccessful(Boolean remoteRegistrationSuccessful) 
	{
		ConfigManager.remoteRegistrationSuccessful = remoteRegistrationSuccessful;
	}
	
	

	public static List<InventoryItem> getInventory() {
		return inventory;
	}

	public static void setInventory(List<InventoryItem> inventory) {
		ConfigManager.inventory = inventory;
	}

	/**
	 * Hack to return the local port that the HTTP listener is on - this assumes that only one listener is running.
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws UnknownHostException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 */
	private static String getLocalPort() throws MalformedObjectNameException,
		NullPointerException, UnknownHostException,
		AttributeNotFoundException, InstanceNotFoundException,
		MBeanException, ReflectionException 
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"),	Query.value("HTTP/1.1")));
		String hostname = InetAddress.getLocalHost().getHostName();
		InetAddress[] addresses = InetAddress.getAllByName(hostname);
		ArrayList<String> endPoints = new ArrayList<String>();
		for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) 
		{
			ObjectName obj = i.next();
			String scheme = mbs.getAttribute(obj, "scheme").toString();
			String port = obj.getKeyProperty("port");
			return port;
/*			
			for (InetAddress addr : addresses) 
			{
				String host = addr.getHostAddress();
				String ep = scheme + "://" + host + ":" + port;
				endPoints.add(ep);
			}
*/			
		}
		return null;
	}
}
