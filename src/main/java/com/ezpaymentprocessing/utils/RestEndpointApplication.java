package com.ezpaymentprocessing.utils;

import javax.ws.rs.core.Application;

import com.monetizationservice.endpoints.PromotionEndpoint;

import java.util.HashSet;
import java.util.Set;

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
