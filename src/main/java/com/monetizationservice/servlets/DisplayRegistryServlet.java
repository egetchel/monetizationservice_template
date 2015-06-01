package com.monetizationservice.servlets;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.jboss.resteasy.core.ResourceInvoker;
import org.jboss.resteasy.core.ResourceLocator;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.jboss.resteasy.plugins.stats.DeleteResourceMethod;
import org.jboss.resteasy.plugins.stats.GetResourceMethod;
import org.jboss.resteasy.plugins.stats.HeadResourceMethod;
import org.jboss.resteasy.plugins.stats.OptionsResourceMethod;
import org.jboss.resteasy.plugins.stats.PostResourceMethod;
import org.jboss.resteasy.plugins.stats.PutResourceMethod;
import org.jboss.resteasy.plugins.stats.RegistryData;
import org.jboss.resteasy.plugins.stats.RegistryEntry;
import org.jboss.resteasy.plugins.stats.ResourceMethodEntry;
import org.jboss.resteasy.plugins.stats.SubresourceLocator;
import org.jboss.resteasy.plugins.stats.TraceResourceMethod;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * Taken from:
 * https://github.com/resteasy/Resteasy/blob/Branch_2_3_7/providers/jaxb/src/main/java/org/jboss/resteasy/plugins/stats/RegistryStatsResource.java
 * 
 * @author egetchel
 *
 */

@WebServlet("/RegistryServlet/*")
public class DisplayRegistryServlet extends HttpServlet
{

	public DisplayRegistryServlet()
	{
		super();
		System.out.println("DisplayRegistryServlet starting...");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get invoked");
		try
		{	RegistryData registry = get();
			System.out.println(registry);
			response.getWriter().print(registry);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.getWriter().append(e.toString());
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Post invoked");
	}
	
	  private RegistryData get() throws JAXBException
	   {
	      //ResourceMethodRegistry registry = (ResourceMethodRegistry) ResteasyProviderFactory.getContextData(Registry.class);
		  ResteasyProviderFactory providerFactory=ResteasyProviderFactory.getInstance();
		  ResourceMethodRegistry registry = (ResourceMethodRegistry) providerFactory.getContextData(Registry.class);

	      RegistryData data = new RegistryData();

	      for (String key : registry.getRoot().getBounded().keySet())
	      {
	         List<ResourceInvoker> invokers = registry.getRoot().getBounded().get(key);

	         RegistryEntry entry = new RegistryEntry();
	         data.getEntries().add(entry);
	         entry.setUriTemplate(key);

	         for (ResourceInvoker invoker : invokers)
	         {
	            if (invoker instanceof ResourceMethod)
	            {
	               ResourceMethod rm = (ResourceMethod) invoker;
	               for (String httpMethod : rm.getHttpMethods())
	               {
	                  ResourceMethodEntry method = null;
	                  if (httpMethod.equals("GET")) method = new GetResourceMethod();
	                  else if (httpMethod.equals("PUT")) method = new PutResourceMethod();
	                  else if (httpMethod.equals("DELETE")) method = new DeleteResourceMethod();
	                  else if (httpMethod.equals("POST")) method = new PostResourceMethod();
	                  else if (httpMethod.equals("OPTIONS")) method = new OptionsResourceMethod();
	                  else if (httpMethod.equals("TRACE")) method = new TraceResourceMethod();
	                  else if (httpMethod.equals("HEAD")) method = new HeadResourceMethod();

	                  method.setClazz(rm.getResourceClass().getName());
	                  method.setMethod(rm.getMethod().getName());
	                  AtomicLong stat = rm.getStats().get(httpMethod);
	                  if (stat != null) method.setInvocations(stat.longValue());
	                  else method.setInvocations(0);

	                  if (rm.getProduces() != null)
	                  {
	                     for (MediaType mediaType : rm.getProduces())
	                     {
	                        method.getProduces().add(mediaType.toString());
	                     }
	                  }
	                  if (rm.getConsumes() != null)
	                  {
	                     for (MediaType mediaType : rm.getConsumes())
	                     {
	                        method.getConsumes().add(mediaType.toString());
	                     }
	                  }
	                  entry.getMethods().add(method);

	               }

	            }
	            else
	            {
	               ResourceLocator rl = (ResourceLocator) invoker;
	               SubresourceLocator locator = new SubresourceLocator();
	               locator.setClazz(rl.getMethod().getDeclaringClass().getName());
	               locator.setMethod(rl.getMethod().getName());
	               entry.setLocator(locator);
	            }

	         }

	      }

	      return data;
	   }
}
