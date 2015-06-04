package com.monetizationservice.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

import com.ezpaymentprocessing.model.PurchaseResponse;
import com.ezpaymentprocessing.utils.ConfigManager;
import com.ezpaymentprocessing.utils.RestClient;

/**
 * Servlet implementation class
 * 
 *  This is to get around the same origin policy that prevents us from posting cross-domain.  Essentially, it forwards the call to the REST Purchase endpoint
 *  to be processed.
 *  
 */
@WebServlet("/PurchaseServlet/*")
public class PurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurchaseServlet() {
        super();
       System.out.println("PurchaseServlet started");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get invoked");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String merchantId = request.getParameter("merchantId");
		String amount = request.getParameter("amount");
		String mobileNumber = request.getParameter("mobileNumber");
		System.out.println("POST: Amount: "+ request.getParameter("amount"));
		RestClient client = new RestClient();
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("merchantId", merchantId));
		parameters.add(new BasicNameValuePair("amount", amount));
		parameters.add(new BasicNameValuePair("mobileNumber", mobileNumber));

		PurchaseResponse purchaseResponse = (PurchaseResponse) client.sendRequest(ConfigManager.getPaymentProcessingURL(), parameters, HttpMethod.GET, PurchaseResponse.class);
		// Total hack
		JSONObject obj = new JSONObject();
		obj.put("approved", purchaseResponse.isApproved());
		obj.put("message", purchaseResponse.getMessage());
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(obj);
		out.flush();
		return;
		
	}

}
