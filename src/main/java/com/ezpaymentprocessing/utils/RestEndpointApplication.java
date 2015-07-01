package com.ezpaymentprocessing.utils;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.monetizationservice.endpoints.PromotionEndpoint;

import java.util.HashSet;
import java.util.Set;

/**
 * Class used to manually register REST endpoints with an application container
 * 
 * This is being used as the annotation scanner for RestEasy does not seem to fire in OSE V2, but
 * will fire locally.
 * 
 * @author E. Getchell
 *
 */
@ApplicationPath("/rest")
public class RestEndpointApplication extends Application 
{
	  private Set<Object> singletons = new HashSet<Object>();
	  
	  public RestEndpointApplication()
	  {
		  singletons.add(new PromotionEndpoint());
	  }
	  
	  @Override
	  public Set<Object> getSingletons() 
	  {
	    return singletons;
	  }

}
